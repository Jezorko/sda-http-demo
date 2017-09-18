package pl.sdacademy.domain.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sdacademy.domain.user.User;

@Repository
interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long> {

    void deleteByUser(User user);

    @Query("select v from EmailVerificationCode v, User u where v.user = u and u = ?1 and v.email = u.email")
    EmailVerificationCode getByUserWhereEmailsAreMatching(User user);
}
