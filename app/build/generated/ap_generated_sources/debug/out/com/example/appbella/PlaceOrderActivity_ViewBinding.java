// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.Button;
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

    target.txt_date = Utils.findRequiredViewAsType(source, R.id.txt_date, "field 'txt_date'", TextView.class);
    target.txt_total_cash = Utils.findRequiredViewAsType(source, R.id.txt_total_cash, "field 'txt_total_cash'", TextView.class);
    target.txt_tittle = Utils.findRequiredViewAsType(source, R.id.txt_tittle, "field 'txt_tittle'", TextView.class);
    target.txt_etiqueta = Utils.findRequiredViewAsType(source, R.id.txt_etiqueta, "field 'txt_etiqueta'", TextView.class);
    target.txt_address = Utils.findRequiredViewAsType(source, R.id.txt_address, "field 'txt_address'", TextView.class);
    target.rdi_cod = Utils.findRequiredViewAsType(source, R.id.rdi_cod, "field 'rdi_cod'", RadioButton.class);
    target.rdi_online_payment = Utils.findRequiredViewAsType(source, R.id.rdi_online_payment, "field 'rdi_online_payment'", RadioButton.class);
    target.btn_proceed = Utils.findRequiredViewAsType(source, R.id.btn_proceed, "field 'btn_proceed'", Button.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.numero_items = Utils.findRequiredViewAsType(source, R.id.numero_items, "field 'numero_items'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PlaceOrderActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_date = null;
    target.txt_total_cash = null;
    target.txt_tittle = null;
    target.txt_etiqueta = null;
    target.txt_address = null;
    target.rdi_cod = null;
    target.rdi_online_payment = null;
    target.btn_proceed = null;
    target.toolbar = null;
    target.numero_items = null;
  }
}
