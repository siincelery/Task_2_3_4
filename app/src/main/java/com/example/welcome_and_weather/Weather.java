package com.example.welcome_and_weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Weather extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private String provider;
    private EditText cityEditText;
    private TextView weatherTextView, humidityTextView, seaLevelTextView;
    private ImageView weatherIconImageView;
    private WeatherRequest weatherRequest;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityEditText = findViewById(R.id.editTextCity);
        weatherTextView = findViewById(R.id.textViewWeather);
        humidityTextView = findViewById(R.id.textViewHumidity);
        seaLevelTextView = findViewById(R.id.textViewSeaLevel);
        weatherIconImageView = findViewById(R.id.imageViewWeatherIcon);


        weatherRequest = new WeatherRequest(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            getWeatherByLocation();
        }
    }
    public void onGetWeather(View view) {
        String city = cityEditText.getText().toString();
        getWeather(city);
    }

    private void getWeatherByLocation() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String city = getCityNameFromLocation(location);
                getWeather(city);
            }
        };
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    private String getCityNameFromLocation (Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses != null && addresses.size() > 0 ? addresses.get(0).getLocality() : "Unknown City";
    }

    private void getWeather(String city) {
        weatherRequest = new WeatherRequest(this);

        weatherRequest.getWeather(city, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int cod = response.getInt("cod");
                    if (cod == 200) {
                        // Обработка успешного ответа
                        String iconUrl = weatherRequest.getIconUrl(response);
                        Glide.with(Weather.this).load(iconUrl).into(weatherIconImageView);

                        JSONObject mainObject = response.getJSONObject("main");
                        JSONArray weatherArray = response.getJSONArray("weather");

                        JSONObject weatherObject = weatherArray.getJSONObject(0);
                        weatherTextView.setText(weatherObject.getString("main"));

                        humidityTextView.setText(String.valueOf(mainObject.getDouble("humidity")));
                        seaLevelTextView.setText(String.valueOf(mainObject.getDouble("sea_level")));
                    } else {
                        // Обработка ошибок от сервера
                        String errorMessage = response.getString("message");
                        if (errorMessage.equals("city not found")) {
                            // Город не найден
                            Toast.makeText(Weather.this, "Город не найден", Toast.LENGTH_SHORT).show();
                        } else {
                            // Другие ошибки
                            Toast.makeText(Weather.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Weather.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Weather.this, "Timeout or No Connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Weather.this, "Auth Failure", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Weather.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Weather.this, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Weather.this, "Parse Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}