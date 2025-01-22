package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Optional<Point> findById(Long id) {
        return pointJpaRepository.findById(id);
    }

    @Override
    public Point findByUserIdWithLock(Long userId) {
        return pointJpaRepository.findByUserIdWithLock(userId);
    }

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public Point save(Point userPoint) {
        return pointJpaRepository.save(userPoint);
    }
}
