package eu.zimandl.fixer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingDouble;
import static java.util.Map.Entry;

/**
 * Created by filip on 12/11/16.
 */
public class FixerUtils {
    public static void printRates(FixerObject fo) {
        JsonObject rates = fo.getRates();
        Set<Map.Entry<String, JsonElement>> entries = rates.entrySet();
        entries.forEach(System.out::println);
    }

    public static void printMin(FixerObject fo) {
        System.out.println("Strongest currency: " + returnMinD(fo));
    }

    public static BigDecimal convertTo(FixerObject fo, String toCurrency, BigDecimal amountOfBaseCurrency) {
        return amountOfBaseCurrency.multiply(fo.getRate(toCurrency));
    }

    public static BigDecimal convert(FixerObject fo, String fromCurrency, String toCurrency, BigDecimal amount) {
        BigDecimal fromCurrRate = fo.getRate(fromCurrency);
        BigDecimal toCurrencyRate = fo.getRate(toCurrency);

        return amount.divide(fromCurrRate, BigDecimal.ROUND_HALF_UP).multiply(toCurrencyRate);
    }

    private static Map.Entry<String, Double> returnMinA(FixerObject fo) {
        final Stream<Map.Entry<String, JsonElement>> rates = fo.getRates().entrySet().stream();
        Optional<Entry<String, Double>> strongestPair = rates.map(e -> {
            final Entry<String, Double> simpleImmutableEntry = new AbstractMap.SimpleImmutableEntry(e.getKey(), e.getValue().getAsDouble());
            return simpleImmutableEntry;
        }).sorted(comparingDouble(Entry::getValue)).findFirst();

        return strongestPair.get();
    }

    private static Map.Entry<String, Double> returnMinB(FixerObject fo) {
        Stream<Map.Entry<String, JsonElement>> rates = fo.getRates().entrySet().stream();
        Map<String, Double> mapRates = rates.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getAsDouble()));
        final Map.Entry<String, Double> min = Collections.min(mapRates.entrySet(), comparingDouble(Entry::getValue));

        return min;
    }

    private static Map.Entry<String, JsonElement> returnMinC(FixerObject fo) {
        Stream<Entry<String, JsonElement>> stream = fo.getRates().entrySet().stream();
        List<Entry<String, JsonElement>> collect = stream.collect(Collectors.toList());
        Comparator<Map.Entry<String, JsonElement>> comBigDec = (r1, r2) -> r1.getValue().getAsBigDecimal().compareTo(r2.getValue().getAsBigDecimal());
        collect.sort(comBigDec);

        return collect.get(0);
    }

    private static Map.Entry<String, JsonElement> returnMinD(FixerObject fo) {
        Comparator<Map.Entry<String, JsonElement>> comBigDec = (r1, r2) -> r1.getValue().getAsBigDecimal().compareTo(r2.getValue().getAsBigDecimal());
        Stream<Entry<String, JsonElement>> stream = fo.getRates().entrySet().stream();
        final Optional<Entry<String, JsonElement>> collectedPair = stream.collect(Collectors.minBy(comBigDec));

        return collectedPair.get();
    }
}
