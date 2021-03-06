package pl.sdacademy.domain.authorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.domain.user.User;

@Repository
interface AuthorizationTokenRepository extends JpaRepository<AuthorizationToken, Long> {
    AuthorizationToken findByUser(User user);

    AuthorizationToken findByValue(String value);
}
