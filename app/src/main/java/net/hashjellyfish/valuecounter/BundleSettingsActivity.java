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
import net.hashjellyfish.valuecounter.vb.ops.EqualsN;
import net.hashjellyfish.valuecounter.vb.ops.MinusN;
import net.hashjellyfish.valuecounter.vb.ops.PlusN;
import net.hashjellyfish.valuecounter.vb.ops.TimesN;

public class BundleSettingsActivity extends AppCompatActivity {
    public static final String NONE_OP = "None";
    public static final String[] OPERATION_TYPES = new String[] {PlusN.OP_ID, MinusN.OP_ID,
            TimesN.OP_ID, EqualsN.OP_ID, NONE_OP };

    private int dataPosition;
    private int originalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_settings);

        if (savedInstanceState==null) {
            Intent intent = getIntent();
            Object preVB = intent.getSerializableExtra("vbData");
            VariableBundle vb = null;
            if (preVB != null && (preVB instanceof VariableBundle)) {
                vb = (VariableBundle)preVB;
                originalValue = vb.mainValue;
            }
            setData(vb);
            dataPosition = intent.getIntExtra("dataPosition",-1);
        } else {
            setData((VariableBundle)savedInstanceState.getSerializable("savedVB"));
            dataPosition = savedInstanceState.getInt("dataPosition");
            originalValue = savedInstanceState.getInt("originalValue");
        }
        setListener(R.id.bundle_op1_type, R.id.bundle_op1_amount);
        setListener(R.id.bundle_op2_type, R.id.bundle_op2_amount);
        setListener(R.id.bundle_op3_type, R.id.bundle_op3_amount);
        findViewById(R.id.save_bundle_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVB();
            }
        });
        findViewById(R.id.cancel_bundle_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelVB();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("savedVB",makeVB());
        outState.putInt("dataPosition", dataPosition);
        outState.putInt("originalValue", originalValue);
    }

    /**
     * Turns the data in the various fields of this activity into a <code>VariableBundle</code>.
     * @return A <code>VariableBundle</code> consisting of the information in this <code>Activity</code>.
     */
    private VariableBundle makeVB() {
        VariableBundle tempBundle = new VariableBundle();
        tempBundle.caption = ((EditText)findViewById(R.id.bundle_name_field)).getText().toString();
        tempBundle.mainValue = Integer.parseInt(((EditText)findViewById(R.id.bundle_value_field)).getText().toString());
        tempBundle.op1 = VariableBundle.parseOperation(((Button)findViewById(R.id.bundle_op1_type))
                .getText().toString(), ((EditText)findViewById(R.id.bundle_op1_amount)).getText()
                .toString());
        tempBundle.op2 = VariableBundle.parseOperation(((Button)findViewById(R.id.bundle_op2_type))
                .getText().toString(), ((EditText)findViewById(R.id.bundle_op2_amount)).getText()
                .toString());
        tempBundle.op3 = VariableBundle.parseOperation(((Button)findViewById(R.id.bundle_op3_type))
                .getText().toString(), ((EditText)findViewById(R.id.bundle_op3_amount)).getText()
                .toString());
        return tempBundle;
    }

    /**
     * Returns the data from this <code>Activity</code> as its result.
     */
    private void saveVB() {
        Intent result = new Intent();
        result.putExtra("vbData", makeVB());
        result.putExtra("dataPosition", dataPosition);
        result.putExtra("originalValue", originalValue);
        setResult(RESULT_OK, result);
        finish();
    }

    /**
     * Cancels this edit.
     */
    private void cancelVB() {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    /**
     * Sets up the click listener for a given <code>Button</code> and <code>EditText</code> pair.
     * @param buttonId Must be the id of a <code>Button</code>.
     * @param textId Should be paired with the above <code>Button</code>, typically an <code>EditText</code>.
     */
    private void setListener(final int buttonId, final int textId) {
        findViewById(buttonId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseOperationType(buttonId, textId);
            }
        });
    }

    /**
     * Sets this <code>Activity</code> to display the data in the given <code>VariableBundle</code>.
     * @param vb A <code>VariableBundle</code> to be edited here.
     */
    private void setData(@Nullable VariableBundle vb) {
        if (vb==null) {
            for (int buttonId : new int[] { R.id.bundle_op1_type, R.id.bundle_op2_type,
                    R.id.bundle_op3_type }) {
                ((Button)findViewById(buttonId)).setText(NONE_OP);
            }
            for (int textId : new int[] { R.id.bundle_op1_amount, R.id.bundle_op2_amount,
                    R.id.bundle_op3_amount }) {
                findViewById(textId).setVisibility(View.GONE);
            }
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
     * @param buttonId Must be the id of a <code>Button</code>.
     * @param textId Must be the id of an <code>EditText</code>.
     */
    protected void chooseOperationType(final int buttonId, final int textId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a type");
        builder.setItems(OPERATION_TYPES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Button)findViewById(buttonId)).setText(OPERATION_TYPES[which]);
                findViewById(textId).setVisibility(which==OPERATION_TYPES.length-1 ? View.GONE : View.VISIBLE);
            }
        });
        builder.show();
    }
}
