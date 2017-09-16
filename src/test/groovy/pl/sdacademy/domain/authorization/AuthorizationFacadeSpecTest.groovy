package pl.sdacademy.domain.authorization

import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception
import pl.sdacademy.domain.user.User
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

import static pl.sdacademy.domain.shared.HeaderNames.AUTHORIZATION_TOKEN

class AuthorizationFacadeSpecTest extends Specification {

    def servletRequest = Mock(HttpServletRequest)
    def tokenRepository = Mock(AuthorizationTokenRepository)
    def authorizationFacade = new AuthorizationFacade(servletRequest, tokenRepository)

    def "should query user by token value"() {
        given:
        def token = Mock AuthorizationToken
        def user = Mock User

        when:
        def result = authorizationFacade.getByToken().toBlocking().single()

        then:
        1 * servletRequest.getHeader(AUTHORIZATION_TOKEN) >> "testToken"

        and:
        1 * tokenRepository.findByValue("testToken") >> token
        1 * token.getUser() >> user

        and:
        user == result
    }

    def "should throw exception if token is not present in the request"() {
        when:
        authorizationFacade.getByToken().toBlocking().single()

        then:
        1 * servletRequest.getHeader(AUTHORIZATION_TOKEN) >> null

        and:
        1 * tokenRepository.findByValue(null) >> null

        and:
        thrown Forbidden403Exception
    }

    def "should throw exception if token is not present in the repository"() {
        when:
        authorizationFacade.getByToken().toBlocking().single()

        then:
        1 * servletRequest.getHeader(AUTHORIZATION_TOKEN) >> "testToken"

        and:
        1 * tokenRepository.findByValue("testToken") >> null

        and:
        thrown Forbidden403Exception
    }
}
