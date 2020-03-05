// Generated code from Butter Knife. Do not modify!
package com.example.appbella;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UpdateInfoActivity_ViewBinding implements Unbinder {
  private UpdateInfoActivity target;

  @UiThread
  public UpdateInfoActivity_ViewBinding(UpdateInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UpdateInfoActivity_ViewBinding(UpdateInfoActivity target, View source) {
    this.target = target;

    target.edt_user_name = Utils.findRequiredViewAsType(source, R.id.edt_user_name, "field 'edt_user_name'", EditText.class);
    target.edt_user_lastname = Utils.findRequiredViewAsType(source, R.id.edt_user_lastname, "field 'edt_user_lastname'", EditText.class);
    target.edt_user_address = Utils.findRequiredViewAsType(source, R.id.edt_user_address, "field 'edt_user_address'", EditText.class);
    target.edt_user_doc = Utils.findRequiredViewAsType(source, R.id.edt_user_doc, "field 'edt_user_doc'", EditText.class);
    target.btn_update = Utils.findRequiredViewAsType(source, R.id.btn_update, "field 'btn_update'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UpdateInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.edt_user_name = null;
    target.edt_user_lastname = null;
    target.edt_user_address = null;
    target.edt_user_doc = null;
    target.btn_update = null;
  }
}
