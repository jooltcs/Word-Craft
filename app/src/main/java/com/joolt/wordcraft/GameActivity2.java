package com.joolt.wordcraft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameActivity2 extends AppCompatActivity {
    private List<TextView> letterViews = new ArrayList<>();
    private int currentLetterIndex = 0;
    String words_file = "words.txt";
    TextView word, result;
    String[] selected_line;
    int result_value2 = 0;
    StringBuilder sb;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        Intent intent = getIntent();

        word = findViewById(R.id.word);
        result = findViewById(R.id.result);
        result_value2 = intent.getIntExtra("result2", 0);
        result.setText(result_value2 + " / 10");
        LinearLayout letterContainer = findViewById(R.id.letterContainer);
        sb = new StringBuilder("");

        selected_line = getWord(words_file, generateNumberLine(1, 95)).split(" - ");
        String selectedWord = selected_line[0];
        if (selectedWord == null || selectedWord.trim().isEmpty()) selectedWord = "apple";

        selectedWord = selectedWord.toLowerCase();
        word.setText(selected_line[2]);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < selectedWord.length(); i++) {
            addText(inflater, letterContainer);
        }

        setupButtonsWithWord(selectedWord);
    }

    private void addText(LayoutInflater inflater, LinearLayout letterContainer) {
        View letterView = inflater.inflate(R.layout.letter_item, letterContainer, false);
        TextView letterText = letterView.findViewById(R.id.letterContent);
        letterViews.add(letterText);
        letterContainer.addView(letterView);
    }

    private String getWord(String file_name, int lineNumber){
        try (InputStream inputStream = getAssets().open(file_name);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            for (int i = 0; i < lineNumber; i++) {
                br.readLine();
            }
            return br.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public static int generateNumberLine(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private void setupButtonsWithWord(String word) {
        List<Button> allButtons = new ArrayList<>();

        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 5; col++) {
                String buttonID = "btn_" + row + col;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                Button button = findViewById(resID);
                if (button != null) {
                    allButtons.add(button);
                }
            }
        }

        Collections.shuffle(allButtons);
        int index = 0;

        Set<Character> uniqueChars = new HashSet<>();
        for (char c : word.toCharArray()) {
            uniqueChars.add(c);
        }

        List<Character> uniqueCharsList = new ArrayList<>(uniqueChars);
        Collections.shuffle(uniqueCharsList);

        for (int i = 0; i < uniqueCharsList.size() && i < allButtons.size(); i++) {
            allButtons.get(i).setText(String.valueOf(uniqueCharsList.get(i)));
            index++;
        }

        Random random = new Random();
        for (; index < allButtons.size(); index++) {
            char randomChar = (char) ('a' + random.nextInt(26));
            allButtons.get(index).setText(String.valueOf(randomChar));
        }

        for (Button button : allButtons) {
            button.setOnClickListener(v -> {
                if (currentLetterIndex < letterViews.size()) {
                    String letter = button.getText().toString();
                    letterViews.get(currentLetterIndex).setText(letter);
                    sb.append(letter);
                    currentLetterIndex++;

                    if (currentLetterIndex == letterViews.size()) {
                        String enteredWord = sb.toString();
                        if (enteredWord.equals(selected_line[0].toLowerCase())) {
                            Toast.makeText(this, "You passed this, try next word!", Toast.LENGTH_SHORT).show();
                            result_value2++;
                            if (result_value2 == 2) {
                                Intent intent = new Intent(GameActivity2.this, GameActivity3.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(GameActivity2.this, GameActivity2.class);
                                intent.putExtra("result2", result_value2);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(this, "Try another word!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GameActivity2.this, GameActivity2.class);
                            intent.putExtra("result2", result_value2);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}