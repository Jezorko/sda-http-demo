package pl.sdacademy.domain.shared

import spock.lang.Specification
import spock.lang.Unroll

import static pl.sdacademy.domain.shared.DataMasker.mask
import static pl.sdacademy.domain.shared.DataMasker.shortHash

class DataMaskerSpecTest extends Specification {
    @Unroll
    '"#toMask" should be masked to "#expected"'() {
        given:
        def actual

        when:
        actual = mask toMask

        then:
        actual == expected

        where:
        toMask | expected
        null   | "null"
        ""     | ""
        " "    | "*"
        "1234" | "****"
    }

    @Unroll
    '"#toMask" should be hashed to "#expect"'() {
        given:
        def actual

        when:
        actual = shortHash toMask

        then:
        expect actual

        where:
        toMask  | expect
        null    | { it == "null" }
        ""      | { it.length() == 5 && it.matches("[0-9a-f]{5}") }
        " "     | { it.length() == 5 && it.matches("[0-9a-f]{5}") }
        "1234"  | { it.length() == 5 && it.matches("[0-9a-f]{5}") }
        "12/34" | { it.length() == 5 && it.matches("[0-9a-f]{5}") }
    }
}