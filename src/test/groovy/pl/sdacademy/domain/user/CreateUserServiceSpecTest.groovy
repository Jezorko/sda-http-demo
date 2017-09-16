package pl.sdacademy.domain.user

import pl.sdacademy.domain.shared.DigestUtil
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception
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
        1 * request.getEmail() >> "test@sdacademy.pl"

        and: "database is queried by username and email"
        1 * repository.getByUsernameOrEmail("user", "test@sdacademy.pl") >> null

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
        1 * request.getEmail() >> "test@sdacademy.pl"

        and: "database is queried by username and email"
        1 * repository.getByUsernameOrEmail("user", "test@sdacademy.pl") >> user

        and: "a new user object is not created and no other action is performed"
        0 * _._

        and: "exception is thrown"
        thrown BadRequest400Exception
    }

    def "should throw internal server error if request is null"() {
        given:
        def request = null

        when:
        service.createUser request

        then: "no action is be performed"
        0 * _._

        and: "exception is thrown"
        thrown InternalServer500Exception
    }
}
