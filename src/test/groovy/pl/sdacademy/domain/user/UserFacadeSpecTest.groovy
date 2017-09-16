package pl.sdacademy.domain.user

import pl.sdacademy.domain.shared.DigestUtil
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception
import spock.lang.Specification
import spock.lang.Unroll

class UserFacadeSpecTest extends Specification {

    def userRepository = Mock UserRepository
    def digestUtil = Mock DigestUtil
    def userFacade = new UserFacade(userRepository, digestUtil)

    def "should return user if credentials are valid"() {
        given:
        def user = new User(passwordHash: "123")

        when:
        def result = userFacade.getUserValidateCredentials(_ as String, _ as String).toBlocking().single()

        then:
        1 * userRepository.getByUsername(_ as String) >> user

        and:
        1 * digestUtil.generateHashFrom(_ as String) >> "123"

        and:
        user == result
    }

    @Unroll
    "should throw #exception for #user and provided password #password"() {
        when:
        userFacade.getUserValidateCredentials(_ as String, _ as String).toBlocking().single()

        then:
        1 * userRepository.getByUsername(_ as String) >> user

        and:
        digestUtil.generateHashFrom(_ as String) >> password

        and:
        thrown exception

        where:
        user                                            | password || exception
        new User(username: "test", passwordHash: "123") | "456"    || BadRequest400Exception
        null as User                                    | "123"    || NotFound404Exception
    }
}
