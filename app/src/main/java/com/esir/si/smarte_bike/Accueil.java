package com.esir.si.smarte_bike;


        import android.app.Activity;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.Button;
        import android.widget.TextView;
public class Accueil extends Activity implements OnClickListener {

    Animation animationconnect;
    Button monBouton;
    TextView monTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monTextView = (TextView) findViewById(R.id.button);
        monBouton = (Button) findViewById(R.id.button);
        monBouton.setOnClickListener(this);
        animationconnect = AnimationUtils.loadAnimation(this, R.anim.connectanim);
    }
    public void onClick(View v) {
        if(v.getId() == R.id.button){
            monTextView.startAnimation(animationconnect);

        }
    }
}