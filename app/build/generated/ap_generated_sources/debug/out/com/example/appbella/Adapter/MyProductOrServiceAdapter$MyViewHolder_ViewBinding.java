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

public class MyProductOrServiceAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private MyProductOrServiceAdapter.MyViewHolder target;

  @UiThread
  public MyProductOrServiceAdapter$MyViewHolder_ViewBinding(
      MyProductOrServiceAdapter.MyViewHolder target, View source) {
    this.target = target;

    target.txt_restaurant_name = Utils.findRequiredViewAsType(source, R.id.txt_restaurant_name, "field 'txt_restaurant_name'", TextView.class);
    target.img_restaurant = Utils.findRequiredViewAsType(source, R.id.img_restaurant, "field 'img_restaurant'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyProductOrServiceAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_restaurant_name = null;
    target.img_restaurant = null;
  }
}
