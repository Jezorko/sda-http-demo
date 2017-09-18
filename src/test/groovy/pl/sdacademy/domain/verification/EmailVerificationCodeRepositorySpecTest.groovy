package pl.sdacademy.domain.verification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import pl.sdacademy.DatabaseSpecification
import pl.sdacademy.domain.user.User
import pl.sdacademy.domain.user.UserRepository

class EmailVerificationCodeRepositorySpecTest extends DatabaseSpecification {

    @Autowired
    UserRepository userRepository

    @Autowired
    EmailVerificationCodeRepository verificationCodeRepository


    @Sql(scripts = ["/sql/initializers/users.sql", "/sql/initializers/verification_codes.sql"])
    def "should find the verification if the user and user's email match the verification"() {
        given:
        def email = "alice@sdacademy.pl"
        User user = userRepository.getByUsernameOrEmail(_ as String, email)

        when:
        def verification = verificationCodeRepository.getByUserWhereEmailsAreMatching(user)

        then:
        verification.email == email
    }


    @Sql(scripts = ["/sql/initializers/users.sql", "/sql/initializers/verification_codes.sql"])
    def "should not find if user has a verification for a different email than their current one"() {
        given:
        def username = "bob"
        User user = userRepository.getByUsername(username)

        when:
        def verification = verificationCodeRepository.getByUserWhereEmailsAreMatching(user)

        then:
        verification == null
    }

}
