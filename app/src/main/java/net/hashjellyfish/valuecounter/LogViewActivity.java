package net.hashjellyfish.valuecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

public class LogViewActivity extends AppCompatActivity {

    private Bundle savedState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);

        CharSequence fullLog = "";
        if (savedInstanceState==null) {
            Intent intent = getIntent();
            fullLog = TextUtils.join("\r\n", intent.getStringArrayListExtra("log"));
            savedState = intent.getBundleExtra("savedState");
        } else {
            fullLog = savedInstanceState.getCharSequence("fullLog","");
            savedState = savedInstanceState.getBundle("savedState");
        }
        ((TextView)findViewById(R.id.log_display)).setText(fullLog);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("fullLog", ((TextView)findViewById(R.id.log_display)).getText());
        outState.putBundle("savedState", savedState);
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
