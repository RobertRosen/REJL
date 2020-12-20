package services;

import Controllers.DataHandler;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import model.CurrentWeather;

import com.google.gson.Gson;

/**
 * Service for communication with a Weather API. Currently: YR API
 * The retrieved JSON code is represented in a bean (java object) for easier manipulation in java.
 */
public class WeatherService extends APIService{

    public WeatherService(DataHandler dataHandler) {
        super(dataHandler);
    }

    @Override
    public HttpResponse<JsonNode> response(DataHandler dataHandler) {
        return Unirest.get("https://api.met.no/weatherapi/locationforecast/2.0/compact?")
                .header("Accept", "application/json")
                .queryString("lat", dataHandler.getLatitude())                // Build on base url
                .queryString("lon", dataHandler.getLongitude())               // Build on base url
                .asJson();
    }

    @Override
    public Object returnObject(Gson gson, JSONObject jsonObject) {
        // Retrieve wanted fields from the JSONObject. Parsing the JSON tree.
        JSONObject jsonFieldObject = jsonObject
                .getJSONObject("properties")
                .getJSONArray("timeseries")
                .getJSONObject(0)   //TODO: Sök upp en tid som matchar. YR använder cachade rapporter, ej liveuppdatering.
                .getJSONObject("data")
                .getJSONObject("next_1_hours")
                .getJSONObject("summary");

        return gson.fromJson(jsonFieldObject.toString(), CurrentWeather.class);
    }

//    //TODO: Remove this method. Keep for now...
//    private Location testAndPrintMarshalling(Gson gson, JSONObject jsonObject) {
//        JSONObject jsonField    = jsonObject.getJSONObject("geometry");
//        String json             = jsonField.toString();
//
//        return gson.fromJson(json, Location.class);
//    }
}