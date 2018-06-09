package net.hashjellyfish.valuecounter;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;

import net.hashjellyfish.valuecounter.vb.VariableBundle;
import net.hashjellyfish.valuecounter.VBContract.VBEntry;
import net.hashjellyfish.valuecounter.vb.ops.Operation;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_CODE = 31;

    protected VariableAdapter main_list_adapter;
    protected static SharedPreferences localPrefs = null;
    public static DBHelper dbHelper = null;
    protected File defaultBundlesLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getApplication());
        defaultBundlesLocation = super.getDatabasePath(DBHelper.VALUE_BUNDLES_FILENAME);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView main_list_rec = findViewById(R.id.main_list_recycler);
        main_list_rec.setHasFixedSize(true);

        RecyclerView.LayoutManager main_list_layout_manager = new LinearLayoutManager(this);
        main_list_rec.setLayoutManager(main_list_layout_manager);

        loadProperties();

        ArrayList<VariableBundle> variableList;
        if (savedInstanceState==null) {
            variableList = loadBundles();
        } else {
            @SuppressWarnings("unchecked")
            ArrayList<VariableBundle> temp = (ArrayList<VariableBundle>)savedInstanceState.getSerializable("fullVariableList");
            variableList = temp;
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
            Intent intent = new Intent(this, BundleSettingsActivity.class);
            intent.putExtra("vbData", (Serializable)null);
            startActivityForResult(intent, VariableAdapter.BUNDLE_SETTINGS_RESULT_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==VariableAdapter.BUNDLE_SETTINGS_RESULT_CODE) {
            if (resultCode==RESULT_OK) {
                int dataPos = data.getIntExtra("dataPosition", -1);
                switch (data.getIntExtra("instruction", 0)) {
                    case BundleSettingsActivity.ADD_INSTRUCTION: {
                        VariableBundle vb = (VariableBundle) data.getSerializableExtra("vbData");
                        saveOneBundle(main_list_adapter.dataList.size() - 1, vb);
                        main_list_adapter.dataList.add(vb);
                        main_list_adapter.notifyDataSetChanged();
                        break;
                    }
                    case BundleSettingsActivity.DELETE_INSTRUCTION: {
                        VariableBundle vb = main_list_adapter.dataList.remove(dataPos);
                        main_list_adapter.notifyDataSetChanged();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(VBEntry.TABLE_NAME, VBEntry._ID+"=?",new String[]
                                {String.valueOf(vb.getId())});
                        main_list_adapter.notifyDataSetChanged();
                        for (int i = dataPos; i < main_list_adapter.dataList.size(); i++) {
                            saveOneBundle(i, main_list_adapter.dataList.get(i), db);
                        }
                        break;
                    }
                    default: {
                        VariableBundle vb = (VariableBundle)data.getSerializableExtra("vbData");
                        saveOneBundle(dataPos, vb);
                        main_list_adapter.dataList.set(dataPos, vb);
                        main_list_adapter.notifyItemChanged(dataPos);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fullVariableList", main_list_adapter.dataList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int grantResults[]) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE: {
                checkPermissions(); // TODO: Set up the database here.
            }
        }
    }

    @Override
    public File getDatabasePath(String name) {
/*        if (name.equalsIgnoreCase(DBHelper.VALUE_BUNDLES_FILENAME)) {
            return new File(localPrefs.getString("valueBundleLocation", defaultBundlesLocation.toString()));
        }/**/ // TODO: Fix this (with permissions).
        return super.getDatabasePath(name);
    }

    // TODO: Make a "refresh list" method.

    /**
     * Loads the default <code>Properties</code> file or instantiates the defaults if the file
     * doesn't exist.
     */
    protected void loadProperties() {
        localPrefs = getDefaultSharedPreferences(this);
        if (!localPrefs.contains("valueBundleLocation")) {
            localPrefs.edit()
                    .putString("valueBundleLocation",defaultBundlesLocation.toString())
                    .apply();
        }
        if (!localPrefs.getString("valueBundleLocation",defaultBundlesLocation.toString()).equalsIgnoreCase(defaultBundlesLocation.toString())) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE); // TODO: Maybe include an explanation.
            } else {
                checkPermissions(); // TODO: Set up the database here.
            }
        }
    }

    private void checkPermissions() { // TODO: Delete this method.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No permission for write access.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permissions for write access!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads all stored <code>VariableBundle</code>s from the appropriate <code>File</code> if the
     * file exists.
     * @return An <code>ArrayList</code> of <code>VariableBundle</code>s. This will be empty if
     *                <code>storage</code> does not exist, is null, or is an empty file.
     */
    @NotNull
    protected ArrayList<VariableBundle> loadBundles() { // TODO: Use the filename.
        if (dbHelper==null) dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { VBEntry._ID, VBEntry.COLUMN_NAME_POSITION,
                VBEntry.COLUMN_NAME_CAPTION, VBEntry.COLUMN_NAME_VALUE, VBEntry.COLUMN_NAME_OP1,
                VBEntry.COLUMN_NAME_OP2, VBEntry.COLUMN_NAME_OP3, VBEntry.COLUMN_NAME_LOG_LENGTH,
                VBEntry.COLUMN_NAME_LOG };
        String sortOrder = VBEntry.COLUMN_NAME_POSITION + " ASC";
        Cursor cursor = db.query(VBEntry.TABLE_NAME, projection, null, null,
                null, null, sortOrder);
        ArrayList<VariableBundle> bundles = new ArrayList<>();
        while (cursor.moveToNext()) {
            bundles.add((new VariableBundle())
                    .setId(cursor.getInt(cursor.getColumnIndex(VBEntry._ID)))
                    .setCaption(cursor.getString(cursor.getColumnIndex(VBEntry.COLUMN_NAME_CAPTION)))
                    .setValue(cursor.getInt(cursor.getColumnIndex(VBEntry.COLUMN_NAME_VALUE)))
                    .setOp(1,cursor.getString(cursor.getColumnIndex(VBEntry.COLUMN_NAME_OP1)))
                    .setOp(2,cursor.getString(cursor.getColumnIndex(VBEntry.COLUMN_NAME_OP2)))
                    .setOp(3,cursor.getString(cursor.getColumnIndex(VBEntry.COLUMN_NAME_OP3)))
                    .setLogLength(cursor.getInt(cursor.getColumnIndex(VBEntry.COLUMN_NAME_LOG_LENGTH)))
                    .setLog(TextUtils.split(cursor.getString(cursor.getColumnIndex(VBEntry.COLUMN_NAME_LOG)),"\r\n"))
            );
        }
        cursor.close();
        return bundles;
    }

    /**
     * Saves a single <code>VariableBundle</code> into the appropriate database.
     * @param pos The position of the bundle in the list.
     * @param vb The bundle to be updated or added.
     */
    protected void saveOneBundle(int pos, VariableBundle vb) {
        saveOneBundle(pos, vb, dbHelper.getWritableDatabase());
    }

    /**
     * Saves a single <code>VariableBundle</code> into the appropriate database.
     * @param pos The position of the bundle in the list.
     * @param vb The bundle to be updated or added.
     * @param db The <code>SQLiteDatabase</code> in which to save the bundle.
     */
    private void saveOneBundle(int pos, VariableBundle vb, SQLiteDatabase db) {
        ContentValues vals = new ContentValues();
        vals.put(VBEntry.COLUMN_NAME_POSITION, pos);
        vals.put(VBEntry.COLUMN_NAME_CAPTION, vb.getCaption());
        vals.put(VBEntry.COLUMN_NAME_VALUE, vb.getValue());
        Operation<Integer> op = vb.getOperation(1);
        vals.put(VBEntry.COLUMN_NAME_OP1, op==null ? null : op.toString());
        op = vb.getOperation(2);
        vals.put(VBEntry.COLUMN_NAME_OP2, op==null ? null : op.toString());
        op = vb.getOperation(3);
        vals.put(VBEntry.COLUMN_NAME_OP3, op==null ? null : op.toString());
        vals.put(VBEntry.COLUMN_NAME_LOG_LENGTH, vb.getLogLength());
        vals.put(VBEntry.COLUMN_NAME_LOG, TextUtils.join("\r\n", vb.getLog()));
        if (vb.getId()==-1) {
            vb.setId(db.insert(VBEntry.TABLE_NAME, null, vals));
        } else {
            String selection = VBEntry._ID + "=?";
            String[] selectionArgs = { String.valueOf(vb.getId()) };
            db.update(VBEntry.TABLE_NAME, vals, selection, selectionArgs);
        }
    }

    /**
     * Intends to load the main settings <code>Activity</code> for this app.
     */
    protected void loadSettings() {
        Intent intent = new Intent(this, MainSettingsActivity.class);
        startActivity(intent);
    }

    // TODO: Allow for moving (reordering) bundles around.
}
