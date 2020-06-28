package com.example.appbella.Adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbella.Common.Common;
import com.example.appbella.Model.AddToCart;
import com.example.appbella.Model.EventBust.CalculatePriceEvent;
import com.example.appbella.Model.Favorite;
import com.example.appbella.Model.Product;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> implements Filterable {

    private static final String TAG = "";
    private Context mContext;
    private List<Product> mProductList;
    private List<Product> mProductListFull;
    private long mQuantity;
    private DatabaseReference favorite;
    private DatabaseReference product;
    private DatabaseReference addtoCart;


    public ProductAdapter(Context context, List<Product> productList) {
        mContext = context;
        mProductList = productList;
        mProductListFull=new ArrayList<>();
        mProductListFull.addAll(productList);
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_product, parent, false);
        favorite = FirebaseDatabase.getInstance().getReference("Favorites");
        addtoCart = FirebaseDatabase.getInstance().getReference("Cart");
        product = FirebaseDatabase.getInstance().getReference("Products");
        return new ProductAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {

        final Product productDataItem = mProductList.get(position);

        holder.setIsRecyclable(false);

        Picasso.get().load(productDataItem.getImage()).placeholder(R.drawable.app_icon).into(holder.img_product);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        holder.txt_product_and_service_name.setText(productDataItem.getName());
        holder.txt_product_and_service_price.setText(new StringBuilder(mContext.getString(R.string.money_sign)).append(" ").append(productDataItem.getPrice()));

        DatabaseReference inAddtoCart = FirebaseDatabase.getInstance().getReference("Cart").child(auth.getCurrentUser().getUid()).child(productDataItem.getName());

        /*DatabaseReference getCart = FirebaseDatabase.getInstance().getReference("Cart").child(auth.getCurrentUser().getUid());

        getCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //int count = 0;
                //badge.setText("0");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    //Log.d(TAG, "onCreate: started!!"+name);

                        mQuantity = (long) ds.child("productQuantity").getValue();
                        if (mQuantity == 0) {
                            holder.card_add.setVisibility(View.VISIBLE);
                        } else {
                            holder.txt_quantity.setText(String.valueOf(mQuantity));
                            holder.card_add.setVisibility(View.GONE);
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });*/

        inAddtoCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue(String.class);
                    if (productDataItem.getName().equals(name)) {
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

        if (productDataItem.getStatus().equals("1")) {
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
            addtoCart.child(auth.getCurrentUser().getUid()).child(productDataItem.getName()).updateChildren(update);
        });

        holder.img_decrease.setOnClickListener(v -> {
            long q = Long.parseLong(holder.txt_quantity.getText().toString());
            if (q > 0) {
                q -= 1;
                holder.txt_quantity.setText(String.valueOf(q));
                Map<String, Object> update = new HashMap<>();
                update.put("productQuantity", q);
                addtoCart.child(auth.getCurrentUser().getUid()).child(productDataItem.getName()).updateChildren(update);
            }
            if (q == 0){
                addtoCart.child(auth.getCurrentUser().getUid()).child(productDataItem.getName()).removeValue().addOnCompleteListener(task -> {
                    // añadir sonido como en rappi
                    EventBus.getDefault().postSticky(new CalculatePriceEvent(0));
                    holder.txt_quantity.setText("1");
                }).addOnFailureListener(e ->
                        Toast.makeText(mContext, "Error al eliminar producto", Toast.LENGTH_SHORT).show());
            }
        });

        holder.img_add_cart.setOnClickListener(v -> {
            long ads = 0;
            AddToCart add = new AddToCart(
                    productDataItem.getId(),
                    productDataItem.getCategoryId(),
                    productDataItem.getKey(),
                    productDataItem.getName(),
                    productDataItem.getImage(),
                    productDataItem.getPrice(),
                    Long.valueOf(holder.txt_quantity.getText().toString()),
                    ads,
                    false,
                    ads,
                    "1");

            addtoCart.child(auth.getCurrentUser().getUid()).child(productDataItem.getName()).setValue(add)
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
            if (productDataItem.getStatus().equals("1")) {
                favorite.child(auth.getCurrentUser().getUid()).child(productDataItem.getName()).removeValue().addOnCompleteListener(task -> {
                    holder.img_fav.setImageResource(R.drawable.ic_favorite_border_button_color_24dp);
                    holder.img_fav.setTag(false);
                    Map<String, Object> status = new HashMap<>();
                    status.put("status", "0");
                    product.child(productDataItem.getKey()).updateChildren(status);
                }).addOnFailureListener(e ->
                        Toast.makeText(mContext, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show());
            } else {
                Favorite favoriteServices = new Favorite(auth.getCurrentUser().getUid(),
                        "",
                        productDataItem.getName(),
                        productDataItem.getImage(),
                        "0",
                        Integer.parseInt(productDataItem.getId()),
                        1,
                        productDataItem.getPrice());

                favorite.child(auth.getCurrentUser().getUid()).child(productDataItem.getName()).setValue(favoriteServices)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                holder.img_fav.setImageResource(R.drawable.ic_favorite_button_color_24dp);
                                holder.img_fav.setTag(true);
                                Map<String, Object> status = new HashMap<>();
                                status.put("status", "1");
                                product.child(productDataItem.getKey()).updateChildren(status);
                                Common.currentFavorite = favoriteServices;
                                Toast.makeText(mContext, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                                /*mContext.startActivity(new Intent(mContext, Favorite.class));
                                EventBus.getDefault().postSticky(new ProductDetailEvent(true, mProductList.get(position)));*/

                            }
                        }).addOnFailureListener(e -> {
                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }


        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    @Override
    public Filter getFilter() {
        return productDataFilter;
    }

    private Filter productDataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(mProductListFull);
            }else {

                String filter=constraint.toString().toLowerCase().trim();

                for(Product dataItem:mProductListFull){
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
            mProductList.clear();
            mProductList.addAll((Collection<? extends Product>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_product)
        ImageView img_product;
        @BindView(R.id.img_fav)
        ImageView img_fav;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_product_and_service_name)
        TextView txt_product_and_service_name;
        @BindView(R.id.txt_product_and_service_price)
        TextView txt_product_and_service_price;
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
