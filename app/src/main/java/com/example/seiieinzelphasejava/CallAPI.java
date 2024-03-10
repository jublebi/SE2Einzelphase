package com.example.seiieinzelphasejava;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class CallAPI extends Thread {
    private String domain;
    private int port;
    private String inputNumber;
    private NetworkTaskListener listener;

    public CallAPI(String domain, int port, String inputNumber, NetworkTaskListener listener) {
        this.domain = domain;
        this.port = port;
        this.inputNumber = inputNumber;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(domain, port);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Log.println(Log.INFO, "TAG", "Write Data");
            out.write(inputNumber + "\n");
            out.flush();
            Log.println(Log.INFO, "TAG", "Read Response");
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            Log.println(Log.INFO, "TAG", "Close Socket");
            in.close();
            out.close();
            socket.close();

            if (listener != null) {
                listener.onSuccess(response.toString());
            }
        } catch (IOException e) {
            if (listener != null) {
                listener.onError(e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}
