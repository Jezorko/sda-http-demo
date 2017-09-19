package pl.sdacademy.domain.image

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.jdbc.Sql
import pl.sdacademy.DatabaseSpecification
import pl.sdacademy.domain.user.UserRepository
import spock.lang.Unroll

import static java.util.Objects.isNull
import static java.util.Objects.nonNull

class ImageRepositorySpecTest extends DatabaseSpecification {

    @Autowired
    private UserRepository userRepository

    @Autowired
    private ImageRepository imageRepository

    @Unroll
    @Sql(scripts = [
            "/sql/initializers/users.sql",
            "/sql/initializers/images.sql",
            "/sql/initializers/image_tags.sql",
            "/sql/initializers/image_tag_images.sql"
    ])
    "should find an image accessible by the user"() {
        given:
        def user = userRepository.getByUsername("alice")

        when:
        def image = imageRepository.findAccessibleByUserById(user, 1L)

        then:
        nonNull image
        image?.user == user
    }

    @Unroll
    @Sql(scripts = [
            "/sql/initializers/users.sql",
            "/sql/initializers/images.sql",
            "/sql/initializers/image_tags.sql",
            "/sql/initializers/image_tag_images.sql"
    ])
    "should return null if user is not an owner of the image"() {
        given:
        def user = userRepository.getByUsername("bob")

        when:
        def image = imageRepository.findAccessibleByUserById(user, 1L)

        then:
        isNull image
    }

    @Unroll
    @Sql(scripts = [
            "/sql/initializers/users.sql",
            "/sql/initializers/images.sql",
            "/sql/initializers/image_tags.sql",
            "/sql/initializers/image_tag_images.sql"
    ])
    "should find public image by a tag for user #username that owns image=#isOwner"() {
        given:
        def user = userRepository.getByUsername(username)

        when:
        def images = imageRepository.findAllAccessibleByUserByPageAndTags(["valid tag"], user, new PageRequest(0, 1))

        then:
        images?.size == new Integer(1)
        isOwner == (images.getContent()[0].user == user)

        where:
        username | isOwner
        "alice"  | true
        "bob"    | false
    }

    @Unroll
    @Sql(scripts = [
            "/sql/initializers/users.sql",
            "/sql/initializers/images.sql",
            "/sql/initializers/image_tags.sql",
            "/sql/initializers/image_tag_images.sql"
    ])
    "should not find images if the tag is not present"() {
        given:
        def user = userRepository.getByUsername("alice")

        when:
        def images = imageRepository.findAllAccessibleByUserByPageAndTags(["invalid tag"], user, new PageRequest(0, 1))

        then:
        images.content.empty
    }

    @Unroll
    @Sql(scripts = [
            "/sql/initializers/users.sql",
            "/sql/initializers/images.sql",
            "/sql/initializers/image_tags.sql",
            "/sql/initializers/image_tag_images.sql"
    ])
    "should find private image by a tag for user #username only if is owner=#isOwner"() {
        given:
        def user = userRepository.getByUsername(username)

        when:
        def images = imageRepository.findAllAccessibleByUserByPageAndTags(["private tag"], user, new PageRequest(0, 1))

        then:
        isOwner == !images.content.empty
        isOwner == (images.getContent()[0]?.user == user)

        where:
        username | isOwner
        "alice"  | true
        "bob"    | false
    }
}
