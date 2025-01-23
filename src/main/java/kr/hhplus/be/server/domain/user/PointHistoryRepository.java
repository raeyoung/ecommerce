package kr.hhplus.be.server.domain.user;

import org.springframework.stereotype.Component;

@Component
public interface PointHistoryRepository {

    PointHistory findByUserId(Long userId);

    PointHistory save(PointHistory pointHistory);
}
