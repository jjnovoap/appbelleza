package com.example.appbella;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.MyCartAdapter;
import com.example.appbella.Interface.IAddProductToCartLoadListener;
import com.example.appbella.Model.AddToCart;
import com.example.appbella.Model.EventBust.CalculatePriceEvent;
import com.example.appbella.Model.EventBust.SendTotalCashEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class CartListActivity extends AppCompatActivity implements IAddProductToCartLoadListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.txt_final_price)
    TextView txt_final_price;
    @BindView(R.id.btn_order)
    Button btn_order;
    @BindView(R.id.txt_empty_cart)
    TextView txt_empty_cart;
    @BindView(R.id.numero_items)
    TextView numero_items;
    private IAddProductToCartLoadListener iAddProductToCartLoadListener;
    private DatabaseReference addtoCart;


    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        addtoCart = FirebaseDatabase.getInstance().getReference("Cart").child(auth.getCurrentUser().getUid());
        DatabaseReference getCart = FirebaseDatabase.getInstance().getReference("Cart").child(auth.getCurrentUser().getUid());
        iAddProductToCartLoadListener = this;

        initView();
        getAllItemInCart();
        emptycart();
        itemsCounter(getCart);
    }

    private void itemsCounter(DatabaseReference getCart) {

        getCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int count = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        long quantity = (long) ds.child("productQuantity").getValue();
                        count = (int) (count + quantity);
                        numero_items.setText(new StringBuilder(String.valueOf(count))
                                .append(" Ítems"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartListActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllItemInCart() {
        addtoCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AddToCart> productAddList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AddToCart add = ds.getValue(AddToCart.class);
                    productAddList.add(add);
                }
                iAddProductToCartLoadListener.onAddProductToCartLoadSuccess(productAddList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iAddProductToCartLoadListener.onAddProductToCartLoadFailed(databaseError.getMessage());
            }
        });

    }

    private void emptycart() {
        txt_empty_cart.setOnClickListener(v -> {
            addtoCart.removeValue();
            EventBus.getDefault().postSticky(new CalculatePriceEvent(0));
        });
    }

    private void initView() {
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);

        if (txt_final_price.getText().toString().equals("0")){
            btn_order.setText(getString(R.string.empty_cart));
            btn_order.setEnabled(false);
            btn_order.setBackgroundResource(R.drawable.border_buttom_unselected);
        }else{
            btn_order.setText(getString(R.string.place_order));
            btn_order.setEnabled(true);
            btn_order.setBackgroundResource(R.drawable.border_button);
        }

        btn_order.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new SendTotalCashEvent(txt_final_price.getText().toString()));
            startActivity(new Intent(CartListActivity.this, PlaceOrderActivity.class));
        });
    }

    // Event Bus

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
    public void calculatePrice(CalculatePriceEvent event) {
        txt_final_price.setText(String.valueOf(event.getTotalPrice()));
        if (event.getTotalPrice() == 0 && txt_final_price.getText().toString().equals("0")){
            btn_order.setText(getString(R.string.empty_cart));
            btn_order.setEnabled(false);
            btn_order.setBackgroundResource(R.drawable.border_buttom_unselected);
        }else{
            btn_order.setText(getString(R.string.place_order));
            btn_order.setEnabled(true);
            btn_order.setBackgroundResource(R.drawable.border_button);
        }

    }

    @Override
    public void onAddProductToCartLoadSuccess(List<AddToCart> addToCartList) {
        MyCartAdapter adapter = new MyCartAdapter(this, addToCartList);
        recycler_cart.setAdapter(adapter);
    }

    @Override
    public void onAddProductToCartLoadFailed(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
}
