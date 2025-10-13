package com.nkaljo.tax_investement_calculator.dividend.controller;


import com.nkaljo.tax_investement_calculator.dividend.domain.request.DividendTaxRequest;
import com.nkaljo.tax_investement_calculator.dividend.domain.response.DividendTaxResponse;
import com.nkaljo.tax_investement_calculator.dividend.service.DividendTaxService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dividend")
public class DividendController {

    private final DividendTaxService dividendTaxCalculatorService;

    public DividendController(DividendTaxService dividendTaxCalculatorService) {
        this.dividendTaxCalculatorService = dividendTaxCalculatorService;
    }

    @PostMapping("/tax")
    public DividendTaxResponse calculateDividendTax(@RequestBody DividendTaxRequest request) {
        return dividendTaxCalculatorService.calculateDividendTax(request);
    }
}
