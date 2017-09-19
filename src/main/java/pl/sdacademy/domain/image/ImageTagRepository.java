package pl.sdacademy.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ImageTagRepository extends JpaRepository<ImageTag, Long> {
    Optional<ImageTag> findByName(String name);

    List<ImageTag> findAllByImages(Image image);
}
