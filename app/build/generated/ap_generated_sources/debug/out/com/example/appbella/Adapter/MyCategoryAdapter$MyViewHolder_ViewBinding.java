// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyCategoryAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private MyCategoryAdapter.MyViewHolder target;

  @UiThread
  public MyCategoryAdapter$MyViewHolder_ViewBinding(MyCategoryAdapter.MyViewHolder target,
      View source) {
    this.target = target;

    target.img_category = Utils.findRequiredViewAsType(source, R.id.img_category, "field 'img_category'", ImageView.class);
    target.txt_category = Utils.findRequiredViewAsType(source, R.id.txt_category, "field 'txt_category'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyCategoryAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_category = null;
    target.txt_category = null;
  }
}
