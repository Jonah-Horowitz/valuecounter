package net.hashjellyfish.valuecounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.hashjellyfish.valuecounter.vb.VariableBundle;

public class BundleSettingsActivity extends AppCompatActivity {
    public static final String NONE_OP = "None";
    public static final String[] OPERATION_TYPES = new String[] { "+", "-", "x", "=", NONE_OP };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_settings);

        Intent intent = getIntent();
        Object prevb = intent.getSerializableExtra("vbData");
        VariableBundle vb = null;
        if (prevb != null && (prevb instanceof VariableBundle)) {
            vb = (VariableBundle) prevb;
        }
        setData(vb);
    }

    /**
     * Sets this <code>Activity</code> to display the data in the given <code>VariableBundle</code>.
     * @param vb A <code>VariableBundle</code> to be edited here.
     */
    void setData(@Nullable VariableBundle vb) {
        if (vb==null) {
            // TODO: Display neutral text for null vb.
        } else {
            ((EditText)findViewById(R.id.bundle_name_field)).setText(vb.caption);
            ((EditText)findViewById(R.id.bundle_value_field)).setText(String.valueOf(vb.mainValue));
            if (vb.op1==null) {
                ((Button)findViewById(R.id.bundle_op1_type)).setText(NONE_OP);
                findViewById(R.id.bundle_op1_amount).setVisibility(View.GONE);
            } else {
                ((Button)findViewById(R.id.bundle_op1_type)).setText(vb.op1.opType());
                EditText op1Amount = findViewById(R.id.bundle_op1_amount);
                op1Amount.setVisibility(View.VISIBLE);
                op1Amount.setText(vb.op1.toString().substring(1));
            }
            if (vb.op2==null) {
                ((Button)findViewById(R.id.bundle_op2_type)).setText(NONE_OP);
                findViewById(R.id.bundle_op2_amount).setVisibility(View.GONE);
            } else {
                ((Button)findViewById(R.id.bundle_op2_type)).setText(vb.op2.opType());
                EditText op2Amount = findViewById(R.id.bundle_op2_amount);
                op2Amount.setVisibility(View.VISIBLE);
                op2Amount.setText(vb.op2.toString().substring(1));
            }
            if (vb.op3==null) {
                ((Button)findViewById(R.id.bundle_op3_type)).setText(NONE_OP);
                findViewById(R.id.bundle_op3_amount).setVisibility(View.GONE);
            } else {
                ((Button)findViewById(R.id.bundle_op3_type)).setText(vb.op3.opType());
                EditText op3Amount = findViewById(R.id.bundle_op3_amount);
                op3Amount.setVisibility(View.VISIBLE);
                op3Amount.setText(vb.op3.toString().substring(1));
            }
        }
    }

    /**
     * Pops up a dialog for the user to choose a type of operation, then assigns that operation's symbol to a <code>Button</code>.
     * @param viewId Must be the id of a <code>Button</code>.
     */
    protected void chooseOperationType(final int viewId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a type");
        builder.setItems(OPERATION_TYPES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Button)findViewById(viewId)).setText(OPERATION_TYPES[which]);
            }
        });
        builder.show();
    }
}
