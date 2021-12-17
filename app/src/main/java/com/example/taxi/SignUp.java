package com.example.taxi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SignUp extends AppCompatActivity {
    EditText name, email, password,password2;
    Button signup;
    private String[] stte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = findViewById(R.id.Sign_Up);
        signup.setOnClickListener(v -> {
            Signup();
        });
    }

    @SuppressLint("ShowToast")
    private void Signup() {
        name = findViewById(R.id.Name);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        password2 = findViewById(R.id.Password2);
        String nameStr = name.getText().toString(),
               emailStr = email.getText().toString(),
               passwordStr = password.getText().toString(),
               password2Str = password2.getText().toString();
        if(!password2Str.equals(passwordStr)) {
            Toast.makeText(getApplicationContext(),"Пароли не совпадают",Toast.LENGTH_LONG).show();
        }
        else {
            if(password2Str.trim().equals("") && passwordStr.trim().equals("") && nameStr.trim().equals("") && emailStr.trim().equals("")) {
                Toast.makeText(getApplicationContext(),"Не все данные введены",Toast.LENGTH_LONG).show();
            }
            else {
                JSONObject data = new JSONObject();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            data.put("username", nameStr);
                            data.put("password", passwordStr);
                            data.put("email", emailStr);
                            URL url = new URL("http://cars.areas.su/signup");
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestProperty("Content-Type", "application/json; utf-8");
                            con.setRequestMethod("POST");
                            con.setDoOutput(true);
                            Log.d("Test", data.toString());
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
                                if(!answer.getJSONObject("notice").getString("token").toString().equals("")) {
                                    stte[0] = answer.getJSONObject("notice").getString("token").toString();

                                }
                            }
                            Log.d("Test", "I read data");


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }});
                if(stte[0].equals("Success")) {
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Такой пользователь уже существует", Toast.LENGTH_LONG);
                }
            }}}}





