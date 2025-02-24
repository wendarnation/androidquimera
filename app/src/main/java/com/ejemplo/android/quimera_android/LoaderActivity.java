package com.ejemplo.android.quimera_android;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

public class LoaderActivity extends AppCompatActivity {

    private int animationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader);

        ImageView loadingImage = findViewById(R.id.loadingImage);
        final RelativeLayout layout = findViewById(R.id.layout);


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

                    ScaleAnimation fullScreenAnimation = new ScaleAnimation(
                            1f, 30f,
                            1f, 30f,
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                    );
                    fullScreenAnimation.setDuration(2000);
                    fullScreenAnimation.setFillAfter(true);


                    ValueAnimator colorAnimation = ValueAnimator.ofObject(
                            new ArgbEvaluator(),
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.white)
                    );


                    colorAnimation.setDuration(1500);
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            layout.setBackgroundColor((int) animator.getAnimatedValue());
                        }
                    });


                    loadingImage.startAnimation(fullScreenAnimation);
                    colorAnimation.start();


                    loadingImage.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    loadingImage.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;


                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) loadingImage.getLayoutParams();


                    params.topMargin = 900;
                    params.leftMargin = 900;


                    params.addRule(RelativeLayout.CENTER_IN_PARENT);

                    loadingImage.setLayoutParams(params);


                    loadingImage.requestLayout();
                }
            }
        });


        loadingImage.startAnimation(scaleAnimation);
    }
}
