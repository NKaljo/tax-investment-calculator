package com.nkaljo.tax_investement_calculator.common.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private static final String BASE_URL = "https://webappcenter.nbs.rs/ExchangeRateWebApp/ExchangeRate/IndexByDate";

    public Optional<Double> getRsdExchangeRate(String currencyCode, String date) {
        try {
            String url = BASE_URL + "?isSearchExecuted=true&Date=" +
                    java.net.URLEncoder.encode(date, "UTF-8") +
                    "&ExchangeRateListTypeID=3";

            return parseExchangeRate(url, currencyCode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rate", e);
        }
    }

    private Optional<Double> parseExchangeRate(String url, String currencyCode) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements rows = doc.select("table tbody tr");

        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() >= 5 && cells.get(0).text().trim().equalsIgnoreCase(currencyCode)) {
                var rate = cells.get(4).text().trim(); // 5th column is "srednji kurs"
                if (!rate.isEmpty()) {
                    var doubleRate = Double.parseDouble(rate.replace(",", "."));
                    return Optional.of(doubleRate);
                }
            }
        }

        return Optional.empty();
    }


}