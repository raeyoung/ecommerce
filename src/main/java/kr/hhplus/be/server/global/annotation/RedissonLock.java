package kr.hhplus.be.server.global.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {

    String key();                     // Lock의 이름 (고유값)

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    long waitTime() default 5000L;      // Lock획득을 시도하는 최대 시간 (ms)

    long leaseTime() default 2000L;     // 락을 획득한 후, 점유하는 최대 시간 (ms)
}