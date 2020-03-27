// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
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

    target.img_user = Utils.findRequiredViewAsType(source, R.id.img_user, "field 'img_user'", ImageView.class);
    target.recycler_catalogo = Utils.findRequiredViewAsType(source, R.id.recycler_catalogo, "field 'recycler_catalogo'", RecyclerView.class);
    target.btn_cart = Utils.findRequiredViewAsType(source, R.id.fab, "field 'btn_cart'", ImageView.class);
    target.badge = Utils.findRequiredViewAsType(source, R.id.badge, "field 'badge'", NotificationBadge.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_user = null;
    target.recycler_catalogo = null;
    target.btn_cart = null;
    target.badge = null;
  }
}
