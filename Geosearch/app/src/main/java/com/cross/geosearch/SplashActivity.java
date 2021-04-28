package com.cross.geosearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cross.geosearch.pojo.Page;
import com.cross.geosearch.utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    private String lat = "";
    private String lon = "";
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        compositeDisposable = new CompositeDisposable();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
            }
        };

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            checkLocationPermission();
        } else {
            Toast.makeText(SplashActivity.this, getString(R.string.no_gps), Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, 2000);
        }


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());

                isInternetAvailable();
            }
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, locationListener);

                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lat = String.valueOf(location.getLatitude());
                        lon = String.valueOf(location.getLongitude());

                        isInternetAvailable();
                    }
                } else {
                    checkLocationPermission();
                }
                return;
            }
        }
    }

    public static Single<Boolean> hasInternetConnection() {
        return Single.fromCallable(() -> {
            try {
                int timeoutMs = 1500;
                Socket socket = new Socket();
                InetSocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

                socket.connect(socketAddress, timeoutMs);
                socket.close();

                return true;
            } catch (IOException e) {
                return false;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void isInternetAvailable() {
        Disposable disposable = hasInternetConnection().subscribe((hasInternet) -> {
            if (hasInternet) {
                searchNearby();
            } else {
                Toast.makeText(SplashActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });

        compositeDisposable.add(disposable);
    }

    private void searchNearby() {
        String url = "https://en.wikipedia.org/w/api.php?action=query&generator=geosearch&prop=coordinates%7Cpageimages&ggscoord=" + lat + "%7C" + lon + "&format=json&pithumbsize=500";

        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        i.putExtra("lat", lat);
        i.putExtra("lon", lon);

        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject pages = response.getJSONObject("query").getJSONObject("pages");
                            Iterator iterator = pages.keys();
                            List<Page> elements = new ArrayList<>();

                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                if (pages.get(key) instanceof JSONObject) {
                                    elements.add(gson.fromJson(String.valueOf(pages.get(key)), Page.class));
                                }
                            }

                            i.putExtra("data", (Serializable) elements);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        VolleySingleton.getInstance(SplashActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}