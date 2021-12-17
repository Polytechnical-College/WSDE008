package com.example.taxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {
    ImageView toggleMenu, toggleMenu2;
    TextView settings,history;
    LinearLayout menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        toggleMenu = findViewById(R.id.toogleMenu);
        toggleMenu2 = findViewById(R.id.ToggleMenu2);
        menu = findViewById(R.id.menu);
        toggleMenu.setOnClickListener( v-> {
            Toogle();
        });
        toggleMenu2.setOnClickListener(v -> {
            Toogle();
        });
        settings = findViewById(R.id.setting);
        history = findViewById(R.id.history);
        settings.setOnClickListener( v-> {
            Intent intent = new Intent (this, Settings.class);
            intent.putExtra("token",getIntent().getExtras().getInt("token"));
            startActivity(intent);
            finish();

        });
        history.setOnClickListener( v-> {
            Intent intent = new Intent (this, History.class);
            intent.putExtra("token",getIntent().getExtras().getInt("token"));
            startActivity(intent);
            finish();
        });
    }

    private void Toogle() {
        if(menu.getVisibility() == View.INVISIBLE) {
            toggleMenu.setVisibility(View.INVISIBLE);
            menu.setVisibility(View.VISIBLE);
        }
        else {
            toggleMenu.setVisibility(View.VISIBLE);
            menu.setVisibility(View.INVISIBLE);
        }


    }
}