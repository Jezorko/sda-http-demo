package pl.sdacademy.domain.authorization;

import lombok.*;
import pl.sdacademy.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PROTECTED;

@Table
@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class AuthorizationToken {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private LocalDateTime expirationDate;
}
