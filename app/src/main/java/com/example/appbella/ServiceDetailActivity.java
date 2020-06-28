package com.example.appbella;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.example.appbella.Interface.IAddonLoadListener;
import com.example.appbella.Model.AddToCart;
import com.example.appbella.Model.Addon;
import com.example.appbella.Model.EventBust.AddOnEventChange;
import com.example.appbella.Model.EventBust.CalculatePriceEvent;
import com.example.appbella.Model.EventBust.ServiceDetailEvent;
import com.example.appbella.Model.Service;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDetailActivity extends AppCompatActivity implements IAddonLoadListener {

    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.txt_money)
    TextView txt_money;
    @BindView(R.id.txt_service)
    TextView txt_service;
    @BindView(R.id.txt_tittle_adds)
    TextView txt_tittle_adds;
    @BindView(R.id.recycler_addon)
    RecyclerView recycler_addon;
    @BindView(R.id.txt_description)
    TextView txt_description;
    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_quantity)
    TextView txt_quantity;
    @BindView(R.id.img_increase)
    ImageView img_increase;
    @BindView(R.id.img_decrease)
    ImageView img_decrease;

    private IAddonLoadListener iAddonLoadListener;

    private Service selectedFood;
    private Long originalPrice;
    private long addPrice;
    private DatabaseReference addtoCart;
    private FirebaseAuth auth;
    private long mQuantity;
    private long addOnPrice = 0;
    private long extraPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_and_service_detail);
        ButterKnife.bind(this);

        iAddonLoadListener = this;
        auth = FirebaseAuth.getInstance();
        addtoCart = FirebaseDatabase.getInstance().getReference("Cart");

        img_increase.setOnClickListener(v -> {
            long q = Long.parseLong(txt_quantity.getText().toString());
            q += 1;
            txt_quantity.setText(String.valueOf(q));
        });

        img_decrease.setOnClickListener(v -> {
            long q = Long.parseLong(txt_quantity.getText().toString());
            if (q > 0) {
                q -= 1;
                txt_quantity.setText(String.valueOf(q));
            }
        });

        EventBus.getDefault().register(this);
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
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void displayFoodDetail(ServiceDetailEvent event) {

        if (event.isSuccess()) {

            txt_service.setText(event.getService().getName());
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            selectedFood = event.getService();
            originalPrice = event.getService().getPrice();

            txt_money.setText(new StringBuilder(this.getString(R.string.money_sign))
                    .append(" ").append(originalPrice));
            txt_description.setText(event.getService().getDescription());
            Picasso.get().load(event.getService().getImage()).into(header);

            DatabaseReference inAddtoCart = FirebaseDatabase.getInstance().getReference("Cart").child(auth.getCurrentUser().getUid()).child(event.getService().getName());
            DatabaseReference addonRef = FirebaseDatabase.getInstance().getReference("Addon").child(event.getService().getName());

            inAddtoCart.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String name = dataSnapshot.child("name").getValue(String.class);
                        if (event.getService().getName().equals(name)) {
                            mQuantity = (long) dataSnapshot.child("productQuantity").getValue();
                            if (mQuantity == 0) {
                                txt_quantity.setText("1");
                                btn_add.setText("Agregar");
                            }else {
                                txt_quantity.setText(String.valueOf(mQuantity));
                                btn_add.setText("Actualizar");
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            addonRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Addon> addonList = new ArrayList<>();
                    if (dataSnapshot.exists()){
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Addon addon = ds.getValue(Addon.class);
                            addonList.add(addon);
                            Common.currentAddon = ds.getValue(Addon.class);
                            addToCart(event);
                        }
                        iAddonLoadListener.onAddonLoadSuccess(addonList);
                    }else{
                        txt_tittle_adds.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    iAddonLoadListener.onAddonLoadFailed(databaseError.getMessage());
                }
                });
        }
    }


    private void addToCart(ServiceDetailEvent event) {

        btn_add.setOnClickListener(v -> {

            long ads = 0;
            AddToCart add = new AddToCart(
                    event.getService().getId(),
                    event.getService().getCategoryId(),
                    event.getService().getKey(),
                    event.getService().getName(),
                    event.getService().getImage(),
                    event.getService().getPrice(),
                    Long.valueOf(txt_quantity.getText().toString()),
                    addPrice,
                    false,
                    ads,
                    "0");
            addtoCart.child(auth.getCurrentUser().getUid()).child(event.getService().getName()).setValue(add)
                    .addOnCompleteListener(task -> {
                        /*if (task.isSuccessful()) {
                            añadir sonido como en rappi
                            Toast.makeText(mContext, "Producto añadido", Toast.LENGTH_SHORT).show();
                        }*/
                    }).addOnFailureListener(e -> {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
            finish();
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void priceChange(AddOnEventChange eventChange) {
        if (eventChange.isAdd()) {
            addPrice = eventChange.getAddon().getExtraPrice();
            addOnPrice += eventChange.getAddon().getExtraPrice();
        } else {
            addOnPrice -= eventChange.getAddon().getExtraPrice();
        }
        calculatePrice();
    }

    private void calculatePrice() {
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
        recycler_addon.setAdapter(new MyAddonAdapter(ServiceDetailActivity.this, addonList));
    }

    @Override
    public void onAddonLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
