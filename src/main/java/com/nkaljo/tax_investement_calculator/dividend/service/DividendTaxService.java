package com.nkaljo.tax_investement_calculator.dividend.service;

import com.nkaljo.tax_investement_calculator.common.service.ExchangeRateService;
import com.nkaljo.tax_investement_calculator.common.util.DateUtil;
import com.nkaljo.tax_investement_calculator.dividend.domain.request.DividendTaxRequest;
import com.nkaljo.tax_investement_calculator.dividend.domain.response.DividendTaxResponse;
import org.springframework.stereotype.Service;

@Service
public class DividendTaxService {

    private static final Double TAX_DIVIDEND_RATE = 0.15;

    private final ExchangeRateService exchangeRateService;

    public DividendTaxService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public DividendTaxResponse calculateDividendTax(DividendTaxRequest request) {
        return exchangeRateService.getRsdExchangeRate(request.currency(), request.date()).map(rate -> {
            final var dividendAmount = request.amount() * rate;
            final var dividendTaxAmount = dividendAmount * TAX_DIVIDEND_RATE;
            final var taxPaid = request.taxPaid() * rate;
            final var tax = Double.max(dividendTaxAmount - taxPaid, 0);

            return new DividendTaxResponse(getTaxDueDate(request.date()), dividendAmount, dividendAmount, dividendTaxAmount, taxPaid, tax);
        }).orElse(null);
    }

    public static String getTaxDueDate(String inputDate) {
        return DateUtil.getFirstWorkingDay(inputDate);
    }
}
