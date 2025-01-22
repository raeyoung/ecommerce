package kr.hhplus.be.server.config.aop;

import kr.hhplus.be.server.global.annotation.RedissonLock;
import kr.hhplus.be.server.global.exception.ExceptionMessage;
import kr.hhplus.be.server.global.util.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAop {
    private final RedissonClient redissonClient;

    @Around("@annotation(kr.hhplus.be.server.global.annotation.RedissonLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RedissonLock redissonLock = methodSignature.getMethod().getAnnotation(RedissonLock.class);

        String key = (String) CustomSpringELParser.getDynamicValue(methodSignature.getParameterNames(), joinPoint.getArgs(), redissonLock.key());

        RLock lock = redissonClient.getLock("LOCK:" + key);

        try {
            boolean available = lock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());

            if (!available) {
                throw new IllegalStateException(ExceptionMessage.REDIS_LOCK_ACQUIRE_FAILED.getMessage());
            }

            log.info("락 획득(KEY: {})", key);
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            throw new IllegalStateException(ExceptionMessage.REDIS_LOCK_ACQUIRE_FAILED.getMessage());
        } finally {
            lock.unlock();
            log.info("락 해제(KEY: {})", key);
        }
    }
}
