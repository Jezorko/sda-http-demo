package pl.sdacademy.domain.task

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Arrays.asList
import static org.apache.commons.lang3.StringUtils.isNotBlank
import static pl.sdacademy.domain.task.Task.TASK_1
import static pl.sdacademy.domain.task.Task.TASK_2

class TaskSpecTest extends Specification {
    @Unroll
    "should return id=#expectedId for the task #task"() {
        when:
        def actualId = task.getId()

        then:
        expectedId == actualId

        where:
        task   | expectedId
        TASK_1 | 1L
        TASK_2 | 2L
    }

    @Unroll
    "task #task should return representation that contains it's own fields"() {
        when:
        def representation = task.asRepresentation()

        then:
        representation.name == task.name()
        representation.token == task.token
        representation.submitToken == task.submitToken

        where:
        task << asList(Task.values())
    }

    @Unroll
    "should check all messages in #messagesPath to match Task values"() {
        given:
        def resource = new ClassPathResource(messagesPath)

        when:
        def properties = PropertiesLoaderUtils.loadProperties(resource)

        then:
        Task.values().each {
            def toCheckName = it.name()
            def message = properties.get(toCheckName)
            assert properties.containsKey(toCheckName)
            assert isNotBlank(message as CharSequence)
        }

        where:
        messagesPath << [
                "/internationalization/tasks/descriptions.properties",
                "/internationalization/tasks/descriptions_pl.properties"
        ]
    }
}
