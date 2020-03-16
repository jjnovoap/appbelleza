package com.example.appbella;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.MyProductOrServiceAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Fragments.MapsBottomDialogFragment;
import com.example.appbella.Interface.IGeneralCategoriesLoadListener;
import com.example.appbella.Model.CategoryProductOrServices;
import com.example.appbella.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IGeneralCategoriesLoadListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private TextView txt_user_name;
    private TextView txt_address;

    FirebaseFirestore userRef;
    DatabaseReference categoryRef;
    private MyProductOrServiceAdapter mAdapter;

    MapsBottomDialogFragment BottomDialogFragmen;
    private DatabaseReference locationRef;
    IGeneralCategoriesLoadListener iGeneralCategoriesLoadListener;
    @BindView(R.id.img_user)
    ImageView img_user;
    @BindView(R.id.recycler_catalogo)
    RecyclerView recycler_catalogo;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    //private LayoutAnimationController mLayoutAnimationController;

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);
        Log.d(TAG, "onCreate: started!!");

        userRef = FirebaseFirestore.getInstance();
        categoryRef = FirebaseDatabase.getInstance().getReference("General Categories");

        iGeneralCategoriesLoadListener = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        img_user.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        txt_user_name = headerView.findViewById(R.id.txt_user_name);
        txt_address = findViewById(R.id.txt_address);

        txt_address.setOnClickListener(v -> showBottomSheet());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Common.user = auth.getCurrentUser().getUid();
        locationRef = FirebaseDatabase.getInstance().getReference().child("Current Location").child(auth.getCurrentUser().getUid());

        BottomDialogFragmen = MapsBottomDialogFragment.newInstance();

        init();
        initView();
        
        loadCatalogo();
        setUserInformation();
    }

    private void setUserInformation() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userRef.collection("User").document(auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(
                        documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String username = documentSnapshot.getString("name");
                                String userPhone = documentSnapshot.getString("userPhone");
                                txt_user_name.setText(username);
                                Common.currentUser = documentSnapshot.toObject(User.class);
                            }
                        }).addOnFailureListener(e ->
                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadCatalogo() {
        Log.d(TAG, "loadCatalago: called!!");

        categoryRef.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: dataSnapshot1 "+dataSnapshot1.getKey()+" = "+dataSnapshot1.getValue());
                }

                List<CategoryProductOrServices> categories = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CategoryProductOrServices category = ds.getValue(CategoryProductOrServices.class);
                    categories.add(category);
                    Common.currentCategoryProductOrServices = ds.getValue(CategoryProductOrServices.class);
                }
                iGeneralCategoriesLoadListener.onCategoriesLoadSuccess(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iGeneralCategoriesLoadListener.onCategoriesLoadFailed(databaseError.getMessage());
            }
        });
    }

    private void initView() {
        Log.d(TAG, "initView: called!!");
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        // This code will select item view type
        // If item is last, it will set full width on Grid layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter != null) {
                    switch (mAdapter.getItemViewType(position)) {
                        case Common.DEFAULT_COLUMN_COUNT:
                            return 1;
                        case Common.FULL_WIDTH_COLUMN:
                            return 2;
                        default:
                            return -1;
                    }
                } else {
                    return -1;
                }
            }
        });
        recycler_catalogo.setLayoutManager(layoutManager);
        recycler_catalogo.setHasFixedSize(true);
        setAddress();
        //mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_item_from_left);
    }


    private void setAddress() {
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                    if (latitude != null && longitude != null)
                        txt_address.setText(getAddress(latitude,longitude));
                }else
                if(!BottomDialogFragmen.isAdded()) {
                    BottomDialogFragmen.show(getSupportFragmentManager(), BottomDialogFragmen.getTag());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error: "+databaseError.getMessage());
            }
        });

    }

    public void showBottomSheet() {
        if (BottomDialogFragmen == null)
            BottomDialogFragmen = new MapsBottomDialogFragment();
        if(!BottomDialogFragmen.isAdded()) {
            BottomDialogFragmen.show(getSupportFragmentManager(), BottomDialogFragmen.getTag());
        }
    }

    private String getAddress(double lat, double lng) {
        String address=null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

    private void init() {
        Log.d(TAG, "init: called!!");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            signOut();
        } else if (id == R.id.nav_nearby) {
            startActivity(new Intent(HomeActivity.this, NearbyRestaurantActivity.class));
        } else if (id == R.id.nav_order_history) {
            startActivity(new Intent(HomeActivity.this, ViewOrderActivity.class));
        } else if (id == R.id.nav_update_info) {
            startActivity(new Intent(HomeActivity.this, UpdateInfoActivity.class));
        } else if (id == R.id.nav_fav) {
            startActivity(new Intent(HomeActivity.this, FavoriteActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        Log.d(TAG, "signOut: called!!");
        // Here we will made alert dialog to confirm
        AlertDialog confirmDialog = new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Do you really want to sign out?")
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("OK", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Common.currentUser = null;
                    Common.currentCategoryProductOrServices = null;
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    // Clear all previous activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }).create();

        confirmDialog.show();
    }


    @Override
    public void onCategoriesLoadSuccess(List<CategoryProductOrServices> categoryProductOrServicesList) {
        Log.d(TAG, "displayRestaurant: called!!");
        mAdapter = new MyProductOrServiceAdapter(this, categoryProductOrServicesList);
        recycler_catalogo.setAdapter(mAdapter);
        //recycler_catalogo.setLayoutAnimation(mLayoutAnimationController);
        Log.d(TAG, "displayBanner: called!!");
        Log.d(TAG, "displayBanner: size: "+categoryProductOrServicesList.size());
    }

    @Override
    public void onCategoriesLoadFailed(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
}
