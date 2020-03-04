package com.example.appbella.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.appbella.Common.Common;
import com.example.appbella.Maps.PermissionUtils;
import com.example.appbella.Model.LocationHelper;
import com.example.appbella.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class MapsBottomDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener{

    public static final String TAG = " ";
    private static View view;
    private GoogleMap googleMap;
    private Location location;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private EditText edt_address_complements;

    public static MapsBottomDialogFragment newInstance() {
        return new MapsBottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
            setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
            view = inflater.inflate(R.layout.maps_bottom_sheet, container, false);
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            Button btn_save_Location = view.findViewById(R.id.btn_save_Location);
            btn_save_Location.setOnClickListener(this);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        UiSettings uiSettings = googleMap.getUiSettings();
        enableMyLocation();
        goToMyLocation();
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.setOnMyLocationClickListener(this);
        googleMap.setOnMyLocationButtonClickListener(this);

        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setIndoorLevelPickerEnabled(true);

        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setAllGesturesEnabled(true);

    }

    private void goToMyLocation()
    {
        edt_address_complements = view.findViewById(R.id.edt_address_complements);
        TextView adress = view.findViewById(R.id.txt_adress);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            enableMyLocation();
        } else
        {
            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),17));
                adress.setText(getAddress(location.getLatitude(), location.getLongitude()));
            }
        }
    }


    private String getAddress(double lat, double lng) {

        String address=null;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> list = null;
        try{
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(list == null){
            System.out.println("Fail to get address from location");
            return null;
        }
        if(list.size() > 0){
            Address addr = list.get(0);
            address = removeNULL(addr.getThoroughfare()) + " " +
                    removeNULL(addr.getFeatureName());
        }
        return address;
    }

    private String removeNULL(String string) {
        if (string==null) {
            return "";
        }
        else {
            return string;
        }
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_save_Location).setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // need for android 6 and above
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE)
        {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            enableMyLocation();
        }
    }

    // need for android 6 and above
    private void enableMyLocation()
    {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null)
        {
            googleMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onClick(View view) {
        String complements = edt_address_complements.getText().toString();
        if (TextUtils.isEmpty(complements)) {
            Toast.makeText(getContext(), "No puede estar vacio", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationHelper helper = new LocationHelper(
                location.getLongitude(),
                location.getLatitude(),
                complements
        );
        Common.currentlocation = helper;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("Current Location").child(auth.getCurrentUser().getUid())
                .setValue(helper).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getContext(), "Ubicación Agregada", Toast.LENGTH_SHORT).show();
                dismiss();
            }
            else{
                Toast.makeText(getContext(), "Ubicación no pudo ser guardada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMyLocationClick(@NonNull Location location)
    {
        Log.d(TAG, "onMyLocationClick() called with: location = [" + location + "]");
    }

    @Override
    public boolean onMyLocationButtonClick()
    {
        Log.d(TAG, "onMyLocationButtonClick() called");
        goToMyLocation();
        return true;
    }

}

