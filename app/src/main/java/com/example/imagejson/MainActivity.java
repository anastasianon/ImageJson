package com.example.imagejson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    Button btn;
    String futureJokeString = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.image);
        btn = findViewById(R.id.btnClick);

        btn.setOnClickListener(view -> new JokeLoader().execute());
//        Picasso.with(this)
//                .load("")
//                .placeholder(R.drawable.user_placeholder)
//                .error(R.drawable.user_placeholder_error)
//                .into(img);

    }

    private class JokeLoader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String jsonString = getJson("https://kitsu.io/api/edge/anime?filter[text]=cowboy%20bebop");

            try {
                JSONObject jsonObject = new JSONObject(jsonString)
                        .getJSONArray("data")
                        .getJSONObject(0)
                        .getJSONObject("attributes")
                        .getJSONObject("posterImage");
                futureJokeString = jsonObject.getString("large");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
//            futureJokeString = "";
//            txt.setText("Loading...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Glide
                    .with(img)
                    .load(futureJokeString)
                    .into(img);
            super.onPostExecute(aVoid);


        }
    }

    private String getJson(String link) {
        String data = "";
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                        "utf-8"));
                data = r.readLine();
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
