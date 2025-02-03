package kr.hhplus.be.server.domain.user;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface PointRepository {

    Optional<Point> findById(Long id);

    Optional<Point> findByUserIdWithLock(@Param("userId") Long userId);

    Optional<Point> findByUserId(Long userId);

    Point save(Point userPoint);
}
