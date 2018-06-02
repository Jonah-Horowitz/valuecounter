package net.hashjellyfish.valuecounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.hashjellyfish.valuecounter.vb.VariableBundle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    public static final String DEFAULT_BUNDLES_LOCATION = (new File(System.getProperty("user.home")
            ,"valuecounter")).toString();
    public static final String VALUE_BUNDLES_FILENAME = "valueBundles.sqlite";

    protected VariableAdapter main_list_adapter;
    protected static SharedPreferences localPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView main_list_rec = findViewById(R.id.main_list_recycler);
        main_list_rec.setHasFixedSize(true);

        RecyclerView.LayoutManager main_list_layout_manager = new LinearLayoutManager(this);
        main_list_rec.setLayoutManager(main_list_layout_manager);

        List<VariableBundle> variableList = new ArrayList<>();
        try {
            loadProperties();
            variableList = loadBundles(new File(localPrefs.getString("valueBundleLocation",
                    DEFAULT_BUNDLES_LOCATION),VALUE_BUNDLES_FILENAME));
            variableList = VariableBundle.loadTestBundles();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        main_list_adapter = new VariableAdapter(this, variableList);
        main_list_rec.setAdapter(main_list_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            loadSettings();
            return true;
        } else if (id == R.id.action_add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==VariableAdapter.BUNDLE_SETTINGS_RESULT_CODE) {
            if (resultCode==RESULT_OK) {
                int dataPos = data.getIntExtra("dataPosition", -1);
                if (dataPos==-1) {
                    return;
                }
                main_list_adapter.dataList.remove(dataPos);
                main_list_adapter.dataList.add(dataPos,(VariableBundle)data.getSerializableExtra("vbData"));
                main_list_adapter.notifyItemChanged(dataPos);
            }
        }
    }

    /**
     * Saves both properties and bundles into their appropriate locations.
     * @throws IOException If an I/O error occurred.
     */
    protected void saveState() throws IOException {
        saveBundles(main_list_adapter.dataList,new File(localPrefs
                .getString("valueBundleLocation",DEFAULT_BUNDLES_LOCATION)
                ,VALUE_BUNDLES_FILENAME));
    }

    /**
     * Loads the default <code>Properties</code> file or instantiates the defaults if the file
     * doesn't exist.
     */
    protected void loadProperties() {
        localPrefs = getDefaultSharedPreferences(this);
        if (!localPrefs.contains("valueBundleLocation")) {
            localPrefs.edit()
                    .putString("valueBundleLocation",DEFAULT_BUNDLES_LOCATION)
                    .apply();
        }
    }

    /**
     * Loads all stored <code>VariableBundle</code>s from the indicated <code>File</code> if the
     * file exists.
     * @param storage A <code>File</code> containing a JSON serialization of a <code>List</code> of
     *                <code>VariableBundle</code>s.
     * @return An <code>ArrayList</code> of <code>VariableBundle</code>s. This will be empty if
     *                <code>storage</code> does not exist, is null, or is an empty file.
     * @throws IOException If an I/O error occurred.
     */
    protected static List<VariableBundle> loadBundles(File storage) throws IOException {
        if (storage==null || !storage.exists()) {
            return new ArrayList<>();
        } else if (!storage.isFile()) {
            throw new IOException("Expected bundle file is not a file.");
        } else {
            FileReader in = new FileReader(storage);
            Gson gson = (new GsonBuilder()).serializeNulls().create();
            try {
                VariableBundle[] vbs = gson.fromJson(in, VariableBundle[].class);
                return Arrays.asList(vbs);
            } catch (JsonIOException e) {
                throw new IOException("Something went wrong reading from the bundle file.");
            } catch (JsonSyntaxException f) {
                throw new IOException("Bundle file is not correct JSON syntax.");
            }
        }
    }

    /**
     * Saves all <code>VariableBundle</code>s into the indicated <code>File</code>, overwriting if
     * it already exists.
     * @param storage The <code>File</code> where the bundles are to be stored.
     * @throws IOException If an I/O error occurred.
     */
    protected static void saveBundles(List<VariableBundle> vbs, File storage) throws IOException {
        if (storage==null || storage.exists() && !storage.isFile()) {
            throw new IOException("Expected bundle file exists and is not a file.");
        } else {
            FileWriter out = new FileWriter(storage);
            Gson gson = (new GsonBuilder()).setPrettyPrinting().serializeNulls().create();
            try {
                gson.toJson(vbs==null ? new ArrayList<VariableBundle>() : vbs, out);
            } catch (JsonIOException e) {
                throw new IOException("There was an error writing the bundles to a file.");
            }
        }
    }

    /**
     * Intends to load the main settings <code>Activity</code> for this app.
     */
    protected void loadSettings() {
        Intent intent = new Intent(this, MainSettingsActivity.class);
        startActivity(intent);
    }
}
