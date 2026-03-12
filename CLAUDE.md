# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ExchangeRateServiceTest

# Run a single test method
./mvnw test -Dtest=ExchangeRateServiceTest#testGetMiddleRateUsd
```

The app starts on port `9999` with context path `/tax-investement-calculator`.

## Architecture

Spring Boot 3.5.4 REST API (Java 24) for calculating Serbian investment taxes. Two independent feature modules (`dividend`, `investment`) share a `common` module.

**Package structure:** `com.nkaljo.tax_investement_calculator`
- `common/service/ExchangeRateService` — scrapes live RSD exchange rates from the NBS (National Bank of Serbia) website using jsoup. Returns `Optional<Double>` for the "srednji kurs" (middle rate).
- `common/util/DateUtil` — date parsing/formatting with the `dd.MM.yyyy.` format (note the trailing dot). All date strings throughout the app use this format.

**Dividend module** (`POST /tax-investement-calculator/dividend/tax`):
- Input: `DividendTaxRequest(date, currency, amount, taxPaid)`
- Fetches the RSD rate for the given date, converts the foreign dividend to RSD, applies the 15% tax rate, subtracts tax already paid abroad
- Output fields map directly to Serbian tax form PP OPO (income code 11-402), with Serbian field names via `@JsonProperty`

**Capital gain module** (`POST /tax-investement-calculator/investment/capital-gain-tax`):
- Input: `CapitalGainTaxRequest(currency, sellTransaction, buyTransactions[])`
- Each `InvestmentTransaction` has `date`, `numberOfShares`, `pricePerShare`
- Uses FIFO matching (`matchBuyTransactionForTax`) to allocate buy lots against the sell quantity
- Fetches RSD rates for each buy date and the sell date to compute RSD-denominated amounts
- Validation (in controller): sold shares ≤ bought shares; all buy dates must be before sell date
- Output: `CapitalGainTaxResponse(sellTransaction, buyTransactions, leftOverTransactions)` where amounts are in RSD

**Key constraints:**
- `ExchangeRateService.getRsdExchangeRate` makes a live HTTP request to NBS; tests mock `Jsoup.connect` statically (mockito-inline)
- `CapitalGainTaxService` calls `.get()` directly on the `Optional` returned by `ExchangeRateService` — no null safety; if the NBS scrape fails or returns no rate, it will throw
- `buyTransactions` list in `CapitalGainTaxRequest` is mutable (sorted in-place in the service by date before FIFO matching)
