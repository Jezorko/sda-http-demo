package pl.sdacademy.domain.verification

import pl.sdacademy.domain.authorization.AuthorizationFacade
import pl.sdacademy.domain.shared.EmailSendingService
import pl.sdacademy.domain.user.User
import spock.lang.Specification

import static pl.sdacademy.domain.shared.EmailSubject.VERIFICATION_CODE
import static rx.Observable.just

class SendEmailVerificationServiceSpecTest extends Specification {

    def authorizationFacade = Mock AuthorizationFacade
    def emailService = Mock EmailSendingService
    def verificationCodeRepository = Mock EmailVerificationCodeRepository
    def verificationCodeGenerator = Mock VerificationCodeGenerator
    def service = new SendEmailVerificationService(authorizationFacade, emailService, verificationCodeRepository, verificationCodeGenerator)

    def "should delete an old verification if it's present and create a new verification code and send it"() {
        given:
        def user = Mock User

        when:
        service.sendEmailVerification()

        then:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * verificationCodeRepository.deleteByUser(user)

        and:
        1 * user.getEmail() >> "test@sdacademy.pl"
        1 * verificationCodeGenerator.generateEmailVerificationCode() >> "12345"
        1 * verificationCodeRepository.save(_ as EmailVerificationCode) >> { EmailVerificationCode code -> code }
        1 * emailService.sendSimpleMessage("test@sdacademy.pl", VERIFICATION_CODE, "12345")
    }
}
