package com.example.appbella;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appbella.Adapter.MyFavoriteAdapter;
import com.example.appbella.Common.Common;
import com.example.appbella.Interface.IFavoriteLoadListener;
import com.example.appbella.Model.Favorite;
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
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

public class FavoriteActivity extends AppCompatActivity implements IFavoriteLoadListener {

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    private DatabaseReference FavoriteRef;
    private IFavoriteLoadListener iFavoriteLoadListener;
    private MyFavoriteAdapter adapter;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private android.app.AlertDialog mDialog;

    @BindView(R.id.recycler_fav)
    RecyclerView recycler_fav;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MyFavoriteAdapter mAdapter;

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        if (mAdapter != null) {
            mAdapter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Log.d(TAG, "onCreate: started!!");

        FavoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
        iFavoriteLoadListener = this;

        init();
        initView();
        loadFavoriteItems();
    }

    private void loadFavoriteItems() {
        Log.d(TAG, "loadFavoriteItems: called!!");
        mDialog.show();

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
                    mDialog.dismiss();
                }
                iFavoriteLoadListener.onFavoriteLoadSuccess(favoriteList);
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFavoriteLoadListener.onFavoriteLoadFailed(databaseError.getMessage());
                mDialog.dismiss();
            }
        });
        /*mCompositeDisposable.add(mIMyRestaurantAPI.getFavoriteByUser(Common.API_KEY, Common.currentUser.getFbid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteModel -> {

                    if (favoriteModel.isSuccess()) {
                        mAdapter = new MyFavoriteAdapter(FavoriteActivity.this, favoriteModel.getResult());
                        recycler_fav.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(this, "[GET FAV RESULT]"+favoriteModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    mDialog.dismiss();

                }, throwable -> {
                    mDialog.dismiss();
                    Toast.makeText(this, "[GET FAV]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));*/
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

    private void initView() {
        Log.d(TAG, "initView: called!!");
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_fav.setLayoutManager(layoutManager);
        recycler_fav.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        toolbar.setTitle(getString(R.string.menu_fav));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        Log.d(TAG, "init: called!!");
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
    }

    @Override
    public void onFavoriteLoadSuccess(List<Favorite> favoriteList) {
        adapter = new MyFavoriteAdapter(this,favoriteList);
        recycler_fav.setAdapter(adapter);
        recycler_fav.setHasFixedSize(true);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onFavoriteLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
