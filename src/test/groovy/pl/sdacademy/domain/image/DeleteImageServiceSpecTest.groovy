package pl.sdacademy.domain.image

import pl.sdacademy.domain.authorization.AuthorizationFacade
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception
import pl.sdacademy.domain.user.User
import pl.sdacademy.domain.verification.EmailVerificationFacade
import spock.lang.Specification

import static rx.Observable.just

class DeleteImageServiceSpecTest extends Specification {

    def imageRepository = Mock ImageRepository
    def authorizationFacade = Mock AuthorizationFacade
    def emailVerificationFacade = Mock EmailVerificationFacade
    def service = new DeleteImageService(imageRepository, authorizationFacade, emailVerificationFacade)

    def "should anonymize image data instead of deleting it"() {
        given:
        def user = Mock User
        def image = Mock Image

        when:
        service.deleteImage(1L)

        then:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationFacade.hasVerifiedEmail(user) >> true

        and:
        1 * imageRepository.findAccessibleByUserById(user, 1L) >> image

        and:
        1 * image.setUser(null)
        1 * image.setIsPublic(false)

        and:
        0 * imageRepository.delete(image)
        1 * imageRepository.save(image) >> image
    }

    def "should throw if user is not verified"() {
        given:
        def user = Mock User

        when:
        service.deleteImage(1L)

        then:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationFacade.hasVerifiedEmail(user) >> false

        and:
        0 * _._

        and:
        thrown Forbidden403Exception
    }

    def "should throw if image is not accessible by the user"() {
        given:
        def user = Mock User

        when:
        service.deleteImage(1L)

        then:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationFacade.hasVerifiedEmail(user) >> true

        and:
        1 * imageRepository.findAccessibleByUserById(user, 1L) >> null

        and:
        0 * _._

        and:
        thrown NotFound404Exception
    }
}
