package com.example.delaywatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Arrays;

public class DelayActivity extends AppCompatActivity {
    TextView delayText1;
    TextView delayText2;
    TextView delayText3;
    TextView delayText4;
    TextView delayText5;
    TextView delayText6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);
        delayText1 = (TextView)findViewById(R.id.allDelaysText);
        delayText2 = (TextView)findViewById(R.id.allDelaysText2);
        delayText3 = (TextView)findViewById(R.id.allDelaysText3);
        delayText4 = (TextView)findViewById(R.id.allDelaysText4);
        delayText5 = (TextView)findViewById(R.id.allDelaysText5);
        delayText6 = (TextView)findViewById(R.id.allDelaysText6);

        Button updateButton = (Button)findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updateDelay().execute();
            }
        });
    }

    public class updateDelay extends AsyncTask<Void, Void, Void> {
        String genText1;
        String genText2;
        String genText3;
        String genText4;
        String genText5;
        String genText6;
        int count;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://ttc.ca/mobile/all_service_alerts.jsp").get();
                String backText1 = doc.text();
                String backText2 = backText1.substring(54, backText1.indexOf("Please note"));
                String[] backText = backText2.split("Service Alert: ");
                genText1 = "SERVICE ALERT: " + backText[1];
                genText2 = "SERVICE ALERT: " + backText[2];
                genText3 = "SERVICE ALERT: " + backText[3];
                genText4 = "SERVICE ALERT: " + backText[4];
                genText5 = "SERVICE ALERT: " + backText[5];
                genText6 = "SERVICE ALERT: " + backText[6];
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            delayText1.setText(genText1);
            delayText2.setText(genText2);
            delayText3.setText(genText3);
            delayText4.setText(genText4);
            delayText5.setText(genText5);
            delayText6.setText(genText6);
        }
    }

}