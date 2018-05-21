package net.hashjellyfish.valuecounter;

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
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    public static final File SETTINGS_FILE = new File(new File(System.getProperty("user.home"),
            "valuecounter"),"local_settings.properties");
    public static final String VALUE_BUNDLES_FILENAME = "valueBundles.json";

    private RecyclerView main_list_rec;
    private VariableAdapter main_list_adapter;
    private RecyclerView.LayoutManager main_list_layout_manager;
    private Properties localProps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        main_list_rec = (RecyclerView) findViewById(R.id.main_list_recycler);
        main_list_rec.setHasFixedSize(true);

        main_list_layout_manager = new LinearLayoutManager(this);
        main_list_rec.setLayoutManager(main_list_layout_manager);

        List<VariableBundle> variableList = new ArrayList<VariableBundle>();
        try {
            loadProperties();
//            variableList = loadBundles(new File(localProps.getProperty("valueBundleLocation"),VALUE_BUNDLES_FILENAME));
            variableList = VariableBundle.loadTestBundles();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        main_list_adapter = new VariableAdapter(variableList);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves both properties and bundles into their appropriate locations.
     * @throws IOException If an I/O error occurred.
     */
    public void saveState() throws IOException {
        saveProperties();
        saveBundles(main_list_adapter.dataList,new File(localProps.getProperty("valueBundleLocation"),
                VALUE_BUNDLES_FILENAME));
    }

    /**
     * Loads the default <code>Properties</code> file or instantiates the defaults if the file
     * doesn't exist.
     * @throws IOException If an I/O error occurred.
     */
    public void loadProperties() throws IOException {
        if (!SETTINGS_FILE.exists()) {
            localProps = new Properties();
            localProps.put("valueBundleLocation",SETTINGS_FILE.getParentFile().toString());
            // TODO: Set up default properties.
        } else if (!SETTINGS_FILE.isFile()) {
            throw new IOException("Cannot read local settings: Expected settings file is not a file.");
        }
        localProps = new Properties();
        FileReader in = new FileReader(SETTINGS_FILE);
        localProps.load(in);
        in.close();
    }

    /**
     * Saves the current <code>Properties</code> to the default file.
     * @throws IOException If an I/O error occurred.
     */
    public void saveProperties() throws IOException {
        if (SETTINGS_FILE.getParentFile().exists() && !SETTINGS_FILE.getParentFile().isDirectory()) {
            throw new IOException("Expected settings parent directory exists and is not a directory.");
        } else if (!SETTINGS_FILE.getParentFile().exists() && !SETTINGS_FILE.getParentFile().mkdirs()) {
            throw new IOException("Cannot create settings parent directory.");
        }
        if (SETTINGS_FILE.exists() && !SETTINGS_FILE.isFile()) {
            throw new IOException("Expected settings file exists and is not a file.");
        } else {
            FileWriter out = new FileWriter(SETTINGS_FILE);
            localProps.store(out,"Saved settings for ValueCounter app.");
            out.close();
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
    public static List<VariableBundle> loadBundles(File storage) throws IOException {
        if (storage==null || !storage.exists()) {
            return new ArrayList<VariableBundle>();
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
    public static void saveBundles(List<VariableBundle> vbs, File storage) throws IOException {
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
}
