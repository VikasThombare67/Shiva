package com.example.shiva;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class na extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_na);

        // Set a delay of 3 seconds (3000 milliseconds) before moving to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity (MainActivity)
                Intent intent = new Intent(na.this, LauncherActivity.class);
                startActivity(intent);
                finish(); // Optionally finish the launcher activity
            }
        }, 3000); // Delay of 3 seconds
    }
}
