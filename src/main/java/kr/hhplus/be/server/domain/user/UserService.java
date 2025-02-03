package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.global.exception.ExceptionMessage;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import kr.hhplus.be.server.interfaces.user.PointResponse;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;

    public UserService(PointRepository pointRepository, PointHistoryRepository pointHistoryRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.userRepository = userRepository;
    }


    /**
     * 포인트 조회
     * @param userId
     * @return
     */
    public Point getPoint(long userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.USER_NOT_FOUND.getMessage()));
    }

    /**
     * 포인트 충전
     * @param request
     * @return
     */
    @Transactional
    public PointResponse chargePoint(PointRequest request) {
        // 사용자 검증
        Point userPoint = pointRepository.findByUserIdWithLock(request.userId())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.USER_NOT_FOUND.getMessage()));;

        // UserPoint가 존재하지 않으면 새로 생성
        if (userPoint == null) {
            userPoint = new Point(request.userId(), 0L); // 초기 잔액은 0
            userPoint.setCreatedAt(LocalDateTime.now()); // createdAt 설정
            userPoint.setUpdatedAt(LocalDateTime.now()); // updatedAt 설정
        }

        // 포인트 충전
        userPoint.chargePoint(request.amount());

        // UserPoint 저장 (현재 잔액을 갱신)
        pointRepository.save(userPoint);

        // UserPointHistory 기록 (충전 내역)
        PointHistory userPointHistory = PointHistory.createCharge(request.userId(), request.amount());
        userPointHistory.setCurrentAmount(userPoint.getCurrentAmount());

        pointHistoryRepository.save(userPointHistory);

        return PointResponse.from(userPoint);
    }

    /**
     * 포인트 사용
     * @param request
     * @return
     */
    @Transactional
    public PointResponse usePoint(PointRequest request) {
        // 사용자 검증
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.USER_NOT_FOUND.getMessage()));

        Point userPoint = pointRepository.findByUserIdWithLock(user.getId())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.USER_NOT_FOUND.getMessage()));;

        // UserPoint가 존재하지 않으면 새로 생성
        if (userPoint == null) {
            userPoint = new Point(user.getId(), 0L); // 초기 잔액은 0
            userPoint.setCreatedAt(LocalDateTime.now()); // createdAt 설정
            userPoint.setUpdatedAt(LocalDateTime.now()); // updatedAt 설정
        }

        // 포인트 충전
        userPoint.usePoint(request.amount());

        // UserPoint 저장 (현재 잔액을 갱신)
        pointRepository.save(userPoint);

        // UserPointHistory 기록 (충전 내역)
        PointHistory userPointHistory = PointHistory.createUse(user.getId(), request.amount());
        userPointHistory.setCurrentAmount(userPoint.getCurrentAmount());

        pointHistoryRepository.save(userPointHistory);

        return PointResponse.from(userPoint);
    }
}
