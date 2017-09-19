package pl.sdacademy.domain.image

import org.springframework.core.convert.converter.Converter
import org.springframework.core.io.Resource
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import pl.sdacademy.domain.authorization.AuthorizationFacade
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception
import pl.sdacademy.domain.user.User
import spock.lang.Specification
import spock.lang.Unroll

import static rx.Observable.just

class GetImageServiceSpecTest extends Specification {

    def authorizationFacade = Mock AuthorizationFacade
    def imageRepository = Mock ImageRepository
    def imageStorage = Mock ImageStorage
    def imageTagRepository = Mock ImageTagRepository
    def service = new GetImageService(authorizationFacade, imageRepository, imageStorage, imageTagRepository)

    @Unroll
    "should return image if the user is owner=#isOwner and image is public=#isPublic accessible by the user"() {
        given:
        def image = Mock Image
        def user = Mock User
        def imageResource = Mock Resource

        when:
        def result = service.getImage(0L).toBlocking().single()

        then:
        1 * imageRepository.findOne(0L) >> image

        and:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * image.getUser() >> (isOwner ? user : Mock(User))
        1 * image.getIsPublic() >> isPublic

        and:
        1 * image.getFileName() >> "image.png"

        and:
        1 * imageStorage.loadAsResource("image.png") >> imageResource

        and:
        result == imageResource

        where:
        isOwner | isPublic
        true    | true
        false   | true
        true    | false
    }

    def "should throw if image is not present"() {
        when:
        service.getImage(0L).toBlocking().single()

        then:
        1 * imageRepository.findOne(0L) >> null

        and:
        0 * _._

        and:
        thrown NotFound404Exception
    }

    def "should throw if image is not accessible by the user"() {
        given:
        def image = Mock Image
        def user = Mock User

        when:
        service.getImage(0L).toBlocking().single()

        then:
        1 * imageRepository.findOne(0L) >> image

        and:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * image.getUser() >> Mock(User)
        1 * image.getIsPublic() >> false

        and:
        0 * _._

        and:
        thrown Forbidden403Exception
    }

    def "should get images matching tags"() {
        given:
        def pageable = Mock Pageable
        def user = Mock User
        def page = Mock Page
        def image = Mock Image
        def imageTag = Mock ImageTag

        when:
        def images = service.getImages(pageable, ["valid tag"]).toBlocking().single().content

        then:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * imageRepository.findAllAccessibleByUserByPageAndTags(["valid tag"], user, pageable) >> page

        and:
        1 * page.map(_ as Converter) >> { Converter converter -> new PageImpl([converter.convert(image)]) }

        and:
        1 * image.getUser() >> user
        1 * user.getUsername() >> "alice"
        1 * imageTagRepository.findAllByImages(image) >> [imageTag]
        1 * imageTag.getName() >> "valid tag"

        and:
        images.size() == new Integer(1)
        images[0].tags == ["valid tag"]
        images[0].owner == "alice"
    }

}
