package com.example.einzelbeispiel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String HOST = "se2-submission.aau.at";
    private final int SERVER_PORT = 20080;
    private EditText et;
    private TextView tw;
    private String input;

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

        et = findViewById(R.id.textInput);
        tw = findViewById(R.id.responseText);

        Button b1 = findViewById(R.id.sendButton);
        b1.setOnClickListener(v -> {
            input = et.getText().toString();
            sendButtonClicked(input);
        });

        Button b2 = findViewById(R.id.calcButton);
        b2.setOnClickListener(v -> {
            input = et.getText().toString();
            // checking input
            if(input.isEmpty()) {
                tw.setText("Input must not be empty");
            } else if(isNumeric(input)) {
                calcButtonClicked(input);
            } else {
                tw.setText("Input not numeric");
            }
        });
    }

    // ensures that the input only contains digits, for the calc function
    private boolean isNumeric(String input) {
        for (char c : input.toCharArray()) {
            if(!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private void sendButtonClicked(String input) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(HOST, SERVER_PORT);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.write(input);
                out.newLine();
                // ensure data is sent
                out.flush();

                String result = in.readLine();

                runOnUiThread(() -> tw.setText(result));

                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void calcButtonClicked(String input) {
        List<Integer> nonPrimes = new ArrayList<>();
        for (char c :  input.toCharArray()) {
            // checking if digit is prime
            if(!isPrime(Character.getNumericValue(c))) {
                nonPrimes.add(Character.getNumericValue(c));
            }
        }
        // sorting numbers as asked
        Collections.sort(nonPrimes);
        tw.setText(convertToString(nonPrimes));
    }

    // converting int list to string and returning it
    private String convertToString(List<Integer> ints) {
        StringBuilder builder = new StringBuilder();
        for (int i : ints) {
            builder.append(i);
        }
        return builder.toString();
    }

    // standard prime function
    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}