package com.example.seiieinzelphasejava;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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

        Button sendButtonServer = findViewById(R.id.sendButtonServer);
        Button sendButtonLocal = findViewById(R.id.sendButtonLocal);
        TextView serverResponse = findViewById(R.id.serverResponse);
        EditText input = findViewById(R.id.inputNumber);

        sendButtonServer.setOnClickListener(view -> {
            Log.println(Log.INFO, "TAG", "Start Server Request");
            String inputNumber = input.getText().toString();
            if (!inputNumber.isEmpty()) {
//                CallAPI callAPI = new CallAPI("10.0.2.2", 20080, inputNumber, new NetworkTaskListener(){
                CallAPI callAPI = new CallAPI("se2-submission.aau.at", 20080, inputNumber, new NetworkTaskListener() {

                    @Override
                    public void onSuccess(String response) {
                        runOnUiThread(() -> {
                            serverResponse.setText(response);
                            Log.println(Log.INFO, "TAG", "Success");
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> {
                            Log.println(Log.ERROR, "TAG", Objects.requireNonNull(e.getMessage()));
                        });
                    }
                });
                callAPI.start();
            }
        });

        sendButtonLocal.setOnClickListener(view -> {
            String inputText = input.getText().toString();
            if (!inputText.isEmpty()) {
                int alternatingSum = calculateAlternatingSum(Integer.parseInt(input.getText().toString()));
                serverResponse.setText(String.valueOf(alternatingSum));
                if(alternatingSum % 2 == 0){
                    serverResponse.setText(String.valueOf("Gerade"));
                }else{
                    serverResponse.setText(String.valueOf("Ungerade"));
                }
            }
        });
    }

    public int calculateAlternatingSum(int input) {
        int result = 0;

        boolean add = true;

        while (input > 0) {
            int current = input % 10;
            input /= 10;

            if (add) {
                result += current;
            } else {
                result -= current;
            }
            add = !add;
        }
        return result;
    }
}