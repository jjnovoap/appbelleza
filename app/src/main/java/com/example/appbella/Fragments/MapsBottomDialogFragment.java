package com.example.appbella.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.example.appbella.Common.Common;
import com.example.appbella.Maps.PermissionUtils;
import com.example.appbella.Model.LocationHelper;
import com.example.appbella.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsBottomDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    public static final String TAG = " ";
    @SuppressLint("StaticFieldLeak")
    private static View view;
    private GoogleMap googleMap;
    private Location location;
    private ImageView markerImage;
    private EditText adress;
    private double lat;
    private double log;
    private LatLng latLng;
    public static final int DEFAULT_INTENT_INT_VALUE = -1;
    //int AUTOCOMPLETE_REQUEST_CODE = 1;
    private EditText edt_address_complements;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSION_ACCESS_LOCATION = 1;

    public static MapsBottomDialogFragment newInstance() {
        return new MapsBottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (view != null) {
            //setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            //setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
            view = inflater.inflate(R.layout.maps_bottom_sheet, container, false);
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            fusedLocationProviderClient = getFusedLocationProviderClient(getContext());
            Button btn_save_Location = view.findViewById(R.id.btn_save_Location);
            markerImage = view.findViewById(R.id.marker_image_view);
            ImageView myLocationFab = view.findViewById(R.id.my_location_button);
            edt_address_complements = view.findViewById(R.id.edt_address_complements);
            ImageView markerShadowImage = view.findViewById(R.id.marker_shadow_image_view);
            markerShadowImage.setVisibility(View.VISIBLE);
            btn_save_Location.setOnClickListener(this);
            myLocationFab.setOnClickListener(view -> moveToMyLocation());
            adress = view.findViewById(R.id.edt_adress);
            adress.setClickable(false);
            adress.setFocusable(false);
            if (!Places.isInitialized()) {
                Places.initialize(getContext(), "AIzaSyCFHMkzZEUkRayMtcPrDSwZoBgHvAADvSc", Locale.US);
            }
            /*adress.setOnClickListener(v -> {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            });*/
            moveToMyLocation();
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
            e.printStackTrace();
        }
        return view;
    }

    /*@SuppressLint("RestrictedApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }*/

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
            ((BottomSheetBehavior) behavior).addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
            //((View)bottomSheet.getParent()).setBackgroundColor(Color.TRANSPARENT);

        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        UiSettings uiSettings = googleMap.getUiSettings();
        //goToMyLocation();
        //googleMap.setIndoorEnabled(true);
        //googleMap.setBuildingsEnabled(true);
        //googleMap.setOnMyLocationClickListener(this);
        ///googleMap.setOnMyLocationButtonClickListener(this);

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

        map.setOnCameraMoveStartedListener(i -> {
            if (markerImage.getTranslationY() == 0f) {
                markerImage.animate()
                        .translationY(-75f)
                        .setInterpolator(new OvershootInterpolator())
                        .setDuration(250)
                        .start();
            }
        });
        map.setOnCameraIdleListener(() -> {
            markerImage.animate()
                    .translationY(0f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(250)
                    .start();
            latLng = map.getCameraPosition().target;
            adress.setText(getAddress(latLng.latitude, latLng.longitude));
        });

        int mapRawResourceStyleRes = DEFAULT_INTENT_INT_VALUE;
        if (mapRawResourceStyleRes != DEFAULT_INTENT_INT_VALUE) {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), mapRawResourceStyleRes));
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

    @SuppressLint("MissingPermission")
    private void moveToMyLocation() {
        getDeviceLocation();
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
        if (requestCode == PERMISSION_ACCESS_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                PermissionUtils.requestPermission(getActivity(), PERMISSION_ACCESS_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, true);
                getDeviceLocation();
            }
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
                log,
                lat,
                complements
        );
        Common.currentlocation = helper;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("Current Location").child(auth.getCurrentUser().getUid())
                .setValue(helper).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getContext(), "Ubicación Agregada Exitosamente", Toast.LENGTH_SHORT).show();
                dismiss();
            }
            else{
                Toast.makeText(getContext(), "Ubicación no pudo ser guardada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_ACCESS_LOCATION);
            }
        }
    }


    @SuppressLint("MissingPermission")
    public void getDeviceLocation() {
        checkPermissions();
        if (mLocationPermissionGranted) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location1 -> {
                this.location = location1;
                moveCameraCurrentLocationOrDefault();
            });
        } else {
            moveCameraCurrentLocationOrDefault();
        }
    }



    private void moveCameraCurrentLocationOrDefault() {
        // Set the map's camera position to the current location of the device.
        Log.d(TAG, "Last know pos " + location);
        CameraPosition cameraPosition;
        if (location != null) {
            cameraPosition = new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 18, 0, 0);
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
        } else {
            cameraPosition = new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()),18,0,0);
        }
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
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
        getDeviceLocation();
        return true;
    }
}

