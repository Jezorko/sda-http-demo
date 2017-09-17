package pl.sdacademy.domain.verification

import pl.sdacademy.domain.authorization.AuthorizationFacade
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception
import pl.sdacademy.domain.user.User
import spock.lang.Specification

import static rx.Observable.just

class GetEmailVerificationServiceSpecTest extends Specification {

    def authorizationFacade = Mock AuthorizationFacade
    def verificationCodeRepository = Mock EmailVerificationCodeRepository
    def service = new GetEmailVerificationService(authorizationFacade, verificationCodeRepository)

    def "should return verification code if the user email is matching"() {
        given:
        def user = Mock User
        def verification = Mock EmailVerificationCode

        when:
        def result = service.getVerificationCode().toBlocking().single()

        then:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * verificationCodeRepository.findByUserWhereEmailsAreMatching(user) >> verification
        1 * verification.getValue() >> "123"

        and:
        "123" == result.verificationCode
    }

    def "should throw if the verification code was not found"() {
        given:
        def user = Mock User

        when:
        service.getVerificationCode().toBlocking().single()

        then:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * verificationCodeRepository.findByUserWhereEmailsAreMatching(user) >> null

        and:
        0 * _._

        and:
        thrown NotFound404Exception
    }
}
