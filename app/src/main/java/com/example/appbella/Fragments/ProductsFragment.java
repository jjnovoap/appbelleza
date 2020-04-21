package com.example.appbella.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.SubcategoryAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IProductCategoryLoadListener;
import com.example.appbella.Model.Subcategory;
import com.example.appbella.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProductsFragment extends Fragment implements IProductCategoryLoadListener {

    public ProductsFragment() {
        // Required empty public constructor
    }
    private Unbinder unbinder;
    private SubcategoryAdapter Adapter;
    @BindView(R.id.recycler_category)
    RecyclerView recycler_category;
    private IProductCategoryLoadListener iProductCategoryLoadListener;
    private DatabaseReference categoriesServicesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        unbinder = ButterKnife.bind(this,view);
        iProductCategoryLoadListener = this;

        categoriesServicesRef = FirebaseDatabase.getInstance().getReference("Services Categories");


        GridLayoutManager layoutMan = new GridLayoutManager(getContext(), 1);
        layoutMan.setOrientation(RecyclerView.HORIZONTAL);
        // This code will select item view type
        // If item is last, it will set full width on Grid layout
        layoutMan.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (Adapter != null) {
                    switch (Adapter.getItemViewType(position)) {
                        case Common.DEFAULT_COLUMN_COUNT:
                            return 1;
                        case Common.FULL_WIDTH_COLUMN:
                            return 1;
                        default:
                            return -1;
                    }
                } else {
                    return -1;
                }
            }
        });
        recycler_category.setLayoutManager(layoutMan);
        recycler_category.setHasFixedSize(true);

        categoriesServicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Subcategory> subcategoryList = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String id = String.valueOf(2);
                    String key = ds.child("id").getValue(String.class);
                    if (id.equals(key)){
                        Subcategory subcategory = ds.getValue(Subcategory.class);
                        subcategoryList.add(subcategory);
                    }
                }
                iProductCategoryLoadListener.onProductCategoryLoadSuccess(subcategoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iProductCategoryLoadListener.onProductCategoryLoadFailed(databaseError.getMessage());
            }
        });

        return view;
    }

    @Override
    public void onProductCategoryLoadSuccess(List<Subcategory> subcategoryList) {
        Adapter = new SubcategoryAdapter(getContext(), subcategoryList);
        recycler_category.setAdapter(Adapter);
    }

    @Override
    public void onProductCategoryLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
}
