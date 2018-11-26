package com.project.uber;

import android.app.Activity;
import android.app.ProgressDialog;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.project.uber.model.Usuario;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.uber.model.UsuarioFirebase;

public class SignUpActivity extends Activity {
    private EditText txtName, txtEmail, txtPass;
    private Button btnSign;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private RadioButton chbPassageiro;
    private RadioButton chbMotorista;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.CriarComponentes();
        this.CriarEventos();
    }
    private void CriarComponentes(){
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnSign = findViewById(R.id.btnSign);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        chbMotorista = findViewById(R.id.chbMotorista);
        chbPassageiro = findViewById(R.id.chbPassageiro);


    }
    private void CriarEventos(){
        usuario = new Usuario();
        chbMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chbMotorista.isChecked()){
                    chbPassageiro.setChecked(false);
                }
            }
        });
        chbPassageiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chbPassageiro.isChecked()){
                    chbMotorista.setChecked(false);
                }
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String pass = txtPass.getText().toString();
                 String tipo;
                if (chbMotorista.isChecked()){
                    tipo = "M";
                }else {
                    tipo = "P";
                }

                dialog.setMessage("Registering, Please wait.");
                dialog.show();
                if (txtName.getText().toString().isEmpty() || txtEmail.getText().toString().isEmpty() || txtPass.getText().toString().isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this,"Fields cannot be blank", Toast.LENGTH_SHORT).show();
                }else {
                    usuario.setNome(name);
                    usuario.setEmail(email);
                    usuario.setSenha(pass);
                    usuario.setTipo(tipo);
                    auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                String idUsuario = task.getResult().getUser().getUid();
                                usuario.setId(idUsuario);
                                usuario.Salvar();
                                UsuarioFirebase.AtualizarNomeUsuario(usuario.getNome());
                                if(usuario.getTipo().equals("P")){
                                    startActivity(new Intent(SignUpActivity.this, MapsActivity.class));
                                    finish();
                                    Toast.makeText(SignUpActivity.this,"Sucesso ao cadastrar Passageiro!",Toast.LENGTH_SHORT).show();
                                }else {
                                    startActivity(new Intent(SignUpActivity.this, RequisicoesActivity.class));
                                    finish();
                                    Toast.makeText(SignUpActivity.this,"Sucesso ao cadastrar Motorista!",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(SignUpActivity.this,"User could not be registered",Toast.LENGTH_SHORT).show();
                                String ex = "";
                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthWeakPasswordException e){
                                    ex = "Digite uma senha mais forte!";
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    ex = "Digite um e-mail válido";
                                }catch (FirebaseAuthUserCollisionException e){
                                    ex = "Esta conta já foi cadastrada";
                                } catch( Exception e){
                                    ex = "Erro ao cadastrar usuário: " + e.getMessage();
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }
            }
        });

    }
}
