package com.example.trabalho1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button buttonConsulta;
    ProgressDialog progressDialog;

    TextView result2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonConsulta = findViewById(R.id.btnBuscar);

        result2 = findViewById(R.id.txtView);
        buttonConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String api = "https://api.chucknorris.io/jokes/random";
                Consulta consulta = new Consulta();
                consulta.execute(api);
            }
        });
    }

    protected class Consulta extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer out = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    out.append(line + "\n");
               }
                is.close();
                return
                        out.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Buscando dados");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String dados) {
            try {
                parseJSON(dados);

                progressDialog.hide();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Falha na consulta", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }
        private void parseJSON(String data) {
            try {
                if (data.contains("erro")) {
                    Toast.makeText(MainActivity.this, "Falha na consulta", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject(data);
                    result2.setText(jsonObject.getString("value"));

                    }

                } catch (JSONException e) {
                e.printStackTrace();

        }
        }


    }
}