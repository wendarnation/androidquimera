package com.ejemplo.android.quimera_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userInputLayout, passwordInputLayout;
    private TextInputEditText inputUsuario, inputPassword;
    private TextView errorMessage;
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar la barra de estado
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        setContentView(R.layout.activity_login);

        // Vincular los elementos con sus IDs en el XML
        userInputLayout = findViewById(R.id.userInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        inputUsuario = findViewById(R.id.inputUsuario);
        inputPassword = findViewById(R.id.inputPassword);
        errorMessage = findViewById(R.id.errorMessage);
        btnEntrar = findViewById(R.id.btnEntrar);

        // Manejar el clic en el botón "Entra"
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarYEntrar();
            }
        });
    }

    private void validarYEntrar() {
        String usuario = inputUsuario.getText().toString().trim();
        String contraseña = inputPassword.getText().toString().trim();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            errorMessage.setText("Por favor, completa ambos campos.");
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            errorMessage.setVisibility(View.GONE);

            // Guardar el nombre de usuario en SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USERNAME", usuario);
            editor.apply();

            // Pasar el usuario a HomeActivity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}