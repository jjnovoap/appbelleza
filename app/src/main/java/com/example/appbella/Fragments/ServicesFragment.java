package com.example.appbella.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Adapter.CategoryServiceAdapter;
import com.example.appbella.Adapter.SubcategoryServiceAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IServiceCategoryLoadListener;
import com.example.appbella.Interface.IServiceSubcategoryLoadListener;
import com.example.appbella.Model.EventBust.ServiceSubcategoryEvent;
import com.example.appbella.Model.ServiceSubcategory;
import com.example.appbella.Model.ServiceCategory;
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

public class ServicesFragment extends Fragment implements IServiceCategoryLoadListener, IServiceSubcategoryLoadListener/*, IServicesOrProductLoadListener */ {

    public ServicesFragment() {
        // Required empty public constructor
    }

    private DatabaseReference subcategoryRef;
    Unbinder unbinder;
    @BindView(R.id.recycler_category)
    RecyclerView recycler_category;
    @BindView(R.id.recycler_subcategory)
    RecyclerView recycler_subcategory;
    private IServiceCategoryLoadListener iServiceCategoryLoadListener;
    private IServiceSubcategoryLoadListener IServiceSubcategoryLoadListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        unbinder = ButterKnife.bind(this, view);

        IServiceSubcategoryLoadListener = this;
        iServiceCategoryLoadListener = this;

        subcategoryRef = FirebaseDatabase.getInstance().getReference("Services Categories");
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("General Services Categories");

        categoryRef.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<ServiceCategory> categories = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ServiceCategory serviceCategory = ds.getValue(ServiceCategory.class);
                    categories.add(serviceCategory);
                    Common.currentServiceCategory = ds.getValue(ServiceCategory.class);
                }
                iServiceCategoryLoadListener.onCategoriesLoadSuccess(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iServiceCategoryLoadListener.onCategoriesLoadFailed(databaseError.getMessage());
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
    public void loadSubacategoryByCategory(ServiceSubcategoryEvent event) {

        if (event.isSuccess()) {
            subcategoryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<ServiceSubcategory> serviceSubcategoryList = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String id = String.valueOf(event.getServices().getId());
                        String key = ds.child("id").getValue(String.class);
                        if (id.equals(key)){
                            ServiceSubcategory serviceSubcategory = ds.getValue(ServiceSubcategory.class);
                            serviceSubcategoryList.add(serviceSubcategory);
                        }
                    }
                    IServiceSubcategoryLoadListener.onServiceSubcategoryLoadSuccess(serviceSubcategoryList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    IServiceSubcategoryLoadListener.onServiceSubcategoryLoadFailed(databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onServiceSubcategoryLoadSuccess(List<ServiceSubcategory> serviceSubcategoryList) {
        SubcategoryServiceAdapter adapter = new SubcategoryServiceAdapter(getContext(), serviceSubcategoryList);
        recycler_subcategory.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_subcategory.setHasFixedSize(true);
        recycler_subcategory.setLayoutManager(layoutManager);
    }

    @Override
    public void onServiceSubcategoryLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoriesLoadSuccess(List<ServiceCategory> serviceCategoryList) {
        CategoryServiceAdapter mAdapter = new CategoryServiceAdapter(getContext(), serviceCategoryList);
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
