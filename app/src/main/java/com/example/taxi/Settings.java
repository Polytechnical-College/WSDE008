package com.example.taxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Settings extends AppCompatActivity {
    TextView email, name, paid, hours, exit;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        exit = findViewById(R.id.exit);
        email = findViewById(R.id.Email);
        name = findViewById(R.id.Name);
        paid = findViewById(R.id.paid);
        hours = findViewById(R.id.hours);
        Integer token = getIntent().getExtras().getInt("token");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject data = new JSONObject();
                    data.put("token", token);
                    URL url = new URL("http://cars.areas.su/profile");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    OutputStream os = con.getOutputStream();
                    byte[] input = data.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        JSONObject answer = new JSONObject(String.valueOf(response));
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    name.setText(answer.getString("firstname") + " " + answer.getString("secondname"));
                                    email.setText(answer.getString("email"));
                                    hours.setText(answer.getString("timeDrive") + " hours");
                                    paid.setText("$" + answer.getString("cash"));
                                    username = answer.getString("username");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        exit.setOnClickListener(v -> {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
            URL url = null;
            try {
                JSONObject data = new JSONObject();
                data.put("username",username);
                url = new URL("http://cars.areas.su/logout");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                byte[] input = data.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.d("Answer",response.toString());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
                }}
             );
    });
    }
}