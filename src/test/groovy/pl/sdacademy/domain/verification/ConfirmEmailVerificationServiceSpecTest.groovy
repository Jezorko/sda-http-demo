package pl.sdacademy.domain.verification

import pl.sdacademy.domain.authorization.AuthorizationFacade
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception
import pl.sdacademy.domain.user.User
import pl.sdacademy.domain.verification.dto.request.ConfirmEmailVerificationRequest
import spock.lang.Specification

import static rx.Observable.just

class ConfirmEmailVerificationServiceSpecTest extends Specification {

    def authorizationFacade = Mock AuthorizationFacade
    def emailVerificationRepository = Mock EmailVerificationCodeRepository
    def service = new ConfirmEmailVerificationService(authorizationFacade, emailVerificationRepository)

    def "should confirm the verification if the request is correct and user email is matching the email from verification"() {
        given:
        def request = new ConfirmEmailVerificationRequest(verificationCode: "123")
        def user = Mock User
        def verification = Mock EmailVerificationCode

        when:
        service.confirmEmailVerification request

        then:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationRepository.findByUserWhereEmailsAreMatching(user) >> verification

        and:
        1 * verification.getValue() >> "123"
        1 * verification.setVerified(true)
        1 * emailVerificationRepository.save(verification)
    }

    def "should throw if verification is not found for the user and email"() {
        given:
        def request = new ConfirmEmailVerificationRequest(verificationCode: "123")
        def user = Mock User

        when:
        service.confirmEmailVerification request

        then:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationRepository.findByUserWhereEmailsAreMatching(user) >> null

        and:
        0 * _._

        and:
        thrown NotFound404Exception
    }

    def "should throw if the verification code value is not valid"() {
        given:
        def request = new ConfirmEmailVerificationRequest(verificationCode: "123")
        def user = Mock User
        def verification = Mock EmailVerificationCode

        when:
        service.confirmEmailVerification request

        then:
        1 * authorizationFacade.getByToken() >> just(user)
        1 * emailVerificationRepository.findByUserWhereEmailsAreMatching(user) >> verification

        and:
        1 * verification.getValue() >> "000"

        and:
        0 * _._

        and:
        thrown BadRequest400Exception
    }

}
