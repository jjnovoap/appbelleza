// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
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

    target.btn_add = Utils.findRequiredViewAsType(source, R.id.btn_add, "field 'btn_add'", Button.class);
    target.txt_money = Utils.findRequiredViewAsType(source, R.id.txt_money, "field 'txt_money'", TextView.class);
    target.txt_service = Utils.findRequiredViewAsType(source, R.id.txt_service, "field 'txt_service'", TextView.class);
    target.recycler_addon = Utils.findRequiredViewAsType(source, R.id.recycler_addon, "field 'recycler_addon'", RecyclerView.class);
    target.txt_description = Utils.findRequiredViewAsType(source, R.id.txt_description, "field 'txt_description'", TextView.class);
    target.header = Utils.findRequiredViewAsType(source, R.id.header, "field 'header'", ImageView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductAndServiceDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btn_add = null;
    target.txt_money = null;
    target.txt_service = null;
    target.recycler_addon = null;
    target.txt_description = null;
    target.header = null;
    target.toolbar = null;
  }
}
