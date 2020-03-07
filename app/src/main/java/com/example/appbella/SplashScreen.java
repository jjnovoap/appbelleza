package com.example.appbella;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();

    private AlertDialog mDialog;
    CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started!!");
        userRef = FirebaseFirestore.getInstance().collection("User");
        init();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if (auth.getCurrentUser() != null) {
                            // already signed in
                            DocumentReference currentUser = userRef.document(auth.getCurrentUser().getUid());
                            currentUser.get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot userSnapShot = task.getResult();
                                            if (userSnapShot.exists()) {
                                                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                //If user already available in our system
                                                startActivity(new Intent(SplashScreen.this, UpdateInfoActivity.class));
                                                finish();
                                            }
                                            if (mDialog.isShowing())
                                                mDialog.dismiss();
                                        }
                                    });
                        } else {
                            // not signed in
                            Toast.makeText(SplashScreen.this, "Not sign in! Please sign in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            finish();
                        }
                        /*
                        // Get Token
                        FirebaseInstanceId.getInstance()
                                .getInstanceId()
                                .addOnFailureListener(e ->
                                        Toast.makeText(SplashScreen.this, "[GET TOKEN]"+e.getMessage(), Toast.LENGTH_SHORT).show())
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        if (auth.getCurrentUser() != null) {
                                            // already signed in
                                            DocumentReference currentUser = userRef.document(auth.getCurrentUser().getUid());

                                            Paper.book().write(Common.REMEMBER_FBID, auth.getCurrentUser().getUid());

                                            mDialog.show();

                                            currentUser.get()
                                                    .addOnCompleteListener(tas -> {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot userSnapShot = tas.getResult();
                                                            if (userSnapShot.exists()) {
                                                                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                //If user already available in our system
                                                                startActivity(new Intent(SplashScreen.this, UpdateInfoActivity.class));
                                                                finish();
                                                            }
                                                            mDialog.dismiss();
                                                        }
                                                    });
                                        } else {
                                            // not signed in
                                            Toast.makeText(SplashScreen.this, "Not sign in! Please sign in", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });*/

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(SplashScreen.this, "You must accept this permission to user our app", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

    }

    private void init() {
        Log.d(TAG, "init: called!!");
        Paper.init(this);
        mDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
    }
}
