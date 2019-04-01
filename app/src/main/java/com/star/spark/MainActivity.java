package com.star.spark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.star.spark.widget.NineLuckView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private NineLuckView mNineLuckView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        mNineLuckView = findViewById(R.id.nine_luck_view);
        mNineLuckView.setOnLuckViewAnimEndListener(new NineLuckView.OnLuckViewAnimListener() {
            @Override
            public void onAnimStart() {
                mNineLuckView.setmLuckNum(new Random().nextInt(8));
            }

            @Override
            public void onAnimEnd(int position, String msg) {
                Toast.makeText(MainActivity.this, "位置：--->" + position + "<----信息：--->" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
