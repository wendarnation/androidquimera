package com.ejemplo.android.quimera_android;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.widget.Button;

public class HomeLoginActivity extends AppCompatActivity {

    private VideoView videoBackground;
    private ImageView animatedImage;
    private TextView animatedText;
    private Button leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cambiar el color de la barra de estado
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        setContentView(R.layout.home_login);

        videoBackground = findViewById(R.id.videoinicio);
        animatedImage = findViewById(R.id.animatedImage);
        animatedText = findViewById(R.id.animatedText);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);

        adjustTextSize();
        // Cargar y reproducir el video
        playVideo();

        // Ejecutar la animación de la imagen después de 2 segundos
        new Handler().postDelayed(this::animateImage, 2000);

        leftButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeLoginActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }


    private void playVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoinicio);
        videoBackground.setVideoURI(videoUri);

        videoBackground.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoBackground.start();

            // Obtener dimensiones del video original
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();

            // Obtener dimensiones de la pantalla
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            // Zoom del video (ajustado según la pantalla)
            float scaleFactor = 4.5f; // Zoom que quieres
            videoBackground.setScaleX(scaleFactor);
            videoBackground.setScaleY(scaleFactor);

            // Ajustar el tamaño del VideoView para que cubra toda la pantalla correctamente
            ViewGroup.LayoutParams params = videoBackground.getLayoutParams();
            params.width = screenWidth;
            params.height = Math.max(screenHeight, (int) (videoHeight * scaleFactor)); // Hacerlo más alto que la pantalla
            videoBackground.setLayoutParams(params);

            // Calcular desplazamiento para centrarlo bien
            float translationX = (screenWidth - (videoWidth * scaleFactor)) / 20f;
            float translationY = (screenHeight - (videoHeight * scaleFactor)) / 30f;

            // Aplicar desplazamiento correctamente
            videoBackground.post(() -> {
                videoBackground.setTranslationX(translationX);
                videoBackground.setTranslationY(translationY + (screenHeight * 0.5f)); // Ajusta más el centrado
            });
        });

        // Asegurar que el video siga reproduciéndose en bucle
        videoBackground.setOnCompletionListener(mp -> videoBackground.start());
    }

    /**
     * Anima la imagen para que aparezca con un efecto de fade-in y se mueva desde abajo hasta el centro.
     */
    private void animateImage() {
        float startY = 200f; // Comienza desde abajo
        float endY = -100f; // Termina en su posición normal

        // Animación de desvanecimiento (alpha de 0 a 1)
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(animatedImage, "alpha", 0f, 1f);
        fadeIn.setDuration(700);

        // Animación de movimiento desde abajo hasta el centro
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(animatedImage, "translationY", startY, endY);
        moveUp.setDuration(700);

        // Ejecutar ambas animaciones simultáneamente
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn, moveUp);
        animatorSet.start();

        // Esperar 1 segundo después de la imagen para animar el texto
        new Handler().postDelayed(this::animateText, 1000);
    }

    private void adjustTextSize() {
        float screenWidth = getResources().getDisplayMetrics().widthPixels; // Ancho de la pantalla en píxeles
        float scaledSize = screenWidth / 50f;

        animatedText.setTextSize(scaledSize);
    }

    /**
     * Anima el texto para que aparezca con un efecto de fade-in y se mueva desde abajo hasta su posición final.
     */
    private void animateText() {
        float startY = 100f; // Comienza un poco más abajo de su posición final
        float endY = 0f; // Termina en su posición normal

        // Animación de desvanecimiento (alpha de 0 a 1)
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(animatedText, "alpha", 0f, 1f);
        fadeIn.setDuration(700);

        // Animación de movimiento desde abajo hasta su posición final
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(animatedText, "translationY", startY, endY);
        moveUp.setDuration(700);

        // Ejecutar ambas animaciones simultáneamente
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn, moveUp);
        animatorSet.start();

        // Esperar 500ms después del texto para animar los botones
        new Handler().postDelayed(this::animateButtons, 500);
    }

    private void animateButtons() {
        ObjectAnimator fadeInLeft = ObjectAnimator.ofFloat(leftButton, "alpha", 0f, 1f);
        ObjectAnimator fadeInRight = ObjectAnimator.ofFloat(rightButton, "alpha", 0f, 1f);

        fadeInLeft.setDuration(700);
        fadeInRight.setDuration(700);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeInLeft, fadeInRight);
        animatorSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoBackground.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoBackground.pause();
    }
}
