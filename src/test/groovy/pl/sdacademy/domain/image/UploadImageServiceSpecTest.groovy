package pl.sdacademy.domain.image

import org.springframework.web.multipart.MultipartFile
import pl.sdacademy.domain.authorization.AuthorizationFacade
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception
import pl.sdacademy.domain.user.User
import pl.sdacademy.domain.verification.EmailVerificationFacade
import spock.lang.Specification

import static java.lang.Boolean.TRUE
import static java.util.Optional.empty
import static java.util.Optional.of
import static rx.Observable.just

class UploadImageServiceSpecTest extends Specification {
    def imageStorage = Mock ImageStorage
    def fileNameGenerator = Mock ImageFileNameGenerator
    def imageRepository = Mock ImageRepository
    def authorizationFacade = Mock AuthorizationFacade
    def imageTagRepository = Mock ImageTagRepository
    def emailVerificationFacade = Mock EmailVerificationFacade
    def service = new UploadImageService(imageStorage, fileNameGenerator, imageRepository, authorizationFacade, imageTagRepository, emailVerificationFacade)

    def "should save image and assign tags to it"() {
        given:
        def file = Mock MultipartFile
        def user = Mock User
        def image = Mock Image
        def imageTag = Mock ImageTag
        def imagesListInTag = Mock List

        when:
        def newImageId = service.uploadImage(file, TRUE, ["valid tag"]).toBlocking().single().id

        then:
        1 * fileNameGenerator.generate() >> "testFileName"

        and:
        1 * file.getContentType() >> "image/png"

        and:
        1 * imageStorage.store(file, "testFileName")

        and:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationFacade.hasVerifiedEmail(user) >> true

        and:
        1 * imageRepository.save(_ as Image) >> image

        and:
        1 * imageTagRepository.findByName(_ as String) >> of(imageTag)
        1 * imageTag.getImages() >> imagesListInTag
        1 * imagesListInTag.add(_ as Image)
        1 * imageTagRepository.save([imageTag])

        and:
        1 * image.getId() >> 1L

        and:
        1L == newImageId
    }

    def "should save image and create a tag if it's not present"() {
        given:
        def file = Mock MultipartFile
        def user = Mock User
        def image = Mock Image

        when:
        def newImageId = service.uploadImage(file, TRUE, ["valid tag"]).toBlocking().single().id

        then:
        1 * fileNameGenerator.generate() >> "testFileName"

        and:
        1 * file.getContentType() >> "image/png"

        and:
        1 * imageStorage.store(file, "testFileName")

        and:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationFacade.hasVerifiedEmail(user) >> true

        and:
        1 * imageRepository.save(_ as Image) >> image

        and:
        1 * imageTagRepository.findByName(_ as String) >> empty()
        1 * imageTagRepository.save(_ as List)

        and:
        1 * image.getId() >> 1L

        and:
        1L == newImageId
    }

    def "should throw if file content type is invalid"() {
        given:
        def file = Mock MultipartFile

        when:
        service.uploadImage(file, TRUE, ["valid tag"]).toBlocking().single()

        then:
        1 * fileNameGenerator.generate() >> "testFileName"

        and:
        1 * file.getContentType() >> "text/plain"

        and:
        0 * _._

        and:
        thrown BadRequest400Exception
    }

    def "should throw if the user is not verified"() {
        given:
        def file = Mock MultipartFile
        def user = Mock User

        when:
        service.uploadImage(file, TRUE, ["valid tag"]).toBlocking().single()

        then:
        1 * fileNameGenerator.generate() >> "testFileName"

        and:
        1 * file.getContentType() >> "image/png"

        and:
        1 * imageStorage.store(file, "testFileName")

        and:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationFacade.hasVerifiedEmail(user) >> false

        and:
        0 * _._

        and:
        thrown Forbidden403Exception
    }

}
