package com.example.appbella.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.CategoryProductAdapter;
import com.example.appbella.Adapter.SubcategoryProductAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IProductCategoryLoadListener;
import com.example.appbella.Interface.IProductSubcategoryLoadListener;
import com.example.appbella.Model.ProductCategory;
import com.example.appbella.Model.EventBust.ProductSubcategoryEvent;
import com.example.appbella.Model.ProductSubcategory;
import com.example.appbella.R;
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
import butterknife.Unbinder;

public class ProductsFragment extends Fragment implements IProductCategoryLoadListener, IProductSubcategoryLoadListener {

    public ProductsFragment() {
        // Required empty public constructor
    }
    private DatabaseReference subcategoryRef;
    Unbinder unbinder;
    @BindView(R.id.recycler_category)
    RecyclerView recycler_category;
    @BindView(R.id.recycler_subcategory)
    RecyclerView recycler_subcategory;
    private IProductCategoryLoadListener iProductCategoryLoadListener;
    private IProductSubcategoryLoadListener iProductSubcategoryLoadListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        unbinder = ButterKnife.bind(this,view);

        iProductSubcategoryLoadListener = this;
        iProductCategoryLoadListener = this;

        subcategoryRef = FirebaseDatabase.getInstance().getReference("Products Categories");
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("General Products Categories");

        categoryRef.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<ProductCategory> productCategories = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ProductCategory productCategory = ds.getValue(ProductCategory.class);
                    productCategories.add(productCategory);
                    Common.currentProductCategory = ds.getValue(ProductCategory.class);
                }
                iProductCategoryLoadListener.onProductCategoriesLoadSuccess(productCategories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iProductCategoryLoadListener.onProductCategoriesLoadFailed(databaseError.getMessage());
            }
        });
        return view;
    }

    /**
     * REGISTER EVENT BUS
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Listen EventBus
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void loadSubacategoryByCategory(ProductSubcategoryEvent event) {

        if (event.isSuccess()) {
            subcategoryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<ProductSubcategory> productSubcategoryList = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String id = String.valueOf(event.getProduct().getId());
                        String key = ds.child("id").getValue(String.class);
                        if (id.equals(key)){
                            ProductSubcategory productSubcategory = ds.getValue(ProductSubcategory.class);
                            productSubcategoryList.add(productSubcategory);
                        }
                    }
                    iProductSubcategoryLoadListener.onProductSubcategoryLoadSuccess(productSubcategoryList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    iProductSubcategoryLoadListener.onProductSubcategoryLoadFailed(databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onProductSubcategoryLoadSuccess(List<ProductSubcategory> productSubcategoryList) {
        SubcategoryProductAdapter adapter = new SubcategoryProductAdapter(getContext(), productSubcategoryList);
        recycler_subcategory.setAdapter(adapter);
        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recycler_subcategory.setHasFixedSize(true);
        recycler_subcategory.setLayoutManager(layoutManager);
    }

    @Override
    public void onProductSubcategoryLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductCategoriesLoadSuccess(List<ProductCategory> productCategoryList) {
        CategoryProductAdapter mAdapter = new CategoryProductAdapter(getContext(), productCategoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_category.setAdapter(mAdapter);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(layoutManager);
    }

    @Override
    public void onProductCategoriesLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
}
