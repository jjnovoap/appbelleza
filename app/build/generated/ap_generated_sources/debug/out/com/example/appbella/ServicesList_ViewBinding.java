// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServicesList_ViewBinding implements Unbinder {
  private ServicesList target;

  @UiThread
  public ServicesList_ViewBinding(ServicesList target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ServicesList_ViewBinding(ServicesList target, View source) {
    this.target = target;

    target.recycler_food_list = Utils.findRequiredViewAsType(source, R.id.recycler_food_list, "field 'recycler_food_list'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.txt_subcategory = Utils.findRequiredViewAsType(source, R.id.txt_subcategory, "field 'txt_subcategory'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ServicesList target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recycler_food_list = null;
    target.toolbar = null;
    target.txt_subcategory = null;
  }
}
