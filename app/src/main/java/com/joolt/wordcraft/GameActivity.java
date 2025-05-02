package com.joolt.wordcraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private List<TextView> letterViews = new ArrayList<>();
    private int currentLetterIndex = 0;
    String words_file = "words.txt";
    TextView word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        word = findViewById(R.id.word);
        LinearLayout letterContainer = findViewById(R.id.letterContainer);

        String selectedWord = getWord(words_file, generateNumberLine(1, 95));
        if (selectedWord == null || selectedWord.trim().isEmpty()) selectedWord = "apple";
        selectedWord = selectedWord.toLowerCase();
        word.setText(selectedWord);

        // Inflate views based on word length
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < selectedWord.length(); i++) {
            addText(inflater, letterContainer);
        }

        // Now buttons can be safely set up
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

        for (; index < word.length() && index < allButtons.size(); index++) {
            allButtons.get(index).setText(String.valueOf(word.charAt(index)));
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
                    currentLetterIndex++;
                }
            });
        }
    }
}
