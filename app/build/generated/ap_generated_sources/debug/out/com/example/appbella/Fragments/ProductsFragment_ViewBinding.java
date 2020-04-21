// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Fragments;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductsFragment_ViewBinding implements Unbinder {
  private ProductsFragment target;

  @UiThread
  public ProductsFragment_ViewBinding(ProductsFragment target, View source) {
    this.target = target;

    target.recycler_category = Utils.findRequiredViewAsType(source, R.id.recycler_category, "field 'recycler_category'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recycler_category = null;
  }
}
