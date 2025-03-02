package com.ejemplo.android.quimera_android;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoaderActivity extends AppCompatActivity {

    private int animationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cambiar el color de la barra de estado
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        setContentView(R.layout.loader);

        ImageView loadingImage = findViewById(R.id.loadingImage);
        final RelativeLayout layout = findViewById(R.id.layout);

        // Animación de escala inicial
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.5f,
                1f, 1.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatCount(3);
        scaleAnimation.setRepeatMode(Animation.REVERSE);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {
                animationCount++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animationCount == 3) {
                    // Animación de expansión a pantalla completa
                    ScaleAnimation fullScreenAnimation = new ScaleAnimation(
                            1f, 30f,
                            1f, 30f,
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                    );
                    fullScreenAnimation.setDuration(2000);
                    fullScreenAnimation.setFillAfter(true);

                    // Animación de cambio de color
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(
                            new ArgbEvaluator(),
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.white)
                    );
                    colorAnimation.setDuration(1500);
                    colorAnimation.addUpdateListener(animator ->
                            layout.setBackgroundColor((int) animator.getAnimatedValue())
                    );

                    loadingImage.startAnimation(fullScreenAnimation);
                    colorAnimation.start();

                    // Ajustar imagen a pantalla completa
                    loadingImage.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    loadingImage.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
                    loadingImage.requestLayout();

                    // **Esperar el fin de la animación y cambiar de actividad**
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(LoaderActivity.this, HomeLoginActivity.class);

                        // Transición suave entre actividades
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(
                                LoaderActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);
                        startActivity(intent, options.toBundle());

                        finish(); // Cerrar LoaderActivity
                    }, 2000); // Se espera lo mismo que la duración de la animación
                }
            }
        });

        // Iniciar la animación
        loadingImage.startAnimation(scaleAnimation);
    }
}
