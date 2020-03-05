// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductAndServiceList_ViewBinding implements Unbinder {
  private ProductAndServiceList target;

  @UiThread
  public ProductAndServiceList_ViewBinding(ProductAndServiceList target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProductAndServiceList_ViewBinding(ProductAndServiceList target, View source) {
    this.target = target;

    target.img_category = Utils.findRequiredViewAsType(source, R.id.img_category, "field 'img_category'", KenBurnsView.class);
    target.recycler_food_list = Utils.findRequiredViewAsType(source, R.id.recycler_food_list, "field 'recycler_food_list'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductAndServiceList target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_category = null;
    target.recycler_food_list = null;
    target.toolbar = null;
  }
}
