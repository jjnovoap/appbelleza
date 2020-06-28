package com.example.appbella;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.appbella.Adapter.MyOrderAdapter;

import com.example.appbella.Interface.ILoadMore;
import com.example.appbella.Interface.IOrderLoadListener;
import com.example.appbella.Model.Order;
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

public class ViewOrderActivity extends AppCompatActivity implements ILoadMore, IOrderLoadListener {

    private static final String TAG = ViewOrderActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_order)
    RecyclerView recycler_view_order;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    IOrderLoadListener iOrderLoadListener;
    private AlertDialog mDialog;

    private MyOrderAdapter mAdapter;
    private List<Order> mOrderList;
    private DatabaseReference setOrder;
    private int maxData = 0;

    private LayoutAnimationController mLayoutAnimationController;

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Log.d(TAG, "onCreate: started!!");

        toolbar.setTitle(getString(R.string.your_order));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_blue_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initView();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        setOrder = FirebaseDatabase.getInstance().getReference().child("Order").child(auth.getCurrentUser().getUid());
        iOrderLoadListener = this;

        getAllOrder();
        getMaxOrder();
    }

    private void getMaxOrder() {
        Log.d(TAG, "getMaxOrder: called!!");
        //mDialog.show();

        /*mCompositeDisposable.add(mIMyRestaurantAPI.getMaxOrder(Common.API_KEY,
                Common.currentUser.getFbid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(maxOrderModel -> {

                    if (maxOrderModel.isSuccess()) {
                        maxData = maxOrderModel.getResult().get(0).getMaxRowNum();
                        Log.d(TAG, "getMaxOrder: maxData: "+maxData);
                        mDialog.dismiss();

                        getAllOrder(0, 10);
                    }

                }, throwable -> {
                    mDialog.dismiss();
                    Toast.makeText(this, "[GET ORDER]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getAllOrder() {
        Log.d(TAG, "getAllOrder: called!!");

        setOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> orderList = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                        Order order = ds.getValue(Order.class);
                        orderList.add(order);
                        mDialog.dismiss();
                }iOrderLoadListener.onOrderLoadSuccess(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iOrderLoadListener.onOrderLoadFailed(databaseError.getMessage());
            }
        });

        //mDialog.show();

        /*mCompositeDisposable.add(mIMyRestaurantAPI.getOrder(Common.API_KEY,
                Common.currentUser.getFbid(), from, to)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(orderModel -> {

            if (orderModel.isSuccess()) {
                if (orderModel.getResult().size() > 0) {
                    if (mAdapter == null) {
                        mOrderList = (orderModel.getResult());

                        mOrderList = new ArrayList<>();
                        mAdapter = new MyOrderAdapter(this, mOrderList, recycler_view_order);
                        mAdapter.setILoadMore(this);
                        recycler_view_order.setAdapter(mAdapter);
                        recycler_view_order.setLayoutAnimation(mLayoutAnimationController);
                    }
                    else {
                        // Here we will remove null item after load done
                        // IF you don't remove it, loading view still available
                        mOrderList.remove(mOrderList.size()-1);
                        mOrderList = orderModel.getResult();
                        mAdapter.addItem(mOrderList);
                    }
                }
            }
            mDialog.dismiss();
        }, throwable -> {
            mDialog.dismiss();
            Toast.makeText(this, "[GET ORDER]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }));*/
    }

    private void initView() {
        Log.d(TAG, "initView: called!!");
        ButterKnife.bind(this);

        mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_item_from_left);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view_order.setLayoutManager(layoutManager);
        recycler_view_order.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

    }

    private void init() {
        Log.d(TAG, "init: called!!");
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
    }

    @Override
    public void onLoadMore() {
        // When loadmore being call
        // First, we will check data count with max data
        if (mAdapter.getItemCount() < maxData) {
            // Add null object to List to tell adapter known show loading state
            mOrderList.add(null);
            mAdapter.notifyItemInserted(mOrderList.size()-1);

            //getAllOrder(mAdapter.getItemCount()+1, mAdapter.getItemCount()+10);

            mAdapter.notifyDataSetChanged();
            mAdapter.setLoaded();
        }
        else {
            Toast.makeText(this, "Max Data to load", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOrderLoadSuccess(List<Order> orders) {
        mOrderList = new ArrayList<>();
        mAdapter = new MyOrderAdapter(this, orders, recycler_view_order);
        mAdapter.setILoadMore(this);
        recycler_view_order.setAdapter(mAdapter);
        recycler_view_order.setLayoutAnimation(mLayoutAnimationController);
    }

    @Override
    public void onOrderLoadFailed(String message) {

    }
}
