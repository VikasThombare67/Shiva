package com.example.shiva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;


public class AdminHomeActivity extends AppCompatActivity {

    private ImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Initialize the ImageViews
        imageView1 = findViewById(R.id.image_view1);
        imageView2 = findViewById(R.id.image_view2);
        imageView3 = findViewById(R.id.image_view3);
        imageView4 = findViewById(R.id.image_view4);

        // Set OnClickListeners for each ImageView
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Notes Activity
                Intent intent = new Intent(AdminHomeActivity.this, User_home.class);
                startActivity(intent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Developers Activity
                Intent intent = new Intent(AdminHomeActivity.this, DevelopersActivity.class);
                startActivity(intent);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Staff Activity
                Intent intent = new Intent(AdminHomeActivity.this, StaffActivity.class);
                startActivity(intent);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Manual Book Activity
                Intent intent = new Intent(AdminHomeActivity.this, ManualBookActivity.class);
                startActivity(intent);
            }
        });
    }
}
