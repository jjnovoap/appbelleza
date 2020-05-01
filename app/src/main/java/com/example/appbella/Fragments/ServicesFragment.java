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

import com.example.appbella.Adapter.CategoryAdapter;
import com.example.appbella.Adapter.SubcategoryAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.ICategoryLoadListener;
import com.example.appbella.Interface.ISubcategoryLoadListener;
import com.example.appbella.Model.EventBust.SubcategoryEvent;
import com.example.appbella.Model.Subcategory;
import com.example.appbella.Model.Category;
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

public class ServicesFragment extends Fragment implements ICategoryLoadListener, ISubcategoryLoadListener/*, IServicesOrProductLoadListener */ {

    public ServicesFragment() {
        // Required empty public constructor
    }

    private DatabaseReference subcategoryRef;
    Unbinder unbinder;
    @BindView(R.id.recycler_category)
    RecyclerView recycler_category;
    @BindView(R.id.recycler_subcategory)
    RecyclerView recycler_subcategory;
    private ICategoryLoadListener iCategoryLoadListener;
    private ISubcategoryLoadListener ISubcategoryLoadListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        unbinder = ButterKnife.bind(this, view);

        ISubcategoryLoadListener = this;
        iCategoryLoadListener = this;

        subcategoryRef = FirebaseDatabase.getInstance().getReference("Services Categories");
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("General Categories");

        categoryRef.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Category> categories = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category category = ds.getValue(Category.class);
                    categories.add(category);
                    Common.currentCategory = ds.getValue(Category.class);
                }
                iCategoryLoadListener.onCategoriesLoadSuccess(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iCategoryLoadListener.onCategoriesLoadFailed(databaseError.getMessage());
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
    public void loadSubacategoryByCategory(SubcategoryEvent event) {

        if (event.isSuccess()) {

            subcategoryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Subcategory> subcategoryList = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String id = String.valueOf(event.getProductOrServices().getId());
                        String key = ds.child("id").getValue(String.class);
                        if (id.equals(key)){
                            Subcategory subcategory = ds.getValue(Subcategory.class);
                            subcategoryList.add(subcategory);
                        }
                    }
                    ISubcategoryLoadListener.onSubcategoryLoadSuccess(subcategoryList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    ISubcategoryLoadListener.onSubcategoryLoadFailed(databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onSubcategoryLoadSuccess(List<Subcategory> subcategoryList) {
        SubcategoryAdapter adapter = new SubcategoryAdapter(getContext(), subcategoryList);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_subcategory.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        // This code will select item view type
        // If item is last, it will set full width on Grid layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter != null) {
                    switch (adapter.getItemViewType(position)) {
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
        recycler_subcategory.setHasFixedSize(true);
        recycler_subcategory.setLayoutManager(layoutManager);
    }

    @Override
    public void onSubcategoryLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoriesLoadSuccess(List<Category> categoryList) {
        CategoryAdapter mAdapter = new CategoryAdapter(getContext(), categoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_category.setAdapter(mAdapter);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(layoutManager);
    }

    @Override
    public void onCategoriesLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
}
