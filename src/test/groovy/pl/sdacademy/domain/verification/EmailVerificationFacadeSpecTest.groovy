package pl.sdacademy.domain.verification

import pl.sdacademy.domain.user.User
import spock.lang.Specification
import spock.lang.Unroll

class EmailVerificationFacadeSpecTest extends Specification {

    def verificationCodeRepository = Mock EmailVerificationCodeRepository
    def verificationFacade = new EmailVerificationFacade(verificationCodeRepository)

    @Unroll
    "should return #expectedResult if verification code is found and has a value #expectedResult"() {
        given:
        def user = Mock User
        def verificationCode = Mock EmailVerificationCode

        when:
        def actualResult = verificationFacade.hasVerifiedEmail(user)

        then:
        1 * verificationCodeRepository.getByUserWhereEmailsAreMatching(user) >> verificationCode

        and:
        1 * verificationCode.getVerified() >> expectedResult

        and:
        expectedResult == actualResult

        where:
        expectedResult << [true, false]
    }

    @Unroll
    "should return false if user has no verifications"() {
        given:
        def user = Mock User

        when:
        def result = verificationFacade.hasVerifiedEmail(user)

        then:
        1 * verificationCodeRepository.getByUserWhereEmailsAreMatching(user) >> null

        and:
        result == false
    }
}
