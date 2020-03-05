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

public class FavoriteActivity_ViewBinding implements Unbinder {
  private FavoriteActivity target;

  @UiThread
  public FavoriteActivity_ViewBinding(FavoriteActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FavoriteActivity_ViewBinding(FavoriteActivity target, View source) {
    this.target = target;

    target.recycler_fav = Utils.findRequiredViewAsType(source, R.id.recycler_fav, "field 'recycler_fav'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FavoriteActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recycler_fav = null;
    target.toolbar = null;
  }
}
