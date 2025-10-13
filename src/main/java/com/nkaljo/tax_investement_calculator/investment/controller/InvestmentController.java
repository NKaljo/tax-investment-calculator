package com.nkaljo.tax_investement_calculator.investment.controller;

import com.nkaljo.tax_investement_calculator.common.util.DateUtil;
import com.nkaljo.tax_investement_calculator.investment.domain.InvestmentTransaction;
import com.nkaljo.tax_investement_calculator.investment.domain.request.CapitalGainTaxRequest;
import com.nkaljo.tax_investement_calculator.investment.domain.response.CapitalGainTaxResponse;
import com.nkaljo.tax_investement_calculator.investment.service.CapitalGainTaxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investment")
public class InvestmentController {

    private final CapitalGainTaxService capitalGainTaxService;

    public InvestmentController(CapitalGainTaxService capitalGainTaxService) {
        this.capitalGainTaxService = capitalGainTaxService;
    }

    @PostMapping("/capital-gain-tax")
    public ResponseEntity<CapitalGainTaxResponse> calculateCapitalGainTax(@RequestBody CapitalGainTaxRequest request) {
        validateRequest(request);

        return ResponseEntity.ok(capitalGainTaxService.calculateCapitalGainTax(request));
    }

    private void validateRequest(CapitalGainTaxRequest request) {
        final var numberOfSharesSold = request.sellTransaction().numberOfShares();
        final var numberOfSharesBought = request.buyTransactions().stream()
                .mapToDouble(InvestmentTransaction::numberOfShares)
                .sum();

        if (numberOfSharesSold > numberOfSharesBought) {
            //400
            throw new RuntimeException("Can't have more sold shares then bought.");
        }

        final var sellDate = request.sellTransaction().date();

        final var buyDatesBeforeSellDate = request.buyTransactions().stream()
                .allMatch(btx -> DateUtil.isBefore(btx.date(),sellDate));

        if (!buyDatesBeforeSellDate) {
            throw new RuntimeException("Dates of buy transactions must be before the sell transaction date.");
        }
    }

}
