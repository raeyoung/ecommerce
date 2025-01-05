package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Long> {

    UserPoint findByUserId(Long userId);

    UserPoint save(UserPoint userPoint);
}
