package pl.sdacademy.domain.image;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sdacademy.domain.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static lombok.AccessLevel.PROTECTED;

@Table
@Entity
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Image {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Boolean isPublic;

    @Temporal(TIMESTAMP)
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdDate;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "images")
    private List<ImageTag> imageTags;
}
