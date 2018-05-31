package net.hashjellyfish.valuecounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainSettingsActivity extends AppCompatActivity {

    protected static final int READ_REQUEST_CODE = 17;

    private SharedPreferences localPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        localPrefs = getDefaultSharedPreferences(this);

        if (savedInstanceState==null) {
            ((TextView) findViewById(R.id.vb_location)).setText(localPrefs
                    .getString("valueBundleLocation", MainActivity.DEFAULT_BUNDLES_LOCATION));
        } else {
            ((TextView)findViewById(R.id.vb_location)).setText(savedInstanceState
                    .getCharSequence("valueBundleLocation",localPrefs
                            .getString("valueBundleLocation",
                                    MainActivity.DEFAULT_BUNDLES_LOCATION)).toString());
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("valueBundleLocation",
                ((TextView)findViewById(R.id.vb_location)).getText());
    }

    /**
     * Returns to the main <code>Activity</code>.
     */
    protected void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Allows the user to select a directory.
     */
    protected void selectDirectory() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==READ_REQUEST_CODE) {
            if (resultCode==RESULT_OK) {
                ((TextView)findViewById(R.id.vb_location)).setText(data.getDataString());
            }
        }
    }

    /**
     * Saves edited properties.
     */
    protected void transferProperties() {
        localPrefs.edit()
                .putString("valueBundlesLocation",
                        ((TextView)findViewById(R.id.vb_location)).getText().toString())
                .apply();
    }
}
