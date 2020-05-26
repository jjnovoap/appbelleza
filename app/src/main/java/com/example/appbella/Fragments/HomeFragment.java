package com.example.appbella.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appbella.Adapter.HomeFragmentsAdapter;
import com.example.appbella.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment  {

    private Unbinder unbinder;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this,view);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        // adapter
        HomeFragmentsAdapter adapter = new HomeFragmentsAdapter(getChildFragmentManager());
        //se pueden añadir fragmentos
        adapter.AddFragment(new ServicesFragment(), "Servicios");
        adapter.AddFragment(new ProductsFragment(),"Productos");
        //configuración adapter
        view_pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(view_pager);
        return view;

    }

}
