package com.example.taxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    TextView signup;
    Button SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = findViewById(R.id.opensignup);
        signup.setOnClickListener(v -> {
            openSignUp();
        });
        SignIn = findViewById(R.id.SignIn);
        SignIn.setOnClickListener(v -> {
            SignIn();
        });
    }

    private void SignIn() {
        EditText username = findViewById(R.id.Name);
        EditText password = findViewById(R.id.Password);
        String uname = username.getText().toString();
        String pass = password.getText().toString();
        final String[] stte = {null};
        String us;
        final int[] token = new int[1];
        if(uname.trim().equals("") && pass.trim().equals("")) {
             Toast.makeText(getApplicationContext(),"Data is clear", Toast.LENGTH_LONG).show();
        } else {


            final JSONObject[] data = {new JSONObject()};
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        data[0].put("username", uname);
                        data[0].put("password", pass);
                        URL url = new URL("http://cars.areas.su/login");;
                        HttpURLConnection con = (HttpURLConnection)url.openConnection();
                        con.setRequestProperty("Content-Type", "application/json; utf-8");
                        con.setRequestMethod("POST");
                        con.setDoOutput(true);
                        Log.d("Test", data[0].toString());
                        OutputStream os = con.getOutputStream();
                        Log.d("Test","I created os");
                        byte[] input = data[0].toString().getBytes(StandardCharsets.UTF_8);
                        os.write(input,0,input.length);
                        Log.d("Check","I send data");
                        try(BufferedReader br = new BufferedReader(
                                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine = null;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            Log.d("Answer",response.toString());
                            JSONObject answer = new JSONObject(String.valueOf(response));
                            if(!answer.getJSONObject("notice").getString("token").toString().equals("")) {
                                Integer token = answer.getJSONObject("notice").getInt("token");
                                Intent intent = new Intent(getApplicationContext(), StartScreen.class);
                                intent.putExtra("token", token);
                                intent.putExtra("username",uname);
                                startActivity(intent);
                                finish();

                            }


                        }
                        Log.d("Test","I read data");


                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    }

    private void openSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }
}