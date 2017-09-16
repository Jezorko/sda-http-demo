package pl.sdacademy.domain.authorization

import pl.sdacademy.domain.authorization.dto.request.LoginUserRequest
import pl.sdacademy.domain.authorization.dto.response.LoginUserResponse
import pl.sdacademy.domain.user.User
import pl.sdacademy.domain.user.UserFacade
import spock.lang.Specification

import java.time.LocalDateTime

import static java.util.Objects.nonNull
import static rx.Observable.just

class AuthorizationServiceSpecTest extends Specification {

    def userFacade = Mock UserFacade
    def tokenRepository = Mock AuthorizationTokenRepository
    def authorizationService = new AuthorizationService(userFacade, tokenRepository)

    def "should generate a token if no tokens are present for the user"() {
        given:
        def request = Mock LoginUserRequest
        def user = Mock User

        when:
        LoginUserResponse result = authorizationService.login(request).toBlocking().single()

        then:
        1 * request.getUsername() >> ""
        1 * request.getPassword() >> ""
        1 * userFacade.getUserValidateCredentials(_ as String, _ as String) >> just(user)

        and:
        1 * tokenRepository.findByUser(user) >> null

        and:
        1 * tokenRepository.save(_ as AuthorizationToken) >> { AuthorizationToken token -> token }

        and:
        nonNull result.token
        nonNull result.validUntil
    }

    def "should generate a new token and delete the old one if the token present for the user is expired"() {
        given:
        def request = Mock LoginUserRequest
        def user = Mock User
        def currentToken = Mock AuthorizationToken

        when:
        LoginUserResponse result = authorizationService.login(request).toBlocking().single()

        then:
        1 * request.getUsername() >> ""
        1 * request.getPassword() >> ""
        1 * userFacade.getUserValidateCredentials(_ as String, _ as String) >> just(user)

        and:
        1 * tokenRepository.findByUser(user) >> currentToken
        1 * currentToken.getExpirationDate() >> LocalDateTime.now().minusDays(5L)

        and:
        1 * tokenRepository.save(_ as AuthorizationToken) >> { AuthorizationToken token -> token }

        and:
        nonNull result.token
        nonNull result.validUntil
    }

    def "should not generate a new token and just return the old one if the token present for the user is not expired"() {
        given:
        def request = Mock LoginUserRequest
        def user = Mock User
        def currentToken = Mock AuthorizationToken

        when:
        LoginUserResponse result = authorizationService.login(request).toBlocking().single()

        then:
        1 * request.getUsername() >> ""
        1 * request.getPassword() >> ""
        1 * userFacade.getUserValidateCredentials(_ as String, _ as String) >> just(user)

        and:
        1 * tokenRepository.findByUser(user) >> currentToken
        1 * currentToken.getExpirationDate() >> LocalDateTime.now().plusDays(5L)
        1 * currentToken.getValue() >> "testToken"
        1 * currentToken.getExpirationDate() >> LocalDateTime.now().plusDays(5L)

        and:
        1 * tokenRepository.save(_ as AuthorizationToken) >> { AuthorizationToken token -> token }

        and:
        "testToken" == result.token
        nonNull result.validUntil
    }
}
