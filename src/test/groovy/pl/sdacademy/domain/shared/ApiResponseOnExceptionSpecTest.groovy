package pl.sdacademy.domain.shared

import org.springframework.http.HttpStatus
import spock.lang.Specification

import static pl.sdacademy.domain.shared.ApiStatus.INTERNAL_SERVER_ERROR

class ApiResponseOnExceptionSpecTest extends Specification {
    def "should return itself wrapped into response entity"() {
        given:
        def responseOnException = new ApiResponseOnException(INTERNAL_SERVER_ERROR, "")

        when:
        def wrappedResponse = responseOnException.wrap(HttpStatus.INTERNAL_SERVER_ERROR)

        then:
        responseOnException == wrappedResponse.body
        HttpStatus.INTERNAL_SERVER_ERROR == wrappedResponse.statusCode
    }
}
