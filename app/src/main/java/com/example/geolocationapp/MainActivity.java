package com.example.geolocationapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude;
    TextView longitude;
    TextView accuracy;
    TextView altitude;
    TextView address;
    String addressMesage = "";
   

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        latitude=findViewById(R.id.myTextViewLatitude);
        longitude=findViewById(R.id.myTextViewLongitdue);
        accuracy=findViewById(R.id.myTextViewAccuracy);
        altitude=findViewById(R.id.myTextViewAltitude);
        address=findViewById(R.id.myTextViewAddress);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if (listAddresses != null && listAddresses.size() > 0) {

                        if (listAddresses.get(0).getThoroughfare() != null) {
                            addressMesage += listAddresses.get(0).getThoroughfare() + "\n ";
                        }
                        if (listAddresses.get(0).getLocality() != null) {
                            addressMesage += listAddresses.get(0).getLocality() + "\n ";
                        }
                        if (listAddresses.get(0).getPostalCode() != null) {
                            addressMesage += listAddresses.get(0).getPostalCode() + "\n ";
                        }
                        if (listAddresses.get(0).getAdminArea() != null) {
                            addressMesage += listAddresses.get(0).getAdminArea() + "\n ";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                longitude.setText("Longitude:   "+location.getLongitude()+"");
                latitude.setText("Latitude:     "+location.getLatitude()+"");
                accuracy.setText("Accuracy:     "+location.getAccuracy()+" (m)");
                altitude.setText("Altitude:     "+location.getAltitude()+" (m)");
                address.setText(addressMesage);
                addressMesage=" ";

               // Log.i("Location",location.toString());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


}
