package net.hashjellyfish.valuecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        TextView tv = findViewById(R.id.vb_location);
        tv.setText(MainActivity.localProps.getProperty("valueBundleLocation"));
    }
}
