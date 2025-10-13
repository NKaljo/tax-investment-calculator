package com.nkaljo.tax_investement_calculator.investment.domain;

public record InvestmentTransaction(String date, Double numberOfShares, Double pricePerShare) {

    public InvestmentTransaction(InvestmentTransaction copy) {
        this(copy.date, copy.numberOfShares, copy.pricePerShare);
    }
}
