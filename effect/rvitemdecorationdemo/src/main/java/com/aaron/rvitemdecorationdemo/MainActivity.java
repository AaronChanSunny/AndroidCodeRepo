package com.aaron.rvitemdecorationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    private static final String[] MSGS = new String[] { "One", "Two", "Three", "Four", "Five", "Six", "Seven" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static String randomText() {
        StringBuilder builder = new StringBuilder();
        int words = RANDOM.nextInt(15) + 10;
        for (int i = 0; i < words; i++) {
            builder.append(MSGS[RANDOM.nextInt(7)]).append(" ");
        }
        return builder.toString();
    }

}
