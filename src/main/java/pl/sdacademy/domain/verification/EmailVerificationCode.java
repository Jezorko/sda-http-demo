package pl.sdacademy.domain.verification;

import lombok.*;
import pl.sdacademy.domain.user.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PROTECTED;

@Table
@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class EmailVerificationCode {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private Boolean verified;
}
