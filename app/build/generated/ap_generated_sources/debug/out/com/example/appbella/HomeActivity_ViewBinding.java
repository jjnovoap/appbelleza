// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nex3z.notificationbadge.NotificationBadge;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeActivity_ViewBinding implements Unbinder {
  private HomeActivity target;

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target, View source) {
    this.target = target;

    target.bottom_navigation = Utils.findRequiredViewAsType(source, R.id.bottom_navigation, "field 'bottom_navigation'", BottomNavigationView.class);
    target.img_user = Utils.findRequiredViewAsType(source, R.id.img_user, "field 'img_user'", ImageView.class);
    target.btn_cart = Utils.findRequiredViewAsType(source, R.id.fab, "field 'btn_cart'", ImageView.class);
    target.badge = Utils.findRequiredViewAsType(source, R.id.badge, "field 'badge'", NotificationBadge.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bottom_navigation = null;
    target.img_user = null;
    target.btn_cart = null;
    target.badge = null;
  }
}
