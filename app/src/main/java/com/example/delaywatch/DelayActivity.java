package com.example.delaywatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DelayActivity extends AppCompatActivity {
    TextView delayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);

        delayText = (TextView)findViewById(R.id.allDelaysText);
        Button updateButton = (Button)findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updateDelay().execute();
            }
        });
    }

    public class updateDelay extends AsyncTask<Void, Void, Void> {
        String genText;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("http://ttc.ca/mobile/all_service_alerts.jsp").get();
                genText = doc.text();
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            delayText.setText(genText);
        }
    }

}