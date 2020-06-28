package com.example.appbella;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appbella.Common.Common;
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
import io.reactivex.disposables.CompositeDisposable;

public class PlaceOrderActivity extends AppCompatActivity{

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
    @BindView(R.id.btn_proceed)
    Button btn_proceed;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.numero_items)
    TextView numero_items;

    private DatabaseReference setorder;

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

        init();
        initView();

    }

    private void initView() {
        ButterKnife.bind(this);

        txt_address.setText(Common.currentUser.getAddress());
        setorder = FirebaseDatabase.getInstance().getReference().child("Order");
        txt_tittle.setText(getString(R.string.place_order));

        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_blue_24);
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
            if (rdi_cod.isChecked()) {
                getOrderNumber(false);
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
        if (!isOnlinePayment) {
            String address = txt_address.getText().toString();

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
                    0);

            setorder.child(auth.getCurrentUser().getUid()).child(OrderId).setValue(order)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PlaceOrderActivity.this, "Order :Placed", Toast.LENGTH_SHORT).show();
                            Intent homeActivity = new Intent(PlaceOrderActivity.this, HomeActivity.class);
                            homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeActivity);
                            finish();
                        }
                    }).addOnFailureListener(e -> {
                Toast.makeText(getApplication(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // After have nonce, we just made a payment with API
        if (!TextUtils.isEmpty(txt_total_cash.getText().toString())) {
            String amount = txt_total_cash.getText().toString();

            if (!mDialog.isShowing()) {
                mDialog.show();
            }

        }
    }

    private void init() {
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        auth = FirebaseAuth.getInstance();
        //numero_items.setText(new StringBuilder("0").append(" Ítems"));
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
