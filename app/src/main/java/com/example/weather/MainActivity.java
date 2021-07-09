package com.example.weather;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    TextView temperature;
    TextView dayOne;
    TextView dayTwo;
    TextView dayThree;
    TextView dayFour;
    TextView dayFive;
    TextView city;
    TextView dateDayOne;
    TextView dateDayTwo;
    TextView dateDayThree;
    TextView dateDayFour;
    TextView dateDayFive;
    TextView maxTemperature;
    TextView minTemperature;
    ImageView icon;
    TextView max;
    TextView min;
    TextView credits;
    CardView cityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherAPI();
        setCity();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    public int toCelsius(float temp){
        temp -= 273.15;
        return Math.round(temp);
    }

    public void weatherAPI(){
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat={}&exclude=minutely,hourly&lon={}&appid={}";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", "value");
        params.put("more", "data");
        temperature = findViewById(R.id.temperature);
        dayOne = findViewById(R.id.DayOne);
        dayTwo = findViewById(R.id.dayTwo);
        dayThree = findViewById(R.id.DayThree);
        dayFour = findViewById(R.id.dayFour);
        dayFive = findViewById(R.id.dayFive);
        dateDayOne = findViewById(R.id.dateDayOne);
        dateDayTwo = findViewById(R.id.dateDayTwo);
        dateDayThree = findViewById(R.id.dateDayThree);
        dateDayFour = findViewById(R.id.dateDayFour);
        dateDayFive = findViewById(R.id.dateDayFive);
        icon = findViewById(R.id.icon);
        //icon.setImageResource(R.mipmap.thunderstorm);
        maxTemperature = findViewById(R.id.maxTemperature);
        minTemperature = findViewById(R.id.minTemperature);
        credits = findViewById(R.id.credits);
        String creditsTXT = "Powered by: Tacorico787";
        credits.setText(creditsTXT);
        cityView = findViewById(R.id.cityView);
        max = findViewById(R.id.Max);
        min = findViewById(R.id.Min);
        max.setText("Max: ");
        min.setText("Min: ");

        client.get(url, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        JSONObject today = null;
                        JSONArray week = null;
                        JSONObject day = null;
                        JSONObject temp = null;

                        try {
                            today = response.getJSONObject("current");
                            temperature.setText(String.valueOf(toCelsius(today.getInt("temp"))));

                            JSONArray weather = today.getJSONArray("weather");
                            JSONObject weatherSpecific = weather.getJSONObject(0);
                            setIcon(weatherSpecific.getString("main"));

                            week = response.getJSONArray("daily");

                            today = week.getJSONObject(0);
                            today = today.getJSONObject("temp");
                            maxTemperature.setText(String.valueOf(toCelsius(today.getInt("max"))));
                            minTemperature.setText(String.valueOf(toCelsius(today.getInt("min"))));

                            day = week.getJSONObject(1);
                            temp = day.getJSONObject("temp");
                            dayOne.setText(String.valueOf(toCelsius(temp.getInt("day"))));
                            int unixTime = day.getInt("dt");
                            Date date = new Date((long)unixTime*1000);
                            SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
                            dateDayOne.setText(simpleDateformat.format(date));



                            day = week.getJSONObject(2);
                            temp = day.getJSONObject("temp");
                            dayTwo.setText(String.valueOf(toCelsius(temp.getInt("day"))));
                            unixTime = day.getInt("dt");
                            date.setTime((long)unixTime*1000);
                            dateDayTwo.setText(simpleDateformat.format(date));

                            day = week.getJSONObject(3);
                            temp = day.getJSONObject("temp");
                            dayThree.setText(String.valueOf(toCelsius(temp.getInt("day"))));
                            unixTime = day.getInt("dt");
                            date.setTime((long)unixTime*1000);
                            dateDayThree.setText(simpleDateformat.format(date));

                            day = week.getJSONObject(4);
                            temp = day.getJSONObject("temp");
                            dayFour.setText(String.valueOf(toCelsius(temp.getInt("day"))));
                            unixTime = day.getInt("dt");
                            date.setTime((long)unixTime*1000);
                            dateDayFour.setText(simpleDateformat.format(date));

                            day = week.getJSONObject(5);
                            temp = day.getJSONObject("temp");
                            dayFive.setText(String.valueOf(toCelsius(temp.getInt("day"))));
                            unixTime = day.getInt("dt");
                            date.setTime((long)unixTime*1000);
                            dateDayFive.setText(simpleDateformat.format(date));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("debug" , "Doesn't");
                    }
                }
        );
    }

    public void setIcon(String condition){
        switch (condition) {
            case "Clear":
                cityView.setCardBackgroundColor(getResources().getColor(R.color.clear));
                icon.setImageResource(R.mipmap.sun);
                break;
            case "Clouds":
            case "Mist":
                cityView.setCardBackgroundColor(getResources().getColor(R.color.cloud));
                icon.setImageResource(R.mipmap.cloud);
                break;
            case "Rain":
            case "Drizzle":
                cityView.setCardBackgroundColor(getResources().getColor(R.color.rain));
                icon.setImageResource(R.mipmap.rain);
                break;
            case "Thunderstorm":
                cityView.setCardBackgroundColor(getResources().getColor(R.color.rain));
                icon.setImageResource(R.mipmap.thunderstorm);
                break;
        }
    }

    public void setCity(){
        TextView city = findViewById(R.id.City);
        String url = "https://api.openweathermap.org/data/2.5/weather?lat={}&lon={}&appid={}";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", "value");
        params.put("more", "data");
        client.get(url, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            city.setText(response.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("debug" , "Doesn't");
                    }
                }
        );

    }
}
