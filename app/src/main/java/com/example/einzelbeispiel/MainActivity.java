package com.example.einzelbeispiel;

import android.icu.util.Output;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private String result;


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
        Button b1 = findViewById(R.id.sendButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButtonClicked();
            }
        });
        Button b2 = findViewById(R.id.calcButton);
        b2.setOnClickListener(v -> calcButtonClicked());
    }

    private void sendButtonClicked() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText et = findViewById(R.id.textInput);
                String data = et.getText().toString();
                try {
                    Socket s = new Socket("se2-submission.aau.at", 20080);
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    out.write(data);

                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    result = in.readLine();
                    TextView tw = findViewById(R.id.responseText);

                    Log.d("MainActivity", "Server Response: " + result);

                    runOnUiThread(() -> {
                        tw.setText(result);
                    });

                    in.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void calcButtonClicked() {
        EditText et = findViewById(R.id.textInput);
        String input = et.getText().toString();
        String result = "";


    }
}