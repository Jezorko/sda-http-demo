package pl.sdacademy.domain.task

import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception
import pl.sdacademy.domain.task.dto.request.SubmitTaskRequest
import pl.sdacademy.infrastructure.properties.ApplicationProperties
import rx.Observable
import spock.lang.Specification

import static java.lang.Boolean.FALSE
import static java.lang.Boolean.TRUE
import static pl.sdacademy.domain.task.Task.TASK_1
import static pl.sdacademy.domain.task.Task.TASK_2
import static rx.Observable.just

class TaskServiceSpecTest extends Specification {

    def taskDescriptionResolver = Mock TaskDescriptionResolver
    def applicationProperties = Mock ApplicationProperties
    def taskService = new TaskService(taskDescriptionResolver, applicationProperties)

    def "should return all tasks as list of their representations if debug mode is enabled"() {
        given:
        def tasks = Task.values()

        when:
        def result = taskService.getAllTasks()

        then:
        applicationProperties.getDebugEnabled() >> TRUE

        and:
        0 * taskDescriptionResolver._

        and:
        tasks.length == result.size()
        result.forEach({ it -> assert it == Task.valueOf(it.name).asRepresentation() })
    }

    def "should throw forbidden exception if debug mode is disabled"() {
        when:
        taskService.getAllTasks()

        then:
        applicationProperties.getDebugEnabled() >> FALSE

        and:
        0 * _._

        and:
        thrown Forbidden403Exception
    }

    def "should return the task data if the task token is valid"() {
        given:
        def taskId = 1L
        def taskToken = TASK_1.token

        when:
        def result = taskService.getTask(taskId, taskToken).toBlocking().single()

        then:
        1 * taskDescriptionResolver.resolveMessageFor(_ as Observable) >> just("TEST_DESCRIPTION")

        and:
        1L == result.id
        "TEST_DESCRIPTION" == result.description
    }

    def "should throw 404 not found exception if the task id is not valid"() {
        given:
        def taskId = -1L

        when:
        taskService.getTask(taskId, _ as String).doOnError({ throw it }).toBlocking().subscribe()

        then:
        1 * taskDescriptionResolver.resolveMessageFor(_ as Observable) >> { Observable o -> o.toBlocking().single() }

        and:
        thrown NotFound404Exception
    }

    def "should throw 400 bad request exception if the task token is not valid"() {
        given:
        def taskId = 1L
        def taskToken = ""

        when:
        taskService.getTask(taskId, taskToken).doOnError({ throw it }).toBlocking().subscribe()

        then:
        1 * taskDescriptionResolver.resolveMessageFor(_ as Observable) >> { Observable o -> o.toBlocking().single() }

        and:
        thrown BadRequest400Exception
    }

    def "should return next task data if current task token and submission token is valid"() {
        given:
        def taskId = 1L
        def taskToken = TASK_1.token
        def submitToken = TASK_1.submitToken

        when:
        def result = taskService.submitTask(new SubmitTaskRequest(id: taskId, token: taskToken, submitToken: submitToken)).toBlocking().single()

        then:
        2L == result.nextTaskId
        TASK_2.token == result.nextTaskToken
    }

    def "should throw 400 bad request exception if current task token is invalid"() {
        given:
        def taskId = 1L
        def taskToken = ""
        def submitToken = TASK_2.token

        when:
        taskService.submitTask(new SubmitTaskRequest(id: taskId, token: taskToken, submitToken: submitToken)).toBlocking().single()

        then:
        thrown BadRequest400Exception
    }

    def "should throw 400 bad request exception if submission token is invalid"() {
        given:
        def taskId = 1L
        def taskToken = TASK_1.token
        def submitToken = ""

        when:
        taskService.submitTask(new SubmitTaskRequest(id: taskId, token: taskToken, submitToken: submitToken)).toBlocking().single()

        then:
        thrown BadRequest400Exception
    }

}
