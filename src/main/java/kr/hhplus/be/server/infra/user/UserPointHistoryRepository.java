package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.UserPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointHistoryRepository  extends JpaRepository<UserPointHistory, Long>  {

    UserPointHistory findByUserId(Long userId);
}
