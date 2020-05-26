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

public class SubcategoryServiceAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private SubcategoryServiceAdapter.MyViewHolder target;

  @UiThread
  public SubcategoryServiceAdapter$MyViewHolder_ViewBinding(
      SubcategoryServiceAdapter.MyViewHolder target, View source) {
    this.target = target;

    target.img_subcategory = Utils.findRequiredViewAsType(source, R.id.img_subcategory, "field 'img_subcategory'", ImageView.class);
    target.txt_subcategory = Utils.findRequiredViewAsType(source, R.id.txt_subcategory, "field 'txt_subcategory'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SubcategoryServiceAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.img_subcategory = null;
    target.txt_subcategory = null;
  }
}
