package info.brightly.bright;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class Map extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public GoogleMap mMap;
    public SupportMapFragment mapFragment;
    public LocationManager locationManager;
    public LatLng restaurant;
    public HashMap<String, LatLng> hashMap = new HashMap<String, LatLng>();
    public String name;
    private void fillMap() {
        hashMap.put("Reuben Hills", new LatLng(-33.882956,151.210960));
        hashMap.put("GreyHound", new LatLng(-33.877462, 151.193297));
        hashMap.put("Toby's", new LatLng(-33.885720, 151.194789));
        hashMap.put("William's", new LatLng(-33.885720, 151.194789));
        hashMap.put("Mary's", new LatLng(-33.885720, 151.194789));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fillMap();
        Bundle extras = getIntent().getExtras();
        name = extras.getString("Address");
        restaurant = hashMap.get(name);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (restaurant == null) {
            restaurant = new LatLng(-33.852,151.211);
        }
        if(name == null) {
            name = "Restaurant Name";
        }
        LatLng sydney = new LatLng(-33.852, 151.211);
        Marker m1 = mMap.addMarker(new MarkerOptions().position(new LatLng(restaurant.latitude, restaurant.longitude))
                .title(name)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Location curr = getLocation();
        LatLng currLatLng = null;
        if(curr != null) {
            currLatLng = new LatLng(curr.getLatitude(), curr.getLongitude());
        } else {
            Toast.makeText(getApplicationContext(), "Did not receive a current location", Toast.LENGTH_SHORT).show();
            currLatLng = sydney;
        }
        Marker m2 = mMap.addMarker(new MarkerOptions().position(currLatLng)
                .title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }

    // if permissions allow, returns a location, otherwise returns null
    private Location getLocation() {
        int LOCATION_REFRESH_TIME = 1000;
        int LOCATION_REFRESH_DISTANCE = 5;
        locationManager = (LocationManager) this.getApplicationContext().getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getApplicationContext(), "GPS not enabled", Toast.LENGTH_SHORT).show();
        }
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(getApplicationContext(), "Networks not enabled", Toast.LENGTH_SHORT).show();
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(getApplicationContext(), "Asking for permissions", Toast.LENGTH_SHORT).show();
        } else {
            //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);

            Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return l;
        }
        return null;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}