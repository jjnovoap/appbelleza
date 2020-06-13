package com.example.appbella;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.appbella.Common.Common;
import com.example.appbella.Database.CartDataSource;
import com.example.appbella.Database.CartDatabase;
import com.example.appbella.Database.LocalCartDataSource;
import com.example.appbella.Fragments.FavoriteFragment;
import com.example.appbella.Fragments.HomeFragment;
import com.example.appbella.Fragments.MapsBottomDialogFragment;
import com.example.appbella.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nex3z.notificationbadge.NotificationBadge;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private TextView txt_user_name;
    private TextView txt_address;
    private boolean isTransactionSafe;
    private boolean isTransactionPending;

    FirebaseFirestore userRef;
    DatabaseReference categoryRef;
    private long mLastClickTime = 0;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;

    MapsBottomDialogFragment BottomDialogFragmen;
    private DatabaseReference locationRef;
    @BindView(R.id.img_user)
    ImageView img_user;
    @BindView(R.id.fab)
    ImageView btn_cart;
    @BindView(R.id.badge)
    NotificationBadge badge;
    private CartDataSource mCartDataSource;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);
        Log.d(TAG, "onCreate: started!!");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout layout_profile = headerView.findViewById(R.id.layout_profile);
        LinearLayout layout_logout = headerView.findViewById(R.id.layout_logout);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Common.user = auth.getCurrentUser().getUid();
        locationRef = FirebaseDatabase.getInstance().getReference().child("Current Location").child(auth.getCurrentUser().getUid());
        userRef = FirebaseFirestore.getInstance();
        categoryRef = FirebaseDatabase.getInstance().getReference("General Categories");


        init();
        countCartItem();
        initView();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        img_user.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        });

        txt_user_name = headerView.findViewById(R.id.txt_user_name);
        txt_address = findViewById(R.id.txt_address);

        txt_address.setOnClickListener(v -> showBottomSheet());


        layout_profile.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(HomeActivity.this, UpdateInfoActivity.class);
            startActivity(intent);
        });


        layout_logout.setOnClickListener(v -> {
            signOut();
        });

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //add fragments
                if (menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if (menuItem.getItemId() == R.id.action_likes)
                    fragment = new FavoriteFragment();
                /*else if (menuItem.getItemId() == R.id.action_about_us_cuber)
                    fragment = new AboutUsFragment();
                else if (menuItem.getItemId() == R.id.action_shopping)
                    fragment = new ShoppingFragment();*/
                return loadFragment(fragment);
            }
        });


        BottomDialogFragmen = MapsBottomDialogFragment.newInstance();
        setUserInformation();

    }

    public void onPostResume() {
        super.onPostResume();
        isTransactionSafe = true;
    }
    public void onPause() {
        super.onPause();
        isTransactionSafe = false;
    }

    private boolean loadFragment(Fragment fragment) {
        if (isTransactionSafe){
            if (fragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                return true;
            }
        }else {
            isTransactionPending = true;
        }
        return false;
    }

    private void setUserInformation() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userRef.collection("User").document(auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(
                        documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String username = documentSnapshot.getString("name");
                                txt_user_name.setText(new StringBuilder("¡Hola ")
                                        .append(username).append("!"));
                                bottom_navigation.setSelectedItemId(R.id.action_home);
                                Common.currentUser = documentSnapshot.toObject(User.class);
                            }
                        }).addOnFailureListener(e ->
                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void initView() {
        ButterKnife.bind(this);

        btn_cart.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CartListActivity.class));
        });

        setAddress();
    }

    private void countCartItem() {
        Log.d(TAG, "countCartByRestaurant: called!!");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mCartDataSource.countItemInCart(auth.getCurrentUser().getPhoneNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        badge.setText(String.valueOf(integer));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(HomeActivity.this, "[COUNT CART}"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
        mCartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());
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
        /*int id = item.getItemId();

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
*/
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
                    Common.currentServiceCategory = null;
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    // Clear all previous activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }).create();

        confirmDialog.show();
    }

}
