package net.hashjellyfish.valuecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Extremely simple <code>Activity</code> which allows the user to view the existing log for a
 * particular <code>VariableBundle</code>.
 */
public class LogViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);

        CharSequence fullLog;
        if (savedInstanceState==null) {
            Intent intent = getIntent();
            fullLog = TextUtils.join("\r\n", intent.getStringArrayListExtra("log"));
        } else {
            fullLog = savedInstanceState.getCharSequence("fullLog","");
        }
        if (fullLog.length()==0) {
            TextView tv = findViewById(R.id.log_display);
            tv.setText(R.string.empty_log);
        } else {
            ((TextView) findViewById(R.id.log_display)).setText(fullLog);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("fullLog", ((TextView)findViewById(R.id.log_display)).getText());
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
}
