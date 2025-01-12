package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.PointHistory;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository {
    User save(User user);
    void save(PointHistory pointHistory);
    Optional<User> findById(Long id);
}
