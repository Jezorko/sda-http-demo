package pl.sdacademy.domain.shared

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import spock.lang.Specification
import spock.lang.Unroll

import static org.apache.commons.lang3.StringUtils.isNotBlank

class EmailSubjectSpecTest extends Specification {
    @Unroll
    "should check all messages in #messagesPath to match EmailSubject values"() {
        given:
        def resource = new ClassPathResource(messagesPath)

        when:
        def properties = PropertiesLoaderUtils.loadProperties(resource)

        then:
        EmailSubject.values().each {
            def toCheckName = it.name()
            def message = properties.get(toCheckName)
            assert properties.containsKey(toCheckName)
            assert isNotBlank(message as CharSequence)
        }

        where:
        messagesPath << [
                "/internationalization/emails/subjects.properties",
                "/internationalization/emails/subjects_pl.properties"
        ]
    }
}
