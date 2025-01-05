package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.global.exception.NotFoundException;
import kr.hhplus.be.server.infra.user.UserPointHistoryRepository;
import kr.hhplus.be.server.infra.user.UserPointRepository;
import kr.hhplus.be.server.infra.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.UserPointRequest;
import kr.hhplus.be.server.interfaces.user.UserPointResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserPointRepository userPointRepository;
    private final UserPointHistoryRepository userPointHistoryRepository;
    private final UserRepository userRepository;

    public UserService(UserPointRepository userPointRepository, UserPointHistoryRepository userPointHistoryRepository, UserRepository userRepository) {
        this.userPointRepository = userPointRepository;
        this.userPointHistoryRepository = userPointHistoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * 잔액 조회
     * @param userId
     * @return
     */
    public UserPointResponse point(long userId) {
        // 사용자 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        UserPoint userPoint = userPointRepository.findByUserId(userId);
        // UserPoint가 없으면 예외 처리 (혹은 기본 값 처리)
        if (userPoint == null) {
            throw new NotFoundException("해당 사용자의 포인트 정보를 찾을 수 없습니다.");
        }
        return UserPointResponse.of(userPoint, user);
    }

    /**
     * 잔액 충전
     * @param request
     * @return
     */
    public UserPointResponse chargePoint(UserPointRequest request) {
        // 사용자 검증
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        UserPoint userPoint = userPointRepository.findByUserId(user.getId());

        // UserPoint가 존재하지 않으면 새로 생성
        if (userPoint == null) {
            userPoint = new UserPoint(user.getId(), 0L); // 초기 잔액은 0
            userPoint.setCreatedAt(LocalDateTime.now()); // createdAt 설정
            userPoint.setUpdatedAt(LocalDateTime.now()); // updatedAt 설정
        }

        // 포인트 충전
        userPoint.chargePoint(request.amount());

        // UserPoint 저장 (현재 잔액을 갱신)
        userPointRepository.save(userPoint);

        // UserPointHistory 기록 (충전 내역)
        UserPointHistory userPointHistory = UserPointHistory.createCharge(user.getId(), request.amount());
        userPointHistory.setCurrentAmount(userPoint.getCurrentAmount());

        userPointHistoryRepository.save(userPointHistory);

        return UserPointResponse.from(userPoint);
    }
}
