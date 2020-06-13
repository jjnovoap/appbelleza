package com.example.appbella;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.MyAddonAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Database.CartDataSource;
import com.example.appbella.Database.CartDatabase;
import com.example.appbella.Database.CartItem;
import com.example.appbella.Database.LocalCartDataSource;
import com.example.appbella.Interface.IAddonLoadListener;
import com.example.appbella.Model.Addon;
import com.example.appbella.Model.EventBust.AddOnEventChange;
import com.example.appbella.Model.EventBust.ServiceDetailEvent;
import com.example.appbella.Model.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductAndServiceDetailActivity extends AppCompatActivity implements IAddonLoadListener {

    private static final String TAG = ProductAndServiceDetailActivity.class.getSimpleName();

    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.txt_money)
    TextView txt_money;
    @BindView(R.id.txt_service)
    TextView txt_service;
    @BindView(R.id.recycler_addon)
    RecyclerView recycler_addon;
    @BindView(R.id.txt_description)
    TextView txt_description;
    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private CartDataSource mCartDataSource;
    DatabaseReference addonRef;
    IAddonLoadListener iAddonLoadListener;

    private Service selectedFood;
    private Long originalPrice;

    private long addOnPrice = 0;
    private long extraPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_and_service_detail);
        Log.d(TAG, "onCreate: started!!");

        addonRef = FirebaseDatabase.getInstance().getReference("Addon");
        iAddonLoadListener = this;
        init();
        initView();
    }

    private void initView() {
        Log.d(TAG, "initView: called!!");
        ButterKnife.bind(this);

        btn_add.setOnClickListener(v -> {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(selectedFood.getName());
            cartItem.setCategoryId(selectedFood.getCategoryId());
            cartItem.setProductName(selectedFood.getName());
            cartItem.setProductPrice((selectedFood.getPrice()+extraPrice));
            cartItem.setProductImage(selectedFood.getImage());
            cartItem.setProductQuantity(1);
            cartItem.setUserPhone(Common.currentUser.getUserPhone());
            cartItem.setProductAddon(new Gson().toJson(Common.addonList));
            cartItem.setProductExtraPrice(extraPrice);
            cartItem.setFbid(Common.currentUser.getFbid());

            mCompositeDisposable.add(mCartDataSource.insertOrReplaceAll(cartItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }, throwable -> {
                        Toast.makeText(this, "[ADD CART]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }));
        });
    }

    private void init() {
        Log.d(TAG, "init: called!!");
        mCartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void displayFoodDetail(ServiceDetailEvent event) {
        Log.d(TAG, "displayFoodDetail: called!!");
        Log.d(TAG, "displayFoodDetail: Name: " + event.getService().getName());
        if (event.isSuccess()) {

            txt_service.setText(event.getService().getName());
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            selectedFood = event.getService();
            originalPrice = event.getService().getPrice();

            txt_money.setText(new StringBuilder(this.getString(R.string.money_sign))
                    .append(" ").append(originalPrice));
            txt_description.setText(event.getService().getDescription());
            Picasso.get().load(event.getService().getImage()).into(header);

            addonRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Addon> addonList = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Addon addon = ds.getValue(Addon.class);
                        addonList.add(addon);
                        Common.currentAddon = ds.getValue(Addon.class);
                    }
                    iAddonLoadListener.onAddonLoadSuccess(addonList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    iAddonLoadListener.onAddonLoadFailed(databaseError.getMessage());
                }
                });


        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void priceChange(AddOnEventChange eventChange) {
        Log.d(TAG, "priceChange: called!!");
        if (eventChange.isAdd()) {
            addOnPrice += eventChange.getAddon().getExtraPrice();
        } else {
            addOnPrice -= eventChange.getAddon().getExtraPrice();
        }
        calculatePrice();
    }

    private void calculatePrice() {
        Log.d(TAG, "calculatePrice: called!!");
        extraPrice = 0;
        long newPrice;

        extraPrice += addOnPrice;

        newPrice = originalPrice + extraPrice;

        txt_money.setText(new StringBuilder(this.getString(R.string.money_sign))
                .append(" ").append(newPrice));
    }

    @Override
    public void onAddonLoadSuccess(List<Addon> addonList) {
        recycler_addon.setHasFixedSize(true);
        recycler_addon.setLayoutManager(new LinearLayoutManager(this));
        recycler_addon.setAdapter(new MyAddonAdapter(ProductAndServiceDetailActivity.this, addonList));
    }

    @Override
    public void onAddonLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
