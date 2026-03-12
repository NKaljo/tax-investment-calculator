package com.nkaljo.tax_investement_calculator.investment.service;

import com.nkaljo.tax_investement_calculator.common.service.ExchangeRateService;
import com.nkaljo.tax_investement_calculator.common.util.DateUtil;
import com.nkaljo.tax_investement_calculator.investment.domain.InvestmentTransaction;
import com.nkaljo.tax_investement_calculator.investment.domain.TaxTransaction;
import com.nkaljo.tax_investement_calculator.investment.domain.request.CapitalGainTaxRequest;
import com.nkaljo.tax_investement_calculator.investment.domain.response.CapitalGainTaxResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CapitalGainTaxService {

    private final ExchangeRateService exchangeRateService;

    public CapitalGainTaxService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public CapitalGainTaxResponse calculateCapitalGainTax(CapitalGainTaxRequest request) {
        final var currency = request.currency();
        final var sellTx = request.sellTransaction();
        final var buyTxs = request.buyTransactions();

        // sort by date
        buyTxs.sort(Comparator.comparing(tx -> DateUtil.convertToDate(tx.date())));

        final var buyTxsForTax = matchBuyTransactionForTax(sellTx, buyTxs);

        final var buyTaxTransactions = buyTxsForTax.stream()
                .map(buyTx -> {
                    final var rate = exchangeRateService.getRsdExchangeRate(currency, buyTx.date()).get();
                    return new TaxTransaction(buyTx.date(), rate, rate * buyTx.numberOfShares() * buyTx.pricePerShare());
                })
                .toList();
        final var sellRate = exchangeRateService.getRsdExchangeRate(currency, sellTx.date()).get();
        final var sellTaxTransaction = new TaxTransaction(sellTx.date(), sellRate, sellRate * sellTx.numberOfShares() * sellTx.pricePerShare());

        final var totalBought = buyTaxTransactions.stream().mapToDouble(TaxTransaction::amount).sum();
        final var totalDifference = sellTaxTransaction.amount() - totalBought;
        final var taxToPay = totalDifference * 0.15;

        return new CapitalGainTaxResponse(sellTaxTransaction, buyTaxTransactions, Collections.emptyList(), totalDifference, taxToPay);
    }

    private List<InvestmentTransaction> matchBuyTransactionForTax(InvestmentTransaction sellTx, List<InvestmentTransaction> buyTxs) {
        final List<InvestmentTransaction> buyTxsForTax = new ArrayList<>();

        var numberOfSoldShares = sellTx.numberOfShares();
        int index = 0;
        while (numberOfSoldShares > 0) {
            final var currentBuyTx = buyTxs.get(index);
            final var currentNumberOfShares = currentBuyTx.numberOfShares();

            if (currentNumberOfShares < numberOfSoldShares) {
                numberOfSoldShares -= currentNumberOfShares;
                index++;
                buyTxsForTax.add(new InvestmentTransaction(currentBuyTx));
            } else {
                buyTxsForTax.add(new InvestmentTransaction(currentBuyTx.date(), numberOfSoldShares, currentBuyTx.pricePerShare()));
                break;
            }
        }
        return buyTxsForTax;
    }
}
