package com.example.appbella.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Interface.IOnImageViewAdapterClickListener;
import com.example.appbella.Model.AddToCart;

import com.example.appbella.Model.EventBust.CalculatePriceEvent;
import com.example.appbella.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    private Context mContext;
    private static final String TAG = "";
    private List<AddToCart> mAddToCartList;
    private DatabaseReference addtoCart;
    private FirebaseAuth auth;
    private int total=0;
    private long mQuantity;

    public MyCartAdapter(Context context, List<AddToCart> addToCartList) {
        mContext = context;
        mAddToCartList = addToCartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mAddToCartList.get(position).getImage()).into(holder.img_proserv);
        holder.txt_proserv_name.setText(mAddToCartList.get(position).getName());
        holder.txt_proserv_price.setText(String.valueOf(mAddToCartList.get(position).getPrice()));
        holder.txt_quantity.setText(String.valueOf(mAddToCartList.get(position).getProductQuantity()));
        holder.txt_items_add.setText(new StringBuilder("Adiciones: ")
                .append(mAddToCartList.get(position).getProductExtraPrice() / 1000));
        holder.txt_extra_price.setText(new StringBuilder("Precio Extra: $ ")
                .append(mAddToCartList.get(position).getProductExtraPrice()));

        addtoCart = FirebaseDatabase.getInstance().getReference("Cart");
        DatabaseReference inAddtoCart = FirebaseDatabase.getInstance().getReference("Cart").child(auth.getCurrentUser().getUid()).child(mAddToCartList.get(position).getName());

        inAddtoCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue(String.class);
                    if (mAddToCartList.get(position).getName().equals(name)) {
                        mQuantity = (long) dataSnapshot.child("productQuantity").getValue();
                        holder.txt_quantity.setText(String.valueOf(mQuantity));
                        total+=mAddToCartList.get(position).getPrice()*mQuantity;
                        if(position==mAddToCartList.size()-1){
                            EventBus.getDefault().postSticky(new CalculatePriceEvent(total));
                        }
                    }else{
                        total+=mAddToCartList.get(position).getPrice();
                        if(position==mAddToCartList.size()-1){
                            EventBus.getDefault().postSticky(new CalculatePriceEvent(total));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        holder.txt_price_new.setText(String.valueOf(mAddToCartList.get(position).getPrice()* Long.parseLong(holder.txt_quantity.getText().toString())));

        // Event
        holder.setIOnImageViewAdapterClickListener((view, position1, isDecrease, isDelete) -> {
            //if (!isDelete) {
                // If decrease quantity
                if (isDecrease) {
                    long q = Long.parseLong(holder.txt_quantity.getText().toString());
                    if (q > 0) {
                        q -= 1;
                        holder.txt_quantity.setText(String.valueOf(q));
                        Map<String, Object> update = new HashMap<>();
                        update.put("productQuantity", q);
                        addtoCart.child(auth.getCurrentUser().getUid()).child(mAddToCartList.get(position).getName()).updateChildren(update);
                    }
                }
                // If increase quantity
                else {
                    long q = Long.parseLong(holder.txt_quantity.getText().toString());
                    if (mAddToCartList.get(position1).getProductQuantity() < 99) {
                        q += 1;
                        holder.txt_quantity.setText(String.valueOf(q));
                        Map<String, Object> update = new HashMap<>();
                        update.put("productQuantity", q);
                        addtoCart.child(auth.getCurrentUser().getUid()).child(mAddToCartList.get(position).getName()).updateChildren(update);
                    }
                }
            //}
        });

        long q = Long.parseLong(holder.txt_quantity.getText().toString());
        if (q == 0){
            addtoCart.child(auth.getCurrentUser().getUid()).child(mAddToCartList.get(position).getName()).removeValue().addOnCompleteListener(task -> {
                // añadir sonido como en rappi
                //holder.txt_quantity.setText("1");
            }).addOnFailureListener(e ->
                    Toast.makeText(mContext, "Error al eliminar producto", Toast.LENGTH_SHORT).show());
        }
    }




    @Override
    public int getItemCount() {
        return mAddToCartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_price_new)
        TextView txt_price_new;
        @BindView(R.id.txt_proserv_name)
        TextView txt_proserv_name;
        @BindView(R.id.txt_proserv_price)
        TextView txt_proserv_price;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_extra_price)
        TextView txt_extra_price;
        @BindView(R.id.txt_items_add)
        TextView txt_items_add;
        @BindView(R.id.img_proserv)
        ImageView img_proserv;
        @BindView(R.id.img_decrease)
        ImageView img_decrease;
        @BindView(R.id.img_increase)
        ImageView img_increase;

        IOnImageViewAdapterClickListener mIOnImageViewAdapterClickListener;

        public void setIOnImageViewAdapterClickListener(IOnImageViewAdapterClickListener IOnImageViewAdapterClickListener) {
            mIOnImageViewAdapterClickListener = IOnImageViewAdapterClickListener;
        }

        Unbinder mUnbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            auth = FirebaseAuth.getInstance();
            mUnbinder = ButterKnife.bind(this, itemView);
            img_decrease.setOnClickListener(this);
            img_increase.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == img_decrease) {
                mIOnImageViewAdapterClickListener.onCalculatePriceListener(v, getAdapterPosition(), true, false);
            } else if (v == img_increase) {
                mIOnImageViewAdapterClickListener.onCalculatePriceListener(v, getAdapterPosition(), false, true);
            }
        }
    }

}
