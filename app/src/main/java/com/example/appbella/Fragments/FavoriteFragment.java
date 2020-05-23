package com.example.appbella.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.appbella.Adapter.MyFavoriteAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IFavoriteLoadListener;
import com.example.appbella.Model.Favorite;
import com.example.appbella.R;
import com.google.firebase.auth.FirebaseAuth;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements IFavoriteLoadListener{

    private DatabaseReference FavoriteRef;
    private IFavoriteLoadListener iFavoriteLoadListener;
    private MyFavoriteAdapter adapter;
    private Unbinder unbinder;

    @BindView(R.id.recycler_fav)
    RecyclerView recycler_fav;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this,view);

        FavoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
        iFavoriteLoadListener = this;

        initView();
        loadFavoriteItems();

        return view;
    }

    private void loadFavoriteItems() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FavoriteRef.child(auth.getCurrentUser().getUid()).getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Favorite> favoriteList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Favorite favorite = ds.getValue(Favorite.class);
                    favoriteList.add(favorite);
                    //If user already available in our system
                    Common.currentFavorite = ds.getValue(Favorite.class);
                }
                iFavoriteLoadListener.onFavoriteLoadSuccess(favoriteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFavoriteLoadListener.onFavoriteLoadFailed(databaseError.getMessage());
            }
        });
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_fav.setLayoutManager(layoutManager);
        recycler_fav.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    @Override
    public void onFavoriteLoadSuccess(List<Favorite> favoriteList) {
        adapter = new MyFavoriteAdapter(getContext(),favoriteList);
        recycler_fav.setAdapter(adapter);
        recycler_fav.setHasFixedSize(true);
        recycler_fav.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onFavoriteLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
