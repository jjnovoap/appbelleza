package com.example.appbella;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int APP_REQUEST_CODE = 1234;

    private AlertDialog mDialog;
    CollectionReference userRef;

    List<AuthUI.IdpConfig> providers = Collections.singletonList(
            //new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build()//,
            //new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @BindView(R.id.btn_sign_in)
    Button btn_sign_in;

    @OnClick(R.id.btn_sign_in)
    void loginUser() {
        Log.d(TAG, "loginUser: called!!");
        showSignInOptions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {

            IdpResponse response = IdpResponse.fromResultIntent(data);
            FirebaseAuth auth = FirebaseAuth.getInstance();

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                DocumentReference currentUser = userRef.document(auth.getCurrentUser().getUid());
                currentUser.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot userSnapShot = task.getResult();
                                assert userSnapShot != null;
                                if (userSnapShot.exists()) {
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    //If user already available in our system
                                    startActivity(new Intent(MainActivity.this, UpdateInfoActivity.class));
                                    finish();
                                }
                                if (mDialog.isShowing())
                                    mDialog.dismiss();
                            }
                        });
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Sin Conexión a Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show();;
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started!!");

        ButterKnife.bind(this);

        userRef = FirebaseFirestore.getInstance().collection("User");

        providers = Collections.singletonList(
                //new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builer
                new AuthUI.IdpConfig.PhoneBuilder().build()//, //Phone Builer
                //new AuthUI.IdpConfig.GoogleBuilder().build() //Google Builer
        );

        init();
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .setLogo(R.drawable.app_icon)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                APP_REQUEST_CODE);
    }

    private void init() {
        Log.d(TAG, "init: called!!");
        Paper.init(this);
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
    }


}
