package com.example.administrator.a2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView gamePoint;
    private static MainActivity mainActivity = null;
    private int score = 0;

    //写一个构造方法，这样就能在其他地方访问到MainActivity
    public MainActivity(){
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gamePoint = (TextView) findViewById(R.id.gamePoint);

    }

    public void clear(){
        score = 0;
        showScore();
    }

    public void showScore(){
        gamePoint.setText(score+"");
    }

    public void addScore(int s){
        score += s;
        showScore();
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }
}
