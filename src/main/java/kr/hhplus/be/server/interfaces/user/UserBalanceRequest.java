package kr.hhplus.be.server.interfaces.user;

public record UserBalanceRequest (
        long userId,
        long amount
){}
