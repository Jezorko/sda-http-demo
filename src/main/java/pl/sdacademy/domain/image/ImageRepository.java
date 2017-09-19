package pl.sdacademy.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.domain.user.User;

import java.util.List;
import java.util.Optional;

@Repository
interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

    @Query("select i from Image i join i.user u where i.id = ?2 and i.user = ?1")
    Image findAccessibleByUserById(User user, Long id);

    @Query("select i from Image i join i.imageTags t join i.user u where t.name in ?1 and (i.isPublic = true or i.user = ?2)")
    Page<Image> findAllAccessibleByUserByPageAndTags(List<String> tags, User user, Pageable pageable);
}
