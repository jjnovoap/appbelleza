package com.example.appbella;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.appbella.Common.Common;
import com.example.appbella.Database.CartDataSource;
import com.example.appbella.Database.CartDatabase;
import com.example.appbella.Database.LocalCartDataSource;
import com.example.appbella.Model.EventBust.SendTotalCashEvent;
import com.example.appbella.Model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlaceOrderActivity extends AppCompatActivity{

    private static final String TAG = PlaceOrderActivity.class.getSimpleName();

    private static final int REQUEST_BRAINTREE_CODE = 7777;

    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.txt_total_cash)
    TextView txt_total_cash;
    @BindView(R.id.txt_tittle)
    TextView txt_tittle;
    @BindView(R.id.txt_etiqueta)
    TextView txt_etiqueta;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.rdi_cod)
    RadioButton rdi_cod;
    @BindView(R.id.rdi_online_payment)
    RadioButton rdi_online_payment;
    @BindView(R.id.btn_proceed)
    Button btn_proceed;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.numero_items)
    TextView numero_items;

    private DatabaseReference setorder;

    //private IBraintreeAPI mIBraintreeAPI;
    private CartDataSource mCartDataSource;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AlertDialog mDialog;
    private FirebaseAuth auth;

    private boolean isSelectedDate = false;
    private boolean isAddNewAddress = false;

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
        setContentView(R.layout.activity_place_order);
        Log.d(TAG, "onCreate: started!!");

        init();
        initView();

    }

    private void initView() {
        Log.d(TAG, "initView: called!!");
        ButterKnife.bind(this);

        txt_address.setText(Common.currentUser.getAddress());
        setorder = FirebaseDatabase.getInstance().getReference().child("Order");

        txt_tittle.setText(getString(R.string.place_order));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar();


        btn_proceed.setOnClickListener(v -> {
            if (!isSelectedDate) {
                Toast.makeText(this, "Please select Date", Toast.LENGTH_SHORT).show();
                String dateString = txt_date.getText().toString();
                return;
            }
           /*if (!isAddNewAddress) {
                if (!chb_default_address.isChecked()) {
                    Toast.makeText(this, "Please choose default Adress or set new address", Toast.LENGTH_SHORT).show();
                    return;
                }
            }*/
            if (rdi_cod.isChecked()) {
                getOrderNumber(false);
            }
            else if (rdi_online_payment.isChecked()) {
                getOrderNumber(true);
            }
        });
    }

    private void calendar() {
        DatePickerTimeline datePickerTimeline = findViewById(R.id.datePickerTimeline);
// Set a Start date (Default, 1 Jan 1970)
        Calendar now = Calendar.getInstance();

        datePickerTimeline.setInitialDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
        datePickerTimeline.setDateTextColor(getResources().getColor(R.color.colorPrimary));
        datePickerTimeline.setDayTextColor(getResources().getColor(R.color.colorPrimary));
        datePickerTimeline.setMonthTextColor(getResources().getColor(R.color.colorPrimary));
// Set a date Selected Listener
        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                // Do Something
                txt_date.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                // Do Something
            }
        });
    }

    private void getOrderNumber(boolean isOnlinePayment) {
        Log.d(TAG, "getOrderNumber: called!!");
        if (!isOnlinePayment) {
            String address = txt_address.getText().toString();

            mCompositeDisposable.add(mCartDataSource.getAllCart(Common.currentUser.getUserPhone())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(cartItems -> {

                        //generate a 4 digit integer 1000 <10000
                        String OrderId = String.valueOf((int)(Math.random()*9000)+1000);


                        FirebaseAuth auth = FirebaseAuth.getInstance();

                        Order order = new Order(OrderId,
                                0,
                                Common.currentServiceCategory.getId(),
                                Common.currentUser.getUserPhone(),
                                Common.currentUser.getName(),
                                address,
                                "NONE",
                                txt_date.getText().toString(),
                                true,
                                Double.valueOf(txt_total_cash.getText().toString()),
                                cartItems.size());

                        setorder.child(auth.getCurrentUser().getUid()).child(OrderId).setValue(order)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        mCartDataSource.cleanCart(Common.currentUser.getUserPhone())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new SingleObserver<Integer>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {

                                                    }

                                                    @Override
                                                    public void onSuccess(Integer integer) {
                                                        Toast.makeText(PlaceOrderActivity.this, "Order :Placed", Toast.LENGTH_SHORT).show();
                                                        Intent homeActivity = new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                                        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(homeActivity);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Toast.makeText(PlaceOrderActivity.this, "[CLEAR CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(e -> {
                            Toast.makeText(getApplication(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });


                        // Get Order Number from Server
                        //Código Único de Compra
                        /*SecureRandom random = new SecureRandom();
                        int OrderId = Integer.parseInt(new BigInteger(80, random).toString(36).toUpperCase());

                        FirebaseAuth auth = FirebaseAuth.getInstance();

                        Order order = new Order(OrderId,
                                0,
                                Common.currentRestaurant.getId(),
                                Common.currentUser.getUserPhone(),
                                Common.currentUser.getName(),
                                address,
                                "NONE",
                                currentDate,
                                true,
                                Double.valueOf(txt_total_cash.getText().toString()),
                                cartItems.size());

                        Map<String,Object> datos = new HashMap<>();
                        datos.put("orderId", OrderId);
                        datos.put("orderStatus", 0);
                        datos.put("restaurantId", Common.currentRestaurant.getId());
                        datos.put("orderPhone", Common.currentUser.getUserPhone());
                        datos.put("orderName", Common.currentUser.getName());
                        datos.put("orderAddress", address);
                        datos.put("transactionId", "NONE");
                        datos.put("orderDate",currentDate);
                        datos.put("cod", true);
                        datos.put("totalPrice", txt_total_cash.getText().toString());
                        datos.put("numOfItem",cartItems.size());


                        mCartDataSource.cleanCart(Common.currentUser.getFbid(),
                                Common.currentRestaurant.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        setorder.child(auth.getCurrentUser().getUid()).child(String.valueOf(OrderId)).setValue(datos)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplication(), "todo bien", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(e -> {
                                            Toast.makeText(getApplication(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                        Toast.makeText(PlaceOrderActivity.this, "Order :Placed", Toast.LENGTH_SHORT).show();
                                        Intent homeActivity = new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(homeActivity);
                                        finish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(PlaceOrderActivity.this, "[CLEAR CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                        mCompositeDisposable.add(mIMyRestaurantAPI.createOrder(
                                Common.currentUser.getFbid(),
                                Common.currentUser.getUserPhone(),
                                Common.currentUser.getName(),
                                address,
                                edt_date.getText().toString(),
                                Common.currentRestaurant.getId(),
                                "NONE",
                                true,
                                Double.valueOf(txt_total_cash.getText().toString()),
                                cartItems.size())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(createOrderModel -> {
                                    if (createOrderModel.isSuccess()) {
                                        // After have order number, we will update all item of this order to order Detail
                                        // First, select Cart items
                                        mCompositeDisposable.add(mIMyRestaurantAPI.updateOrder(Common.API_KEY,
                                                String.valueOf(createOrderModel.getResult().get(0).getOrderNumber()),
                                                new Gson().toJson(cartItems))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(updateOrderModel -> {

                                                    if (updateOrderModel.isSuccess()) {
                                                        // After update item, we will clear cart and show message success
                                                        mCartDataSource.cleanCart(Common.currentUser.getFbid(),
                                                                Common.currentRestaurant.getId())
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(new SingleObserver<Integer>() {
                                                                    @Override
                                                                    public void onSubscribe(Disposable d) {

                                                                    }

                                                                    @Override
                                                                    public void onSuccess(Integer integer) {
                                                                        Toast.makeText(PlaceOrderActivity.this, "Order :Placed", Toast.LENGTH_SHORT).show();
                                                                        Intent homeActivity = new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                                                        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(homeActivity);
                                                                        finish();
                                                                    }

                                                                    @Override
                                                                    public void onError(Throwable e) {
                                                                        Toast.makeText(PlaceOrderActivity.this, "[CLEAR CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }

                                                    if (mDialog.isShowing())
                                                        mDialog.dismiss();

                                                }, throwable -> {
                                                    mDialog.show();
                                                    Toast.makeText(this, "[UPDATE ORDER]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                }));
                                    } else {
                                        mDialog.dismiss();
                                        Toast.makeText(this, "[CREATE ORDER]" + createOrderModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, throwable -> {
                                    mDialog.dismiss();
                                    Toast.makeText(this, "[CREATE ORDER]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }));*/
                    }, throwable -> {
                        Toast.makeText(this, "[GET ALL CART]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }));
        }
        // If online payment, First get token
        else {
            /*mCompositeDisposable.add(mIBraintreeAPI.getToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(braintreeToken -> {

                if (braintreeToken.isSuccess()) {
                    DropInRequest dropInRequest = new DropInRequest().clientToken(braintreeToken.getClientToken());
                    startActivityForResult(dropInRequest.getIntent(PlaceOrderActivity.this), REQUEST_BRAINTREE_CODE);

                }
                else {
                    Toast.makeText(this, "Cannot get Token", Toast.LENGTH_SHORT).show();
                }
                
                mDialog.dismiss();

            }, throwable -> {
                mDialog.dismiss();
                Toast.makeText(this, "[GET TOKEN]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }));*/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BRAINTREE_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();

                // After have nonce, we just made a payment with API
                if (!TextUtils.isEmpty(txt_total_cash.getText().toString())) {
                    String amount = txt_total_cash.getText().toString();

                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }


                    /*mCompositeDisposable.add(mIBraintreeAPI.submitPayment(amount, nonce.getNonce())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(braintreeTransaction -> {

                        if (braintreeTransaction.isSuccess()) {
                            if (!mDialog.isShowing()) mDialog.show();

                            // After we have transaction, just make order like COD payment
                            mCompositeDisposable.add(mCartDataSource.getAllCart(Common.currentUser.getFbid(),
                                    Common.currentRestaurant.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(cartItems -> {
                                        // Get Order Number from Server
                                        mCompositeDisposable.add(mIMyRestaurantAPI.createOrder(Common.API_KEY,
                                                Common.currentUser.getFbid(),
                                                Common.currentUser.getUserPhone(),
                                                Common.currentUser.getName(),
                                                address,
                                                edt_date.getText().toString(),
                                                Common.currentRestaurant.getId(),
                                                braintreeTransaction.getTransaction().getId(),
                                                false,
                                                Double.valueOf(amount),
                                                cartItems.size())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(createOrderModel -> {
                                                    if (createOrderModel.isSuccess()) {
                                                        // After have order number, we will update all item of this order to order Detail
                                                        // First, select Cart items
                                                        mCompositeDisposable.add(mIMyRestaurantAPI.updateOrder(Common.API_KEY,
                                                                String.valueOf(createOrderModel.getResult().get(0).getOrderNumber()),
                                                                new Gson().toJson(cartItems))
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(updateOrderModel -> {

                                                                    if (updateOrderModel.isSuccess()) {
                                                                        // After update item, we will clear cart and show message success
                                                                        mCartDataSource.cleanCart(Common.currentUser.getFbid(),
                                                                                Common.currentRestaurant.getId())
                                                                                .subscribeOn(Schedulers.io())
                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                .subscribe(new SingleObserver<Integer>() {
                                                                                    @Override
                                                                                    public void onSubscribe(Disposable d) {

                                                                                    }

                                                                                    @Override
                                                                                    public void onSuccess(Integer integer) {
                                                                                        Toast.makeText(PlaceOrderActivity.this, "Order :Placed", Toast.LENGTH_SHORT).show();
                                                                                        Intent homeActivity = new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                                                                        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(homeActivity);
                                                                                        finish();
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(Throwable e) {
                                                                                        Toast.makeText(PlaceOrderActivity.this, "[CLEAR CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                    }

                                                                    if (mDialog.isShowing()) mDialog.dismiss();
                                                                }, throwable -> {
                                                                    mDialog.show();
                                                                    Toast.makeText(PlaceOrderActivity.this, "[UPDATE ORDER]", Toast.LENGTH_SHORT).show();
                                                                }));
                                                    } else {
                                                        mDialog.dismiss();
                                                        Toast.makeText(PlaceOrderActivity.this, "[CREATE ORDER]" + createOrderModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }, throwable -> {
                                                    mDialog.dismiss();
                                                    Toast.makeText(PlaceOrderActivity.this, "[CREATE ORDER]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                }));
                                    }, throwable -> {
                                        Toast.makeText(PlaceOrderActivity.this, "[GET ALL CART]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }));
                        }
                        else {
                            mDialog.dismiss();
                        }


                    }, throwable -> {
                        if (mDialog.isShowing()) mDialog.dismiss();
                        Toast.makeText(PlaceOrderActivity.this, "[SUBMIT PAYMENT]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }));*/
                }
            }
        }
    }

    private void init() {
        Log.d(TAG, "init: called!!");
        //mIBraintreeAPI = RetrofitBraintreeClient.getInstance(Common.currentRestaurant.getPaymentUrl()).create(IBraintreeAPI.class);
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        mCartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());
        auth = FirebaseAuth.getInstance();
        mCartDataSource.sumQuantity(auth.getCurrentUser().getPhoneNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        numero_items.setText(new StringBuilder(String.valueOf(aLong))
                                .append(" Ítems"));

                    }
                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage().contains("Query returned empty")){
                            numero_items.setText(new StringBuilder("0")
                                    .append(" Ítems"));

                        }
                        else
                            Toast.makeText(PlaceOrderActivity.this, "[SUM CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
    public void setTotalCash(SendTotalCashEvent event) {
        txt_total_cash.setText(new StringBuilder("$ ")
                .append(event.getCash()));
    }
}
