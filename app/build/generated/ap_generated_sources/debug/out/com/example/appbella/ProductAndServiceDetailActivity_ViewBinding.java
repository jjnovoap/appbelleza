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
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductAndServiceDetailActivity_ViewBinding implements Unbinder {
  private ProductAndServiceDetailActivity target;

  @UiThread
  public ProductAndServiceDetailActivity_ViewBinding(ProductAndServiceDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProductAndServiceDetailActivity_ViewBinding(ProductAndServiceDetailActivity target,
      View source) {
    this.target = target;

    target.fab_add_to_cart = Utils.findRequiredViewAsType(source, R.id.fab_add_to_cart, "field 'fab_add_to_cart'", FloatingActionButton.class);
    target.btn_view_cart = Utils.findRequiredViewAsType(source, R.id.btn_view_cart, "field 'btn_view_cart'", Button.class);
    target.txt_money = Utils.findRequiredViewAsType(source, R.id.txt_money, "field 'txt_money'", TextView.class);
    target.recycler_addon = Utils.findRequiredViewAsType(source, R.id.recycler_addon, "field 'recycler_addon'", RecyclerView.class);
    target.txt_description = Utils.findRequiredViewAsType(source, R.id.txt_description, "field 'txt_description'", TextView.class);
    target.img_food_detail = Utils.findRequiredViewAsType(source, R.id.img_food_detail, "field 'img_food_detail'", KenBurnsView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductAndServiceDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.fab_add_to_cart = null;
    target.btn_view_cart = null;
    target.txt_money = null;
    target.recycler_addon = null;
    target.txt_description = null;
    target.img_food_detail = null;
    target.toolbar = null;
  }
}
