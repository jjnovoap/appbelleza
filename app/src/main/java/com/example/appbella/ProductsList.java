package com.example.appbella;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.ProductAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IProductLoadListener;
import com.example.appbella.Model.EventBust.ProductsListEvent;
import com.example.appbella.Model.Product;
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

public class ProductsList extends AppCompatActivity implements IProductLoadListener{

    @BindView(R.id.recycler_proserv_list)
    RecyclerView recycler_proserv_list;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_subcategory)
    TextView txt_subcategory;
    Button btn_view_cart;

    private IProductLoadListener iProductLoadListener;

    private ProductAdapter adapter;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_and_service_list);
        ButterKnife.bind(this);
        iProductLoadListener = this;

        btn_view_cart = findViewById(R.id.btn_view_cart);

        productRef = FirebaseDatabase.getInstance().getReference("Products");

        btn_view_cart.setOnClickListener(v -> {
            startActivity(new Intent(ProductsList.this, CartListActivity.class));
        });

        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Restore to original adapter when use close Search
                recycler_proserv_list.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Restore to original adapter when use close Search
                recycler_proserv_list.setAdapter(adapter);
                return true;
            }
        });

        return true;
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

    // Listen EventBus
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void loadProductListByCategory(ProductsListEvent event) {
        if (event.isSuccess()) {

            txt_subcategory.setText(event.getProductSubcategory().getName());
            toolbar.setTitle("");
            /*toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
            Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/gilroybold.ttf");
            ((TextView) toolbar.getChildAt(0)).setTypeface(typeFace);
            ((TextView) toolbar.getChildAt(0)).setTextSize(16);*/
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            productRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Product> productList = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.child("categoryId").getValue(String.class);
                        if (event.getProductSubcategory().getCategoryId().equals(name)) {
                            Product product = ds.getValue(Product.class);
                            productList.add(product);
                            Common.currentProduct = product;
                        }
                    }
                    iProductLoadListener.onProductLoadSuccess(productList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    iProductLoadListener.onProductLoadFailed(databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onProductLoadSuccess(List<Product> ProductList) {
        adapter = new ProductAdapter(this, ProductList);
        recycler_proserv_list.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler_proserv_list.setLayoutManager(layoutManager);
    }

    @Override
    public void onProductLoadFailed(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }
}