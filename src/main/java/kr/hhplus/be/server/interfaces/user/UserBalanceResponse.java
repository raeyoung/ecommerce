package kr.hhplus.be.server.interfaces.user;

public record UserBalanceResponse (
    long userId,
    long balance
){}