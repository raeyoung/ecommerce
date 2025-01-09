package kr.hhplus.be.server.infra.user;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.user.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 설정
    @Query("SELECT p FROM Point p WHERE p.userId = :userId")
    Point findByUserIdWithLock(@Param("userId") Long userId);

    Point findByUserId(Long userId);

    Point save(Point userPoint);
}
