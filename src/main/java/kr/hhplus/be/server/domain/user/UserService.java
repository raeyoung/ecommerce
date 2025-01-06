package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.global.exception.NotFoundException;
import kr.hhplus.be.server.infra.user.PointHistoryRepository;
import kr.hhplus.be.server.infra.user.PointRepository;
import kr.hhplus.be.server.infra.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import kr.hhplus.be.server.interfaces.user.PointResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final PointRepository userPointRepository;
    private final PointHistoryRepository userPointHistoryRepository;
    private final UserRepository userRepository;

    public UserService(PointRepository userPointRepository, PointHistoryRepository userPointHistoryRepository, UserRepository userRepository) {
        this.userPointRepository = userPointRepository;
        this.userPointHistoryRepository = userPointHistoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * 잔액 조회
     * @param userId
     * @return
     */
    public PointResponse point(long userId) {
        // 사용자 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Point userPoint = userPointRepository.findByUserId(userId);
        // UserPoint가 없으면 예외 처리 (혹은 기본 값 처리)
        if (userPoint == null) {
            throw new NotFoundException("해당 사용자의 포인트 정보를 찾을 수 없습니다.");
        }
        return PointResponse.of(userPoint, user);
    }

    /**
     * 잔액 충전
     * @param request
     * @return
     */
    @Transactional
    public PointResponse chargePoint(PointRequest request) {
        // 사용자 검증
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Point userPoint = userPointRepository.findByUserId(user.getId());

        // UserPoint가 존재하지 않으면 새로 생성
        if (userPoint == null) {
            userPoint = new Point(user.getId(), 0L); // 초기 잔액은 0
            userPoint.setCreatedAt(LocalDateTime.now()); // createdAt 설정
            userPoint.setUpdatedAt(LocalDateTime.now()); // updatedAt 설정
        }

        // 포인트 충전
        userPoint.chargePoint(request.amount());

        // UserPoint 저장 (현재 잔액을 갱신)
        userPointRepository.save(userPoint);

        // UserPointHistory 기록 (충전 내역)
        PointHistory userPointHistory = PointHistory.createCharge(user.getId(), request.amount());
        userPointHistory.setCurrentAmount(userPoint.getCurrentAmount());

        userPointHistoryRepository.save(userPointHistory);

        return PointResponse.from(userPoint);
    }

    @Transactional
    public PointResponse usePoint(PointRequest request) {
        // 사용자 검증
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Point userPoint = userPointRepository.findByUserId(user.getId());

        // UserPoint가 존재하지 않으면 새로 생성
        if (userPoint == null) {
            userPoint = new Point(user.getId(), 0L); // 초기 잔액은 0
            userPoint.setCreatedAt(LocalDateTime.now()); // createdAt 설정
            userPoint.setUpdatedAt(LocalDateTime.now()); // updatedAt 설정
        }

        // 포인트 충전
        userPoint.usePoint(request.amount());

        // UserPoint 저장 (현재 잔액을 갱신)
        userPointRepository.save(userPoint);

        // UserPointHistory 기록 (충전 내역)
        PointHistory userPointHistory = PointHistory.createCharge(user.getId(), request.amount());
        userPointHistory.setCurrentAmount(userPoint.getCurrentAmount());

        userPointHistoryRepository.save(userPointHistory);

        return PointResponse.from(userPoint);
    }
}
