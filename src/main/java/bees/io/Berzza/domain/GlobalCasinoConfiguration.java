package bees.io.Berzza.domain;

import bees.io.Berzza.domain.enums.ECurrency;

import java.util.UUID;

public class GlobalCasinoConfiguration {

    public static ECurrency baseCurrency= ECurrency.EUR;
    public static ECurrency casinoCurrency=ECurrency.USD;
    public static Double casinoBaseCurrencyExchangeRate=0.94;
    public static Double casinoCasinoCurrencyExchangeRate=1.07;

    public static Double maxWin=1000000.0;
    public static Double minBet=10.0;
    public static Double maxBet=10000.0;
    public static Double rainMinBalance;

    public static Integer incrementor1;
    public static Integer incrementor2;
    public static  Integer incrementor3;
    public static Integer incrementor4;


    public static UUID casinoID=UUID.randomUUID();
}
