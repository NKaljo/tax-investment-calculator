package com.nkaljo.tax_investement_calculator.investment.domain.response;

import com.nkaljo.tax_investement_calculator.investment.domain.InvestmentTransaction;
import com.nkaljo.tax_investement_calculator.investment.domain.TaxTransaction;

import java.util.List;

public record CapitalGainTaxResponse(TaxTransaction sellTransaction, List<TaxTransaction> buyTransactions, List<InvestmentTransaction> leftOverTransactions, Double totalDifference, Double taxToPay) {
}
