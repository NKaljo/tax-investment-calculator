package com.nkaljo.tax_investement_calculator.investment.domain.request;

import com.nkaljo.tax_investement_calculator.investment.domain.InvestmentTransaction;

import java.util.List;

public record CapitalGainTaxRequest(String currency, InvestmentTransaction sellTransaction, List<InvestmentTransaction> buyTransactions) {

}
