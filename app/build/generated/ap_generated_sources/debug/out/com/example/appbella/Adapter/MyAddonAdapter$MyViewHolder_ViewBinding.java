// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyAddonAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private MyAddonAdapter.MyViewHolder target;

  @UiThread
  public MyAddonAdapter$MyViewHolder_ViewBinding(MyAddonAdapter.MyViewHolder target, View source) {
    this.target = target;

    target.ckb_addon = Utils.findRequiredViewAsType(source, R.id.ckb_addon, "field 'ckb_addon'", CheckBox.class);
    target.txt_price = Utils.findRequiredViewAsType(source, R.id.txt_price, "field 'txt_price'", TextView.class);
    target.txt_title = Utils.findRequiredViewAsType(source, R.id.txt_title, "field 'txt_title'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyAddonAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ckb_addon = null;
    target.txt_price = null;
    target.txt_title = null;
  }
}
