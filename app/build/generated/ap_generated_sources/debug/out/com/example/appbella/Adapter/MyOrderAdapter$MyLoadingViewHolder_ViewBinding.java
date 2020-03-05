// Generated code from Butter Knife. Do not modify!
package com.example.appbella.Adapter;

import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.appbella.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyOrderAdapter$MyLoadingViewHolder_ViewBinding implements Unbinder {
  private MyOrderAdapter.MyLoadingViewHolder target;

  @UiThread
  public MyOrderAdapter$MyLoadingViewHolder_ViewBinding(MyOrderAdapter.MyLoadingViewHolder target,
      View source) {
    this.target = target;

    target.progress_bar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progress_bar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyOrderAdapter.MyLoadingViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progress_bar = null;
  }
}
