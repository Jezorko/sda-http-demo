package pl.sdacademy.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

    User getByUsernameOrEmail(String username, String email);
}
