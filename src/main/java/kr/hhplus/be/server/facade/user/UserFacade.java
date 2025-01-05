package kr.hhplus.be.server.facade.user;

import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.user.UserPointRequest;
import kr.hhplus.be.server.interfaces.user.UserPointResponse;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    public UserPointResponse point(long userId) {
        return userService.point(userId);
    }

    public UserPointResponse chargePoint(UserPointRequest request) {
        return userService.chargePoint(request);
    }
}
