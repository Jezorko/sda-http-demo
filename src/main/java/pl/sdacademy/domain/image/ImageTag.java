package pl.sdacademy.domain.image;

import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PROTECTED;

@Table
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString(exclude = "images")
@NoArgsConstructor(access = PROTECTED)
public class ImageTag {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    private List<Image> images;
}
