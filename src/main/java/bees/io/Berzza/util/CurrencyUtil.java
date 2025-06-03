package bees.io.Berzza.util;

import bees.io.Berzza.domain.GlobalCasinoConfiguration;
import bees.io.Berzza.domain.dto.BetDTO;
import bees.io.Berzza.domain.enums.ECurrency;

import static bees.io.Berzza.domain.GlobalCasinoConfiguration.casinoBaseCurrencyExchangeRate;

public class CurrencyUtil {



//    private static  HashMap<String,Double> baseCurrencyConversions =new HashMap<>(){{
//        put("EUR",1.0);
//        put("USD",1.18);
//        put("RSD",117.0);
//
//    }};
//    private static final Map<ECurrency, Map<ECurrency, Double>> conversionRates = new HashMap<>();
//
//    static {
//        // Initialize conversion rates
//        Map<ECurrency, Double> usdRates = new HashMap<>();
//        usdRates.put(ECurrency.EUR, 0.85);
//        usdRates.put(ECurrency.GBP, 0.75);
//        usdRates.put(ECurrency.INR, 74.50);
//        usdRates.put(ECurrency.JPY, 110.0);
//
//        Map<ECurrency, Double> eurRates = new HashMap<>();
//        eurRates.put(ECurrency.EUR, 1.0);
//        eurRates.put(ECurrency.USD, 1.18);
//        eurRates.put(ECurrency.GBP, 0.88);
//        eurRates.put(ECurrency.INR, 88.0);
//        eurRates.put(ECurrency.JPY, 129.0);
//
//        Map<ECurrency, Double> gbpRates = new HashMap<>();
//        gbpRates.put(ECurrency.USD, 1.34);
//        gbpRates.put(ECurrency.EUR, 1.14);
//        gbpRates.put(ECurrency.INR, 99.5);
//        gbpRates.put(ECurrency.JPY, 146.0);
//
//
//        conversionRates.put(ECurrency.USD, usdRates);
//        conversionRates.put(ECurrency.EUR, eurRates);
//        conversionRates.put(ECurrency.GBP, gbpRates);
//    }
//
//
//    public static double convertCurrency(double amount, Currency fromCurrency){
//        return amount*baseCurrencyConversions.get(fromCurrency.getName());
//    }

    public static double toBaseCurrency(double amount){
        return amount * casinoBaseCurrencyExchangeRate;
    }

    public static double toBaseCurrency(double amount, ECurrency fromCurrency){
        return amount * casinoBaseCurrencyExchangeRate;
    }
//    public static double convertCurrency(double amount, Currency fromCurrency, Currency toCurrency){
//        if (fromCurrency == toCurrency) {
//            return amount; // No conversion needed
//        }
//
//        if (!conversionRates.containsKey(fromCurrency) || !conversionRates.get(fromCurrency).containsKey(toCurrency)) {
//            throw new IllegalArgumentException("Conversion rate not available");
//        }
//
//        double rate = conversionRates.get(fromCurrency).get(toCurrency);
//        return amount * rate;
//    }

    public static BetDTO mapToCasinoCurrency(BetDTO betDTO  ) {

//        betDTO.setAmount(betDTO.getAmount()/ casinoBaseCurrencyExchangeRate);
//        betDTO.setCashOut(betDTO.getCashOut()/ casinoBaseCurrencyExchangeRate);
        return betDTO;

    }
}
