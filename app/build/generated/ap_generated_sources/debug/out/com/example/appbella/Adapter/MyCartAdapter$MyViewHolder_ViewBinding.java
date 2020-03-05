// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyCartAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private MyCartAdapter.MyViewHolder target;

  @UiThread
  public MyCartAdapter$MyViewHolder_ViewBinding(MyCartAdapter.MyViewHolder target, View source) {
    this.target = target;

    target.txt_price_new = Utils.findRequiredViewAsType(source, R.id.txt_price_new, "field 'txt_price_new'", TextView.class);
    target.txt_food_name = Utils.findRequiredViewAsType(source, R.id.txt_food_name, "field 'txt_food_name'", TextView.class);
    target.txt_food_price = Utils.findRequiredViewAsType(source, R.id.txt_food_price, "field 'txt_food_price'", TextView.class);
    target.txt_quantity = Utils.findRequiredViewAsType(source, R.id.txt_quantity, "field 'txt_quantity'", TextView.class);
    target.txt_extra_price = Utils.findRequiredViewAsType(source, R.id.txt_extra_price, "field 'txt_extra_price'", TextView.class);
    target.img_food = Utils.findRequiredViewAsType(source, R.id.img_food, "field 'img_food'", ImageView.class);
    target.img_delete_food = Utils.findRequiredViewAsType(source, R.id.img_delete_food, "field 'img_delete_food'", ImageView.class);
    target.img_decrease = Utils.findRequiredViewAsType(source, R.id.img_decrease, "field 'img_decrease'", ImageView.class);
    target.img_increase = Utils.findRequiredViewAsType(source, R.id.img_increase, "field 'img_increase'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyCartAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_price_new = null;
    target.txt_food_name = null;
    target.txt_food_price = null;
    target.txt_quantity = null;
    target.txt_extra_price = null;
    target.img_food = null;
    target.img_delete_food = null;
    target.img_decrease = null;
    target.img_increase = null;
  }
}
