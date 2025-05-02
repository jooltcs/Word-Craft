package com.joolt.wordcraft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button button;
    private AutoCompleteTextView dropdownMenu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String[] categories = {"The Law, Government and Crime", "Education and Learning", "Travel, Tourism and Transport", "Friends and Relatives", "Money and Shopping", "Inventions and Discoveries", "People and Society", "Work and Business", "Body and Lifestyle", "Science and Technology", "Nature, The Universe and Astronomy", "Emotions and Personalities", "The Media and Communication", "Hobbies, Sports and Games", "Health and Fitness", "Food, Drink and Culinary Arts", "Weather and the Environment", "Entertainment and Fun", "Fashion and Design", "Art and Culture", "Business and Economics", "Mysteries and the Supernatural", "Family and Childhood", "Home and Daily Life"};
        dropdownMenu = findViewById(R.id.dropdown_menu);
        button = findViewById(R.id.play_button);

        final String[] selected = {categories[0]};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.list_item,
                categories
        );

        dropdownMenu.setAdapter(adapter);
        dropdownMenu.setText(categories[0], false);

        dropdownMenu.setOnItemClickListener((parent, view1, position, id) -> {
            selected[0] = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), "Selected: " + selected[0], Toast.LENGTH_SHORT).show();
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}