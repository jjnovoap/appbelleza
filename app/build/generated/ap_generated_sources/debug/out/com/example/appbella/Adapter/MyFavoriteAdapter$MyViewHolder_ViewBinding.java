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

public class MyFavoriteAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private MyFavoriteAdapter.MyViewHolder target;

  @UiThread
  public MyFavoriteAdapter$MyViewHolder_ViewBinding(MyFavoriteAdapter.MyViewHolder target,
      View source) {
    this.target = target;

    target.img_food = Utils.findRequiredViewAsType(source, R.id.img_food, "field 'img_food'", ImageView.class);
    target.txt_food_name = Utils.findRequiredViewAsType(source, R.id.txt_food_name, "field 'txt_food_name'", TextView.class);
    target.txt_food_price = Utils.findRequiredViewAsType(source, R.id.txt_food_price, "field 'txt_food_price'", TextView.class);
    target.txt_restaurant_name = Utils.findRequiredViewAsType(source, R.id.txt_restaurant_name, "field 'txt_restaurant_name'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFavoriteAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_food = null;
    target.txt_food_name = null;
    target.txt_food_price = null;
    target.txt_restaurant_name = null;
  }
}
