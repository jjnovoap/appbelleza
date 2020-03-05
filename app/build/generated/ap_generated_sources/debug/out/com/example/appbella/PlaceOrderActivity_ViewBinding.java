// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlaceOrderActivity_ViewBinding implements Unbinder {
  private PlaceOrderActivity target;

  @UiThread
  public PlaceOrderActivity_ViewBinding(PlaceOrderActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlaceOrderActivity_ViewBinding(PlaceOrderActivity target, View source) {
    this.target = target;

    target.edt_date = Utils.findRequiredViewAsType(source, R.id.edt_date, "field 'edt_date'", EditText.class);
    target.txt_total_cash = Utils.findRequiredViewAsType(source, R.id.txt_total_cash, "field 'txt_total_cash'", TextView.class);
    target.txt_user_phone = Utils.findRequiredViewAsType(source, R.id.txt_user_phone, "field 'txt_user_phone'", TextView.class);
    target.txt_user_address = Utils.findRequiredViewAsType(source, R.id.txt_user_address, "field 'txt_user_address'", TextView.class);
    target.txt_new_address = Utils.findRequiredViewAsType(source, R.id.txt_new_address, "field 'txt_new_address'", TextView.class);
    target.btn_add_new_address = Utils.findRequiredViewAsType(source, R.id.btn_add_new_address, "field 'btn_add_new_address'", Button.class);
    target.chb_default_address = Utils.findRequiredViewAsType(source, R.id.chb_default_address, "field 'chb_default_address'", CheckBox.class);
    target.rdi_cod = Utils.findRequiredViewAsType(source, R.id.rdi_cod, "field 'rdi_cod'", RadioButton.class);
    target.rdi_online_payment = Utils.findRequiredViewAsType(source, R.id.rdi_online_payment, "field 'rdi_online_payment'", RadioButton.class);
    target.btn_proceed = Utils.findRequiredViewAsType(source, R.id.btn_proceed, "field 'btn_proceed'", Button.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PlaceOrderActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_date = null;
    target.txt_total_cash = null;
    target.txt_user_phone = null;
    target.txt_user_address = null;
    target.txt_new_address = null;
    target.btn_add_new_address = null;
    target.chb_default_address = null;
    target.rdi_cod = null;
    target.rdi_online_payment = null;
    target.btn_proceed = null;
    target.toolbar = null;
  }
}
