package com.nkaljo.tax_investement_calculator.dividend.domain.request;

public record DividendTaxRequest(String date, String currency, Double amount, Double taxPaid) {
}
