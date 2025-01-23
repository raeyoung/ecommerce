package kr.hhplus.be.server.infra.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserIdHolder {

    private final ThreadLocal<String> userId = new ThreadLocal<>();

    public void setUserId(String id) {
        userId.set(id);
    }

    public String getUserId() {
        return userId.get();
    }

    public void clear() {
        userId.remove();
    }
}
