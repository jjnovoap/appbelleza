// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nex3z.notificationbadge.NotificationBadge;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MenuActivity_ViewBinding implements Unbinder {
  private MenuActivity target;

  @UiThread
  public MenuActivity_ViewBinding(MenuActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MenuActivity_ViewBinding(MenuActivity target, View source) {
    this.target = target;

    target.recycler_category = Utils.findRequiredViewAsType(source, R.id.recycler_category, "field 'recycler_category'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.btn_cart = Utils.findRequiredViewAsType(source, R.id.fab, "field 'btn_cart'", FloatingActionButton.class);
    target.badge = Utils.findRequiredViewAsType(source, R.id.badge, "field 'badge'", NotificationBadge.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MenuActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recycler_category = null;
    target.toolbar = null;
    target.btn_cart = null;
    target.badge = null;
  }
}
