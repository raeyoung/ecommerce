package kr.hhplus.be.server.facade.user;

import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import kr.hhplus.be.server.interfaces.user.PointResponse;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    public PointResponse getPoint(long userId) {
        return PointResponse.of(userService.getPoint(userId));
    }

    public PointResponse chargePoint(PointRequest request) {
        return userService.chargePoint(request);
    }

    public PointResponse usePoint(PointRequest request) {
        return userService.usePoint(request);
    }
}
