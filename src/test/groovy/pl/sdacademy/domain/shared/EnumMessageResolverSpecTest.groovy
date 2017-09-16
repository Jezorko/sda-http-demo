package pl.sdacademy.domain.shared

import org.springframework.context.MessageSource
import pl.sdacademy.infrastructure.properties.ClientProperties
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import java.util.function.Supplier

import static pl.sdacademy.domain.shared.EnumMessageResolverSpecTest.TestEnum.TEST_ENUM_VALUE
import static pl.sdacademy.domain.shared.HeaderNames.ACCEPT_LANGUAGE

class EnumMessageResolverSpecTest extends Specification {

    def enumValue = TEST_ENUM_VALUE
    def messageSource = Mock MessageSource
    def httpRequest = Mock HttpServletRequest
    def defaultNameGetter = Mock Supplier
    def enumMessageResolver = Spy EnumMessageResolver

    def "should resolve message"() {
        when:
        def message = enumMessageResolver.resolveMessageFor(enumValue).toBlocking().single()

        then:
        0 * enumMessageResolver.getDefaultNameGetter()
        0 * defaultNameGetter.get()
        1 * enumMessageResolver.getHttpRequest() >> httpRequest
        1 * httpRequest.getLocale() >> new Locale("pl")
        1 * enumMessageResolver.getMessageSource() >> messageSource
        1 * messageSource.getMessage(TEST_ENUM_VALUE.name(), null, new Locale("pl")) >> "TEST_MESSAGE"

        and:
        "TEST_MESSAGE" == message
    }

    def "should use default message name if enum is null"() {
        when:
        def message = enumMessageResolver.resolveMessageFor(null as TestEnum).toBlocking().single()

        then:
        1 * enumMessageResolver.getDefaultNameGetter() >> defaultNameGetter
        1 * defaultNameGetter.get() >> "DEFAULT_NAME"
        1 * enumMessageResolver.getHttpRequest() >> httpRequest
        1 * httpRequest.getLocale() >> new Locale("pl")
        1 * enumMessageResolver.getMessageSource() >> messageSource
        1 * messageSource.getMessage("DEFAULT_NAME", null, new Locale("pl")) >> "TEST_DEFAULT_MESSAGE"

        and:
        "TEST_DEFAULT_MESSAGE" == message
    }

    enum TestEnum {
        TEST_ENUM_VALUE
    }
}
