package com.example.appbella;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.appbella.Adapter.MyCategoryAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Database.CartDataSource;
import com.example.appbella.Database.CartDatabase;
import com.example.appbella.Database.LocalCartDataSource;
import com.example.appbella.Interface.ISpecificCategoryLoadListener;
import com.example.appbella.Model.Category;
import com.example.appbella.Model.EventBust.MenuItemEvent;
import com.example.appbella.Retrofit.IMyRestaurantAPI;
import com.example.appbella.Retrofit.RetrofitClient;
import com.example.appbella.Utils.SpaceItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MenuActivity extends AppCompatActivity implements ISpecificCategoryLoadListener {

    private static final String TAG = MenuActivity.class.getSimpleName();

    @BindView(R.id.recycler_category)
    RecyclerView recycler_category;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton btn_cart;
    @BindView(R.id.badge)
    NotificationBadge badge;

    private IMyRestaurantAPI mIMyRestaurantAPI;
    ISpecificCategoryLoadListener iSpecificCategoryLoadListener;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    DatabaseReference categoriesServicesRef;
    private android.app.AlertDialog mDialog;
    private DatabaseReference favorite;


    private MyCategoryAdapter mAdapter;
    private CartDataSource mCartDataSource;

    private LayoutAnimationController mLayoutAnimationController;

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartByRestaurant();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d(TAG, "onCreate: started!!");

        categoriesServicesRef = FirebaseDatabase.getInstance().getReference("Services Categories");
        iSpecificCategoryLoadListener = this;
        favorite = FirebaseDatabase.getInstance().getReference().child("Favorites");

        init();
        initView();

        countCartByRestaurant();

    }

    private void loadFavoriteByRestaurant(int id) {
        Log.d(TAG, "loadFavoriteByRestaurant: called!!");
        /*FirebaseAuth auth = FirebaseAuth.getInstance();
        favorite.child(auth.getCurrentUser().getUid()).child(String.valueOf(id)).getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("id").getValue(String.class);
                    List<FavoriteOnlyId> list = new ArrayList<>();
                    if (ds.exists()){
                        Common.currentFavOfRestaurant = list;
                    }else {
                        Common.currentFavOfRestaurant = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*mCompositeDisposable.add(mIMyRestaurantAPI.getFavoriteByRestaurant(Common.API_KEY,
                Common.currentUser.getFbid(), Common.currentRestaurant.getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(favoriteOnlyIdModel -> {

            if (favoriteOnlyIdModel.isSuccess()) {
                if (favoriteOnlyIdModel.getResult() != null && favoriteOnlyIdModel.getResult().size() > 0) {
                    Common.currentFavOfRestaurant = favoriteOnlyIdModel.getResult();
                }
                else {
                    Common.currentFavOfRestaurant = new ArrayList<>();
                }
            }
            else {
                Toast.makeText(this, "[GET FAVORITE]"+favoriteOnlyIdModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }, throwable -> {
            Toast.makeText(this, "[GET FAVORITE]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }));*/


    }

    private void countCartByRestaurant() {
        Log.d(TAG, "countCartByRestaurant: called!!");
        mCartDataSource.countItemInCart(Common.currentUser.getFbid(),
                Common.currentCategoryProductOrServices.getId())
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
                        Toast.makeText(MenuActivity.this, "[COUNT CART}"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

        btn_cart.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, CartListActivity.class));
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
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
        recycler_category.setLayoutManager(layoutManager);
        recycler_category.addItemDecoration(new SpaceItemDecoration(8));
    }

    private void init() {
        Log.d(TAG, "init: called!!");
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        mIMyRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT)
                .create(IMyRestaurantAPI.class);

        mCartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());
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
    public void loadMenuByRestaurant(MenuItemEvent event) {

        loadFavoriteByRestaurant(event.getProductOrServices().getId());
        if (event.isSuccess()) {
            toolbar.setTitle(event.getProductOrServices().getName());

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            categoriesServicesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Category> categoryList = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        int id = event.getProductOrServices().getId();
                        int key = ds.child("id").getValue(Integer.class);
                        if (id == key){
                            Category category = ds.getValue(Category.class);
                            categoryList.add(category);
                        }
                    }iSpecificCategoryLoadListener.onCategoryLoadSuccess(categoryList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    iSpecificCategoryLoadListener.onCategoryLoadFailed(databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onCategoryLoadSuccess(List<Category> categoryList) {
        mAdapter = new MyCategoryAdapter(MenuActivity.this, categoryList);
        recycler_category.setAdapter(mAdapter);
        recycler_category.setLayoutAnimation(mLayoutAnimationController);
    }

    @Override
    public void onCategoryLoadFailed(String message) {

    }
}
