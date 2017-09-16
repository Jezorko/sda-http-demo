package pl.sdacademy.domain.user;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PROTECTED;

@Table
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString(exclude = "passwordHash")
@NoArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String email;
}
