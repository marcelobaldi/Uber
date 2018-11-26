package com.project.uber;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends Activity {
    private EditText txtEmail, txtPass;
    private Button btnSign;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.CriarComponentes();
        this.CriarEvento();
    }
    private void CriarComponentes(){
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnSign = findViewById(R.id.btnSign);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }
    private void CriarEvento(){
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Signing in, Please wait");
                dialog.show();
                if (txtEmail.getText().toString().isEmpty() || txtPass.getText().toString().isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(SignInActivity.this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(txtEmail.getText().toString(),txtPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                    //   dialog.dismiss();
                                        Toast.makeText(SignInActivity.this,"User successfully signed in", Toast.LENGTH_LONG).show();



                                        //  Intent i = new Intent(SignInActivity.this, MainPageActivity.class);
                                      //  startActivity(i);


                                    }else {
                                     //   dialog.dismiss();
                                        Toast.makeText(SignInActivity.this,"User not found",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

    }
}
