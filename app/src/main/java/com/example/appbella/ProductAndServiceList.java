package com.example.appbella;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.MyItemAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IServicesOrProductLoadListener;
import com.example.appbella.Model.Category;
import com.example.appbella.Model.EventBust.FoodListEvent;
import com.example.appbella.Model.Product_and_Service;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

public class ProductAndServiceList extends AppCompatActivity implements IServicesOrProductLoadListener {

    private static final String TAG = ProductAndServiceList.class.getSimpleName();

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private android.app.AlertDialog mDialog;

    @BindView(R.id.recycler_food_list)
    RecyclerView recycler_food_list;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    IServicesOrProductLoadListener iServicesOrProductLoadListener;

    private MyItemAdapter adapter;
    private MyItemAdapter searchAdapter;
    private Category selectedCategory;
    DatabaseReference servicesRef;

    private LayoutAnimationController mLayoutAnimationController;

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        if (adapter != null) {
            adapter.onStop();
        }
        if (searchAdapter != null) {
            searchAdapter.onStop();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_and_service_list);
        Log.d(TAG, "onCreate: started!!");

        servicesRef = FirebaseDatabase.getInstance().getReference("Services");



        iServicesOrProductLoadListener = this;

        init();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearchFood(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Restore to original adapter when use close Search
                recycler_food_list.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

    private void startSearchFood(String query) {
        Log.d(TAG, "startSearchFood: called!!");
        mDialog.show();
        /*mCompositeDisposable.add(mIMyRestaurantAPI.searchFood(Common.API_KEY, query, selectedCategory.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(foodModel -> {

                    if (foodModel.isSuccess()) {
                        searchAdapter = new MyFoodAdapter(FoodListActivity.this, foodModel.getResult());
                        recycler_food_list.setAdapter(searchAdapter);
                        recycler_food_list.setLayoutAnimation(mLayoutAnimationController);
                    } else {
                        if (foodModel.getMessage().contains("Empty")) {
                            recycler_food_list.setAdapter(null);
                            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    mDialog.dismiss();

                }, throwable -> {
                    Toast.makeText(FoodListActivity.this, "[SEARCH FOOD]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        Log.d(TAG, "initView: called!!");
        ButterKnife.bind(this);

        mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_item_from_left);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_food_list.setLayoutManager(layoutManager);
        recycler_food_list.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    private void init() {
        Log.d(TAG, "init: called!!");
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
    }

    /**
     * REGISTER EVENT BUS
     */
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Listen EventBus
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void loadFoodListByCategory(FoodListEvent event) {
        Log.d(TAG, "loadFoodListByCategory: called!!");
        if (event.isSuccess()) {

            selectedCategory = event.getCategory();

            toolbar.setTitle(event.getCategory().getName());
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
            Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/gilroybold.ttf");
            ((TextView) toolbar.getChildAt(0)).setTypeface(typeFace);
            ((TextView) toolbar.getChildAt(0)).setTextSize(16);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mDialog.show();

            servicesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Product_and_Service> serviceList = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String name = ds.child("categoryId").getValue(String.class);
                        if (event.getCategory().getCategoryId().equals(name)){
                            Product_and_Service service = ds.getValue(Product_and_Service.class);
                            serviceList.add(service);
                            Common.currentFood = service;
                            mDialog.dismiss();
                        }
                    }iServicesOrProductLoadListener.onServicesOProductLoadSuccess(serviceList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    iServicesOrProductLoadListener.onServicesOProductLoadFailed(databaseError.getMessage());
                }
            });

        } else {
            mDialog.dismiss();
        }
    }

    @Override
    public void onServicesOProductLoadSuccess(List<Product_and_Service> servicesOrProductList) {
        adapter = new MyItemAdapter(this, servicesOrProductList);
        recycler_food_list.setAdapter(adapter);
        recycler_food_list.setLayoutAnimation(mLayoutAnimationController);
        mDialog.dismiss();
    }

    @Override
    public void onServicesOProductLoadFailed(String message) {

    }
}
