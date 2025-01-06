package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    Point findByUserId(Long userId);

    Point save(Point userPoint);
}
