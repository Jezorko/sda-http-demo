package pl.sdacademy.domain.user

import pl.sdacademy.domain.shared.DigestUtil
import pl.sdacademy.domain.shared.exceptions.InternalServer500Exception
import pl.sdacademy.domain.user.dto.request.CreateUserRequest
import spock.lang.Specification

class CreateUserServiceSpecTest extends Specification {

    def repository = Mock UserRepository
    def digestUtil = Mock DigestUtil
    def service = new CreateUserService(repository, digestUtil)

    def "should create user and save it to the database if the username is absent"() {
        given:
        def request = Mock CreateUserRequest

        when:
        service.createUser request

        then: "username is taken from the request"
        1 * request.getUsername() >> "user"

        and: "database is queried by username"
        1 * repository.getByUsername("user") >> null

        and: "a new user object is created and it's password hashed"
        1 * request.getUsername()
        1 * request.getEmail()
        1 * request.getPassword() >> "password"
        1 * digestUtil.generateHashFrom("password") >> "hash"

        and: "a new user is saved to the database"
        1 * repository.save(_ as User)
    }

    def "should not create a new user if the username is present in the repository"() {
        given:
        def request = Mock CreateUserRequest
        def user = Mock User

        when:
        service.createUser request

        then: "username is taken from the request"
        1 * request.getUsername() >> "user"

        and: "database is queried by username"
        1 * repository.getByUsername("user") >> user

        and: "a new user object is not created so the password needs not to be hashed"
        0 * digestUtil._

        and: "the same user is saved to the database"
        1 * repository.save(user)
    }

    def "should throw internal server error if request is null"() {
        given:
        def request = null

        when:
        service.createUser request

        then: "no action can be performed"
        0 * _._

        and: "exception is thrown"
        thrown InternalServer500Exception
    }
}
