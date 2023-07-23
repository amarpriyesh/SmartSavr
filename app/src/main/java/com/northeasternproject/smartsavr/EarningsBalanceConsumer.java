package com.northeasternproject.smartsavr;

@FunctionalInterface
public interface EarningsBalanceConsumer {
    void accept(int totalBalance, int sumWeeklyCents, int sumMonthlyCents);
}
