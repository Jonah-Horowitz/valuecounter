package net.hashjellyfish.valuecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class MainSettingsActivity extends AppCompatActivity {

    protected static final int READ_REQUEST_CODE = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        ((TextView)findViewById(R.id.vb_location)).setText(MainActivity.localProps.getProperty("valueBundleLocation"));
        findViewById(R.id.select_location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDirectory();
            }
        });

        findViewById(R.id.save_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferProperties();
                returnToMain();
            }
        });
        findViewById(R.id.cancel_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });
    }

    protected void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void selectDirectory() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==READ_REQUEST_CODE) {
            if (resultCode==RESULT_OK) {
                ((TextView)findViewById(R.id.vb_location)).setText(data.getDataString());
            }
        }
    }

    protected void transferProperties() {
        MainActivity.localProps.setProperty("valueBundleLocation",String.valueOf(((TextView)findViewById(R.id.vb_location)).getText()));
        try {
            MainActivity.saveProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
