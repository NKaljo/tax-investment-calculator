package com.nkaljo.tax_investement_calculator.dividend.domain.request;

public record DividendTaxRequest(
        // date format: dd.MM.yyyy.
        String date,
        String currency,
        Double amount,
        Double taxPaid) {
}
