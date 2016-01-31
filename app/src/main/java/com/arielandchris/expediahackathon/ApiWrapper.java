package com.arielandchris.expediahackathon;

import android.content.Context;
import android.content.res.AssetManager;

import com.arielandchris.expediahackathon.model.Airport;
import com.arielandchris.expediahackathon.model.GeoSearch;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Chris on 1/30/2016.
 */
// Flight Search
// http://terminal2.expedia.com:80/x/mflights/search?departureDate=2016-02-2&departureAirport=SEA&arrivalAirport=JFK
// Things to do
// http://terminal2.expedia.com:80/x/activities/search?location=London&startDate=2015-08-08&endDate=2015-08-18
// GeoSearch
// http://terminal2.expedia.com:80/x/geo/features?within=50km&lat=37.777363&lng=-122.453269&type=airport
public class ApiWrapper {

    private interface ExpediaInterface {
        @GET("geo/features")
        Call<List<GeoSearch>> geoSearch(
                @Query("within") String within,
                @Query("lat") String lat,
                @Query("lng") String lng,
                @Query("type") String type,
                @Header("Authorization") String apiKey
        );
    }

    private final String API_KEY;
    private Gson gson;
    private ExpediaInterface service;

    public ApiWrapper(String apiKey) {
        API_KEY = apiKey;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://terminal2.expedia.com:80/x/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        gson = new Gson();
        service = retrofit.create(ExpediaInterface.class);
    }

    public GeoSearch geoSearch(String within, String lat, String lng, String type, Callback<List<GeoSearch>> callback) {

        //service.geoSearch(within, lat, lng, type, API_KEY);
        Call<List<GeoSearch>> call = service.geoSearch(within, lat, lng, type, "expedia-apikey key=" + API_KEY);
        call.enqueue(callback);
        return null;
    }

    public String thingsToDo(String searchTerm) {
        return thingsToDo(searchTerm, "none", "none");
    }

    public String thingsToDo(String searchTerm, String startDate, String endDate) {
        return "fddasf";

    }

    public static ArrayList<Airport> getAirportsByDist(Context context, String origin) {
        AssetManager am = context.getAssets();
        StringBuilder sb = null;
        ArrayList<Airport> airportList = new ArrayList<Airport>();
        Airport originAP = getAirportByCode(context, origin);
        if (originAP == null) return null; // THROW AN ERROR OR DO THIS <--
        try {
            BufferedReader br =  new BufferedReader(new InputStreamReader(am.open("test.txt")));
            sb = new StringBuilder();
            String mLine = br.readLine();
            while (mLine != null) {
                String lat, lng, code;
                String[] arr = mLine.split(",");
                lat = arr[6];
                lng = arr[7];
                code = arr[4];
                code = code.replace("\"","");
                airportList.add(new Airport(code, Double.parseDouble(lat) - originAP.getLat(), Double.parseDouble(lng) - originAP.getLng()));
                mLine = br.readLine();
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Collections.sort(airportList);
        return airportList;
    }

    public static Airport getAirportByCode(Context context, String code) {
        AssetManager am = context.getAssets();
        StringBuilder sb = null;
        try {
            BufferedReader br =  new BufferedReader(new InputStreamReader(am.open("validAirports.txt")));
            sb = new StringBuilder();
            String mLine = br.readLine();
            while (mLine != null) {
                String[] arr = mLine.split(",");
                String apc = arr[4].replace("\"","");
                if (!apc.equalsIgnoreCase(code)) {
                    mLine = br.readLine();
                } else {
                    br.close();
                    return new Airport(apc, arr[6], arr[7]);
                }
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
