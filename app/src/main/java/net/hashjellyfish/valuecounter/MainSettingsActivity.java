package net.hashjellyfish.valuecounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Allows the user to edit any settings which are independent of any <code>VariableBundle</code>.
 * Note: This is currently not usable since there are no such settings, but it is not being deleted because such settings are anticipated again in future.
 */
public class MainSettingsActivity extends AppCompatActivity {

    protected static final int READ_REQUEST_CODE = 17;

    private SharedPreferences localPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        localPrefs = getDefaultSharedPreferences(this);

        final String defLoc = getDatabasePath(DBHelper.VALUE_BUNDLES_FILENAME).toString();
        if (savedInstanceState==null) {
            ((TextView) findViewById(R.id.vb_location)).setText(localPrefs
                    .getString("valueBundleLocation", defLoc));
        } else {
            ((TextView)findViewById(R.id.vb_location)).setText(savedInstanceState
                    .getCharSequence("valueBundleLocation",localPrefs
                            .getString("valueBundleLocation",
                                    defLoc)).toString());
        }
        findViewById(R.id.reset_to_default_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)findViewById(R.id.vb_location))
                        .setText(defLoc);
            }
        });
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
                // TODO: Refresh the main list.
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
        finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves edited properties.
     */
    protected void transferProperties() {
        localPrefs.edit()
                .putString("valueBundleLocation",
                        ((TextView)findViewById(R.id.vb_location)).getText().toString())
                .apply();
    }

    // TODO: Include the choice to copy or move the list, or start a new one.
}
