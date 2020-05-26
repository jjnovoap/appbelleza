// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private ProductAdapter.MyViewHolder target;

  @UiThread
  public ProductAdapter$MyViewHolder_ViewBinding(ProductAdapter.MyViewHolder target, View source) {
    this.target = target;

    target.img_food = Utils.findRequiredViewAsType(source, R.id.img_food, "field 'img_food'", ImageView.class);
    target.img_fav = Utils.findRequiredViewAsType(source, R.id.img_fav, "field 'img_fav'", ImageView.class);
    target.txt_quantity = Utils.findRequiredViewAsType(source, R.id.txt_quantity, "field 'txt_quantity'", TextView.class);
    target.txt_product_and_service_name = Utils.findRequiredViewAsType(source, R.id.txt_product_and_service_name, "field 'txt_product_and_service_name'", TextView.class);
    target.txt_product_and_service_price = Utils.findRequiredViewAsType(source, R.id.txt_product_and_service_price, "field 'txt_product_and_service_price'", TextView.class);
    target.const_detail = Utils.findRequiredViewAsType(source, R.id.const_detail, "field 'const_detail'", ConstraintLayout.class);
    target.img_add_cart = Utils.findRequiredViewAsType(source, R.id.img_cart, "field 'img_add_cart'", ImageView.class);
    target.img_delete_food = Utils.findRequiredViewAsType(source, R.id.img_delete_food, "field 'img_delete_food'", ImageView.class);
    target.img_decrease = Utils.findRequiredViewAsType(source, R.id.img_decrease, "field 'img_decrease'", ImageView.class);
    target.img_increase = Utils.findRequiredViewAsType(source, R.id.img_increase, "field 'img_increase'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_food = null;
    target.img_fav = null;
    target.txt_quantity = null;
    target.txt_product_and_service_name = null;
    target.txt_product_and_service_price = null;
    target.const_detail = null;
    target.img_add_cart = null;
    target.img_delete_food = null;
    target.img_decrease = null;
    target.img_increase = null;
  }
}
