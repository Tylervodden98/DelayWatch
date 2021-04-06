package com.example.delaywatch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TParserActivity extends AppCompatActivity {
    Button click;
    TextView data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_parser);

        click = (Button) findViewById(R.id.button);
        data = (TextView) findViewById(R.id.fetcheddata);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}