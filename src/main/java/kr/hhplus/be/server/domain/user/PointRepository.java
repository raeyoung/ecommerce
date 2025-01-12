package kr.hhplus.be.server.domain.user;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface PointRepository {

    Point findByUserIdWithLock(@Param("userId") Long userId);

    Point findByUserId(Long userId);

    Point save(Point userPoint);
}
