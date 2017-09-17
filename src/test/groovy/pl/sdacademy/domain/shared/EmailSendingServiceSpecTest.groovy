package pl.sdacademy.domain.shared

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import spock.lang.Specification

import static pl.sdacademy.domain.shared.EmailSubject.VERIFICATION_CODE
import static rx.Observable.just

class EmailSendingServiceSpecTest extends Specification {

    def mailSender = Mock(JavaMailSender)
    def messageResolver = Mock(EmailSubjectMessageResolver)
    def service = new EmailSendingService(mailSender, messageResolver)

    def "should send message"() {
        when:
        service.sendSimpleMessage("test@sdacademy.pl", "test subject", "test content")

        then:
        1 * mailSender.send(new SimpleMailMessage(to: "test@sdacademy.pl",
                                                  subject: "test subject",
                                                  text: "test content"))
    }

    def "should resolve subject and send message"() {
        when:
        service.sendSimpleMessage("test@sdacademy.pl", VERIFICATION_CODE, "test content")

        then:
        1 * messageResolver.resolveMessageFor(VERIFICATION_CODE) >> just("test subject")

        and:
        1 * mailSender.send(new SimpleMailMessage(to: "test@sdacademy.pl",
                                                  subject: "test subject",
                                                  text: "test content"))
    }
}
