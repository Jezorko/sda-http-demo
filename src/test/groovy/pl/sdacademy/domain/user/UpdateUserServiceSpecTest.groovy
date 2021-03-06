package pl.sdacademy.domain.user

import pl.sdacademy.domain.authorization.AuthorizationFacade
import pl.sdacademy.domain.shared.DigestUtil
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception
import pl.sdacademy.domain.user.dto.request.UpdateUserRequest
import spock.lang.Specification
import spock.lang.Unroll

import static rx.Observable.just

class UpdateUserServiceSpecTest extends Specification {

    def userRepository = Mock UserRepository
    def authorizationFacade = Mock AuthorizationFacade
    def digestUtil = Mock DigestUtil
    def service = new UpdateUserService(userRepository, authorizationFacade, digestUtil)

    def "should update the user details if user is logged in and no other user is present for updated information"() {
        given:
        def request = Mock UpdateUserRequest
        def user = Mock User

        when:
        service.updateUser request

        then:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * request.getUsername() >> "username"
        1 * userRepository.countUsersByUsername("username") >> 0L
        1 * request.getEmail() >> "email"
        1 * userRepository.countUsersByEmail("email") >> 0L
        1 * request.getPassword() >> "password"

        and:
        1 * digestUtil.generateHashFrom("password") >> "passwordHash"

        and:
        1 * user.setUsername("username")
        1 * user.setEmail("email")
        1 * user.setPasswordHash("passwordHash")

        and:
        1 * userRepository.save(user) >> user
        1 * user.getId() >> 1L
    }

    @Unroll
    "should throw if username exists = #usernameExists and email exists = #emailExists"() {
        given:
        def request = Mock UpdateUserRequest
        def user = Mock User

        when:
        service.updateUser request

        then:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * request.getUsername() >> "username"
        1 * userRepository.countUsersByUsername("username") >> (usernameExists ? 1L : 0L)
        (usernameExists ? 0 : 1) * request.getEmail() >> "email"
        (usernameExists ? 0 : 1) * userRepository.countUsersByEmail("email") >> (emailExists ? 1L : 0L)

        then:
        thrown BadRequest400Exception

        where:
        usernameExists | emailExists
        true           | false
        false          | true
        true           | true
    }

    @Unroll
    "should update only the #detail of user if user is logged in and a partial request was sent"() {
        given:
        def request = Mock UpdateUserRequest
        def user = Mock User

        when:
        service.updateUser request

        then:
        1 * authorizationFacade.getByToken() >> just(user)

        and:
        1 * request."$detailGetter"() >> "detail"
        request._ >> null

        and:
        userRepository.countUsersByUsername(_ as String) >> 0L
        userRepository.countUsersByEmail(_ as String) >> 0L
        digestUtil.generateHashFrom("detail") >> "detail"

        and:
        1 * user."$detailSetter"("detail")
        0 * user._

        and:
        1 * userRepository.save(user) >> user
        1 * user.getId() >> 1L

        where:
        detail     || detailGetter  | detailSetter
        "username" || "getUsername" | "setUsername"
        "email"    || "getEmail"    | "setEmail"
        "password" || "getPassword" | "setPasswordHash"
    }
}
