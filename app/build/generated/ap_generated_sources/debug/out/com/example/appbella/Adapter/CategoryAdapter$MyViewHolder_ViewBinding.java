// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CategoryAdapter$MyViewHolder_ViewBinding implements Unbinder {
  private CategoryAdapter.MyViewHolder target;

  @UiThread
  public CategoryAdapter$MyViewHolder_ViewBinding(CategoryAdapter.MyViewHolder target,
      View source) {
    this.target = target;

    target.txt_category = Utils.findRequiredViewAsType(source, R.id.txt_category, "field 'txt_category'", TextView.class);
    target.img_view = Utils.findRequiredViewAsType(source, R.id.img_view, "field 'img_view'", ImageView.class);
    target.card = Utils.findRequiredViewAsType(source, R.id.card, "field 'card'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CategoryAdapter.MyViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txt_category = null;
    target.img_view = null;
    target.card = null;
  }
}
