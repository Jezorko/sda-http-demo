package pl.sdacademy.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

    @Query("select u from User u where (u.username like %?1) or (u.email like %?2)")
    User getByUsernameOrEmail(String username, String email);

    Long countUsersByUsername(String username);

    Long countUsersByEmail(String email);
}
