// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CartListActivity_ViewBinding implements Unbinder {
  private CartListActivity target;

  @UiThread
  public CartListActivity_ViewBinding(CartListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CartListActivity_ViewBinding(CartListActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.recycler_cart = Utils.findRequiredViewAsType(source, R.id.recycler_cart, "field 'recycler_cart'", RecyclerView.class);
    target.txt_final_price = Utils.findRequiredViewAsType(source, R.id.txt_final_price, "field 'txt_final_price'", TextView.class);
    target.btn_order = Utils.findRequiredViewAsType(source, R.id.btn_order, "field 'btn_order'", Button.class);
    target.txt_empty_cart = Utils.findRequiredViewAsType(source, R.id.txt_empty_cart, "field 'txt_empty_cart'", TextView.class);
    target.numero_items = Utils.findRequiredViewAsType(source, R.id.numero_items, "field 'numero_items'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CartListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recycler_cart = null;
    target.txt_final_price = null;
    target.btn_order = null;
    target.txt_empty_cart = null;
    target.numero_items = null;
  }
}
