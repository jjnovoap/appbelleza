// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyOrderAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private MyOrderAdapter.MyViewHolder target;

  @UiThread
  public MyOrderAdapter$MyViewHolder_ViewBinding(MyOrderAdapter.MyViewHolder target, View source) {
    this.target = target;

    target.txt_order_number = Utils.findRequiredViewAsType(source, R.id.txt_order_number, "field 'txt_order_number'", TextView.class);
    target.txt_order_status = Utils.findRequiredViewAsType(source, R.id.txt_order_status, "field 'txt_order_status'", TextView.class);
    target.txt_order_phone = Utils.findRequiredViewAsType(source, R.id.txt_order_phone, "field 'txt_order_phone'", TextView.class);
    target.txt_order_address = Utils.findRequiredViewAsType(source, R.id.txt_order_address, "field 'txt_order_address'", TextView.class);
    target.txt_order_date = Utils.findRequiredViewAsType(source, R.id.txt_order_date, "field 'txt_order_date'", TextView.class);
    target.txt_order_total_price = Utils.findRequiredViewAsType(source, R.id.txt_order_total_price, "field 'txt_order_total_price'", TextView.class);
    target.txt_num_of_item = Utils.findRequiredViewAsType(source, R.id.txt_num_of_item, "field 'txt_num_of_item'", TextView.class);
    target.txt_payment_method = Utils.findRequiredViewAsType(source, R.id.txt_payment_method, "field 'txt_payment_method'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyOrderAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_order_number = null;
    target.txt_order_status = null;
    target.txt_order_phone = null;
    target.txt_order_address = null;
    target.txt_order_date = null;
    target.txt_order_total_price = null;
    target.txt_num_of_item = null;
    target.txt_payment_method = null;
  }
}
