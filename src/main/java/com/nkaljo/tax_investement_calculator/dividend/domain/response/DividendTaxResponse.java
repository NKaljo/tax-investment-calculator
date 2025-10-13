package com.nkaljo.tax_investement_calculator.dividend.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DividendTaxResponse(
        @JsonProperty("Izbor prijave")
        String taxType,
        @JsonProperty("1.4 Datum dospelosti poreske obaveze")
        String taxDueDate,
        @JsonProperty("4.2 Sifra vrste prihoda")
        String incomeCode,
        @JsonProperty("4.6 Bruto prihod")
        Double totalDividendAmount,
        @JsonProperty("4.7 Osnovica za porez")
        Double amountForTax,
        @JsonProperty("4.8 Obracunati porez")
        Double calculatedTax,
        @JsonProperty("4.9 Porez placen u drugoj drzavi")
        Double taxAlreadyPaid,
        @JsonProperty("4.10 Porez za uplatu")
        Double tax) {

    public DividendTaxResponse(String taxDueDate, Double totalDividendAmount, Double amountForTax, Double calculatedTax, Double taxAlreadyPaid, Double tax) {
        this("PP OPO", "11-402", taxDueDate, totalDividendAmount, amountForTax, calculatedTax, taxAlreadyPaid, tax);
    }
}
