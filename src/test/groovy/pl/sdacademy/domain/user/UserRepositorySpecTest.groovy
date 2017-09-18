package pl.sdacademy.domain.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import pl.sdacademy.DatabaseSpecification
import spock.lang.Unroll

class UserRepositorySpecTest extends DatabaseSpecification {

    @Autowired
    private UserRepository userRepository

    @Unroll
    @Sql(scripts = "/sql/initializers/users.sql")
    "should find user by username #username and email #email"() {
        when:
        def user = userRepository.getByUsernameOrEmail(username, email)

        then:
        user?.username == expectedUsername
        user?.email == expectedEmail

        where:
        username | email                || expectedUsername | expectedEmail
        "alice"  | "alice@sdacademy.pl" || "alice"          | "alice@sdacademy.pl"
        "alice"  | null                 || "alice"          | "alice@sdacademy.pl"
        null     | "alice@sdacademy.pl" || "alice"          | "alice@sdacademy.pl"
        "bob"    | "bob@sdacademy.pl"   || "bob"            | null
        "bob"    | null                 || "bob"            | null
    }

    @Unroll
    @Sql(scripts = "/sql/initializers/users.sql")
    "should not find the user if username is #username and email is #email"() {
        when:
        def user = userRepository.getByUsernameOrEmail(username, email)

        then:
        user == null

        where:
        username                      | email
        "not present in the database" | "not present in the database"
        "not present in the database" | null
        null                          | "not present in the database"
        null                          | null
    }

}
