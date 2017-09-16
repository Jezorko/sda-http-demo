package pl.sdacademy.domain.shared

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import spock.lang.Specification
import spock.lang.Unroll

import static org.apache.commons.lang3.StringUtils.isNotBlank

class ApiStatusSpecTest extends Specification {
    @Unroll
    "should check all messages in #messagesPath to match ApiStatus values"() {
        given:
        def resource = new ClassPathResource(messagesPath)

        when:
        def properties = PropertiesLoaderUtils.loadProperties(resource)

        then:
        ApiStatus.values().each {
            def toCheckName = it.name()
            def message = properties.get(toCheckName)
            assert properties.containsKey(toCheckName)
            assert isNotBlank(message as CharSequence)
        }

        where:
        messagesPath << [
                "/internationalization/statuses/descriptions.properties",
                "/internationalization/statuses/descriptions_pl.properties"
        ]
    }
}
