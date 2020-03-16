package com.example.appbella;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbella.Common.Common;
import com.example.appbella.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class UpdateInfoActivity extends AppCompatActivity {

    private static final String TAG = UpdateInfoActivity.class.getSimpleName();

    private AlertDialog mDialog;

    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.edt_user_lastname)
    EditText edt_user_lastname;
    @BindView(R.id.edt_user_address)
    EditText edt_user_address;
    @BindView(R.id.edt_user_doc)
    EditText edt_user_doc;
    @BindView(R.id.btn_update)
    Button btn_update;

    CollectionReference userRef;

    private static final Pattern DOC_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            ".{2,}" +               //at least 1 characters
            "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        Log.d(TAG, "onCreate: started!!");

        ButterKnife.bind(this);
        userRef = FirebaseFirestore.getInstance().collection("User");

        init();
        initView();
    }

    // Override back arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        btn_update.setOnClickListener(v -> {

            if (!validateName() | !validateAddress() | !validateNumdoc()) {
                return;
            }

            mDialog.show();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            userRef.document(auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userSnapShot = task.getResult();
                            if (!userSnapShot.exists()) {
                                if (!mDialog.isShowing())
                                    mDialog.show();
                                //add user to firebase
                                final User user = new User(auth.getCurrentUser().getUid(),
                                        edt_user_name.getText().toString(),
                                        edt_user_lastname.getText().toString(),
                                        edt_user_address.getText().toString(),
                                        auth.getCurrentUser().getPhoneNumber(),
                                        edt_user_doc.getText().toString());

                                userRef.document(auth.getCurrentUser().getUid())
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            if (mDialog.isShowing())
                                                mDialog.dismiss();
                                            Common.currentUser = user;
                                            Toast.makeText(UpdateInfoActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(UpdateInfoActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }).addOnFailureListener(e -> {

                                    if (mDialog.isShowing())
                                        mDialog.dismiss();
                                    Toast.makeText(UpdateInfoActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                //If user already available in our system
                                Common.currentUser = userSnapShot.toObject(User.class);
                            }
                            if (mDialog.isShowing())
                                mDialog.dismiss();
                        }
                    }).addOnFailureListener(e -> {
                mDialog.dismiss();
                Toast.makeText(UpdateInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        });
    }

    //Check if boxes are empty
    private boolean validateName() {
        String Name = edt_user_name.getText().toString().trim();
        if (Name.isEmpty()) {
            edt_user_name.setError("No olvides ingresar tu nombre");
            return false;
        } else {
            edt_user_name.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {
        String direccionInput = edt_user_address.getText().toString().trim();
        if (direccionInput.isEmpty()) {
            edt_user_address.setError("No olvides ingresar tu dirección");
            return false;
        } else {
            edt_user_address.setError(null);
            return true;
        }
    }

    private boolean validateNumdoc() {
        String numdocInput = Objects.requireNonNull(edt_user_doc.getText()).toString().trim();
        if (numdocInput.isEmpty()) {
            edt_user_doc.setError("Ingrese número de documento");
            return false;
        } else if (numdocInput.length() > 12) {
            edt_user_doc.setError("Documento no valido");
            return false;
        } else if (!DOC_PATTERN.matcher(numdocInput).matches()) {
            edt_user_doc.setError("Documento no valido");
            return false;
        } else {
            edt_user_doc.setError(null);
            return true;
        }
    }


    private void init() {
        Log.d(TAG, "init: called!!");
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
    }
}
