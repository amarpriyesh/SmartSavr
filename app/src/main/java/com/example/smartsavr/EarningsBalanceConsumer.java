package com.example.smartsavr;

@FunctionalInterface
public interface EarningsBalanceConsumer {
    void accept(int totalBalance, int sumWeeklyCents, int sumMonthlyCents);
}
