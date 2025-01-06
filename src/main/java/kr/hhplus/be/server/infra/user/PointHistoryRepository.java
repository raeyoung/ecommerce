package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>  {

    PointHistory findByUserId(Long userId);
}
