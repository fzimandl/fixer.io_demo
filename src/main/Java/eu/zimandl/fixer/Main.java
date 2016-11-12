package eu.zimandl.fixer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;

/**
 * Created by filip on 07/11/16.
 */
public class Main {
    public static void main(String[] args) throws UnirestException {
        //unirestImpl();
        gsonImpl();
    }

    private static String unirestImpl() throws UnirestException {
        Gson gson = new GsonBuilder().create();
        ObjectMapper mapper = new ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                return gson.fromJson(value, valueType);
            }

            @Override
            public String writeValue(Object value) {
                return null;
            }
        };
        Unirest.setObjectMapper(mapper);
        HttpResponse<FixerObject> jsonNodeHttpResponse = Unirest.get("http://api.fixer.io/latest").asObject(FixerObject.class);

        return jsonNodeHttpResponse.getBody().toString();
    }

    private static String gsonImpl() throws UnirestException {
        HttpResponse<String> ratesString = Unirest.get("http://api.fixer.io/latest").asString();

        Gson gson = new GsonBuilder().create();
        FixerObject ratesObj = gson.fromJson(ratesString.getBody(), FixerObject.class);

        System.out.println(ratesObj.getRate("CZK"));

        FixerUtils.printRates(ratesObj);
        FixerUtils.printMin(ratesObj);

        System.out.println(FixerUtils.convert(ratesObj, "USD", "CZK", new BigDecimal(25)));

        return ratesObj.toString();
    }
}
