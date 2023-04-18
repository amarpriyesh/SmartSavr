package com.example.smartsavr;

@FunctionalInterface
public interface EarningsBalanceConsumer {
    void accept(int balanceCents, int sumWeeklyCents, int sumMonthlyCents);
}
