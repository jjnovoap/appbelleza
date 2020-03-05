// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ViewOrderActivity_ViewBinding implements Unbinder {
  private ViewOrderActivity target;

  @UiThread
  public ViewOrderActivity_ViewBinding(ViewOrderActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ViewOrderActivity_ViewBinding(ViewOrderActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.recycler_view_order = Utils.findRequiredViewAsType(source, R.id.recycler_view_order, "field 'recycler_view_order'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ViewOrderActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recycler_view_order = null;
  }
}
