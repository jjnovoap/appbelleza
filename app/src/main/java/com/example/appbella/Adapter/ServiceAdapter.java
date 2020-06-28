package com.example.appbella.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Common.Common;
import com.example.appbella.Model.AddToCart;
import com.example.appbella.Model.EventBust.CalculatePriceEvent;
import com.example.appbella.Model.EventBust.ServiceDetailEvent;
import com.example.appbella.Model.Favorite;
import com.example.appbella.Model.Product;
import com.example.appbella.Model.Service;
import com.example.appbella.ServiceDetailActivity;
import com.example.appbella.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> implements Filterable {

    private static final String TAG = "";
    private Context mContext;
    private List<Service> mServiceList;
    private List<Service> mServiceListFull;
    private long mQuantity;
    private DatabaseReference favorite, productAndService, addtoCart;


    public ServiceAdapter(Context context, List<Service> serviceList) {
        mContext = context;
        mServiceList = serviceList;
        mServiceListFull=new ArrayList<>();
        mServiceListFull.addAll(serviceList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_service, parent, false);
        favorite = FirebaseDatabase.getInstance().getReference().child("Favorites");
        addtoCart = FirebaseDatabase.getInstance().getReference("Cart");
        productAndService = FirebaseDatabase.getInstance().getReference("Services");
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Service serviceDataItem = mServiceList.get(position);

        holder.setIsRecyclable(false);

        Picasso.get().load(serviceDataItem.getImage()).placeholder(R.drawable.app_icon).into(holder.img_service);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        holder.txt_product_and_service_name.setText(serviceDataItem.getName());
        holder.txt_product_and_service_price.setText(new StringBuilder(mContext.getString(R.string.money_sign)).append(" ").append(serviceDataItem.getPrice()));

        DatabaseReference inAddtoCart = FirebaseDatabase.getInstance().getReference("Cart").child(auth.getCurrentUser().getUid()).child(serviceDataItem.getName());

        inAddtoCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue(String.class);
                    if (serviceDataItem.getName().equals(name)) {
                        mQuantity = (long) dataSnapshot.child("productQuantity").getValue();
                        if (mQuantity == 0) {
                            holder.card_add.setVisibility(View.VISIBLE);
                        } else {
                            holder.txt_quantity.setText(String.valueOf(mQuantity));
                            holder.card_add.setVisibility(View.GONE);
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (serviceDataItem.getStatus().equals("1")) {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_button_color_24dp);
        } else {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_border_button_color_24dp);
        }

        holder.img_increase.setOnClickListener(v -> {
            long q = Long.parseLong(holder.txt_quantity.getText().toString());
            q += 1;
            holder.txt_quantity.setText(String.valueOf(q));
            Map<String, Object> update = new HashMap<>();
            update.put("productQuantity", q);
            addtoCart.child(auth.getCurrentUser().getUid()).child(serviceDataItem.getName()).updateChildren(update);
        });

        holder.img_decrease.setOnClickListener(v -> {
            long q = Long.parseLong(holder.txt_quantity.getText().toString());
            if (q > 0) {
                q -= 1;
                holder.txt_quantity.setText(String.valueOf(q));
                Map<String, Object> update = new HashMap<>();
                update.put("productQuantity", q);
                addtoCart.child(auth.getCurrentUser().getUid()).child(serviceDataItem.getName()).updateChildren(update);
            }
            if (q == 0) {
                addtoCart.child(auth.getCurrentUser().getUid()).child(serviceDataItem.getName()).removeValue().addOnCompleteListener(task -> {
                    // añadir sonido como en rappi
                    EventBus.getDefault().postSticky(new CalculatePriceEvent(0));
                    holder.txt_quantity.setText("1");
                }).addOnFailureListener(e ->
                        Toast.makeText(mContext, "Error al eliminar producto", Toast.LENGTH_SHORT).show());
            }
        });


        holder.lay_detail.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, ServiceDetailActivity.class));
            EventBus.getDefault().postSticky(new ServiceDetailEvent(true, serviceDataItem));
        });

        holder.img_add_cart.setOnClickListener(v -> {
            long ads = 0;
            AddToCart add = new AddToCart(
                    serviceDataItem.getId(),
                    serviceDataItem.getCategoryId(),
                    serviceDataItem.getKey(),
                    serviceDataItem.getName(),
                    serviceDataItem.getImage(),
                    serviceDataItem.getPrice(),
                    Long.valueOf(holder.txt_quantity.getText().toString()),
                    ads,
                    false,
                    ads,
                    "0");

            addtoCart.child(auth.getCurrentUser().getUid()).child(serviceDataItem.getName()).setValue(add)
                    .addOnCompleteListener(task -> {
                        /*if (task.isSuccessful()) {
                            añadir sonido como en rappi
                            Toast.makeText(mContext, "Producto añadido", Toast.LENGTH_SHORT).show();
                        }*/
                    }).addOnFailureListener(e -> {
                Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        // Event
        holder.img_fav.setOnClickListener(v -> {
            if (serviceDataItem.getStatus().equals("1")) {
                favorite.child(auth.getCurrentUser().getUid()).child(serviceDataItem.getName()).removeValue().addOnCompleteListener(task -> {
                    holder.img_fav.setImageResource(R.drawable.ic_favorite_border_button_color_24dp);
                    holder.img_fav.setTag(false);
                    Map<String, Object> status = new HashMap<>();
                    status.put("status", "0");
                    productAndService.child(serviceDataItem.getKey()).updateChildren(status);
                }).addOnFailureListener(e ->
                        Toast.makeText(mContext, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show());
            } else {
                Favorite favoriteServices = new Favorite(auth.getCurrentUser().getUid(),
                        "",
                        serviceDataItem.getName(),
                        serviceDataItem.getImage(),
                        "0",
                        Integer.parseInt(serviceDataItem.getId()),
                        1,
                        serviceDataItem.getPrice());

                favorite.child(auth.getCurrentUser().getUid()).child(serviceDataItem.getName()).setValue(favoriteServices)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                holder.img_fav.setImageResource(R.drawable.ic_favorite_button_color_24dp);
                                holder.img_fav.setTag(true);
                                Map<String, Object> status = new HashMap<>();
                                status.put("status", "1");
                                productAndService.child(serviceDataItem.getKey()).updateChildren(status);
                                Common.currentFavorite = favoriteServices;
                                Toast.makeText(mContext, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    @Override
    public Filter getFilter() {
        return serviceDataFilter;
    }

    private Filter serviceDataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Service> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(mServiceListFull);
            }else {

                String filter=constraint.toString().toLowerCase().trim();

                for(Service dataItem:mServiceListFull){
                    if(dataItem.getName().toLowerCase().contains(filter)){
                        filteredList.add(dataItem);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mServiceList.clear();
            mServiceList.addAll((Collection<? extends Service>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_service)
        ImageView img_service;
        @BindView(R.id.img_fav)
        ImageView img_fav;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_product_and_service_name)
        TextView txt_product_and_service_name;
        @BindView(R.id.txt_product_and_service_price)
        TextView txt_product_and_service_price;
        @BindView(R.id.lay_detail)
        ConstraintLayout lay_detail;
        @BindView(R.id.img_cart)
        ImageView img_add_cart;
        @BindView(R.id.card_add)
        CardView card_add;
        @BindView(R.id.img_decrease)
        ImageView img_decrease;
        @BindView(R.id.img_increase)
        ImageView img_increase;

        Unbinder mUnbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mUnbinder = ButterKnife.bind(this, itemView);
        }

    }
}
