package pl.sdacademy.domain.verification

import spock.lang.Specification

class VerificationCodeGeneratorSpecTest extends Specification {
    def "verification code should consist of 5 digits"() {
        given:
        def verificationCodeGenerator = new VerificationCodeGenerator()

        when:
        def result = verificationCodeGenerator.generateEmailVerificationCode()

        then:
        result =~ /[0-9]{5}/
    }
}
