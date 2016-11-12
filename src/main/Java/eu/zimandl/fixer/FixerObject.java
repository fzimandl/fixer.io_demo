package eu.zimandl.fixer;

import com.google.gson.JsonObject;

import java.math.BigDecimal;


public class FixerObject {
    private String base;
    private String date;
    private JsonObject rates;

    public String toString() {
        return base + date + rates;
    }

    public BigDecimal getRate(String currency) {
        return rates.get(currency).getAsBigDecimal();
    }

    public JsonObject getRates() {
        return rates;
    }
}