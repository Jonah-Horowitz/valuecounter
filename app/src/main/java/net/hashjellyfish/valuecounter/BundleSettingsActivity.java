package net.hashjellyfish.valuecounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.hashjellyfish.valuecounter.vb.VariableBundle;
import net.hashjellyfish.valuecounter.vb.ops.EqualsN;
import net.hashjellyfish.valuecounter.vb.ops.MinusN;
import net.hashjellyfish.valuecounter.vb.ops.Operation;
import net.hashjellyfish.valuecounter.vb.ops.PlusN;
import net.hashjellyfish.valuecounter.vb.ops.TimesN;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BundleSettingsActivity extends AppCompatActivity {
    public static final String NONE_OP = "None";
    public static final String[] OPERATION_TYPES = new String[] { PlusN.OP_ID, MinusN.OP_ID,
            TimesN.OP_ID, EqualsN.OP_ID, NONE_OP };
    public static final int ADD_INSTRUCTION = 1;
    public static final int DELETE_INSTRUCTION = 2;

    private int dataPosition;
    @Nullable private Integer originalValue = null;
    private long id = -1L;
    @NotNull private ArrayList<String> log = new ArrayList<>();

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
                originalValue = vb.getValue();
                id = vb.getId();
                log = vb.getLog();
            }
            setData(vb);
            dataPosition = intent.getIntExtra("dataPosition",-1);
        } else {
            ((EditText)findViewById(R.id.bundle_name_field)).setText(savedInstanceState.getCharSequence("caption"));
            ((EditText)findViewById(R.id.bundle_value_field)).setText(savedInstanceState.getCharSequence("value"));
            CharSequence opType = savedInstanceState.getCharSequence("op1Type");
            ((Button)findViewById(R.id.bundle_op1_type)).setText(opType);
            if (opType!=null && NONE_OP.contentEquals(opType)) {
                findViewById(R.id.bundle_op1_amount).setVisibility(View.GONE);
            } else {
                findViewById(R.id.bundle_op1_amount).setVisibility(View.VISIBLE);
            }
            ((EditText)findViewById(R.id.bundle_op1_amount)).setText(savedInstanceState.getCharSequence("op1Amount"));
            opType = savedInstanceState.getCharSequence("op2Type");
            ((Button)findViewById(R.id.bundle_op2_type)).setText(opType);
            if (opType!=null && NONE_OP.contentEquals(opType)) {
                findViewById(R.id.bundle_op2_amount).setVisibility(View.GONE);
            } else {
                findViewById(R.id.bundle_op2_amount).setVisibility(View.VISIBLE);
            }
            ((EditText)findViewById(R.id.bundle_op2_amount)).setText(savedInstanceState.getCharSequence("op2Amount"));
            opType = savedInstanceState.getCharSequence("op3Type");
            ((Button)findViewById(R.id.bundle_op3_type)).setText(opType);
            if (opType!=null && NONE_OP.contentEquals(opType)) {
                findViewById(R.id.bundle_op3_amount).setVisibility(View.GONE);
            } else {
                findViewById(R.id.bundle_op3_amount).setVisibility(View.VISIBLE);
            }
            ((EditText)findViewById(R.id.bundle_op3_amount)).setText(savedInstanceState.getCharSequence("op3Amount"));
            ((EditText)findViewById(R.id.bundle_log_length)).setText(savedInstanceState.getCharSequence("logLength"));
            ArrayList<String> tempLog = savedInstanceState.getStringArrayList("log");
            log = tempLog==null ? new ArrayList<String>() : tempLog;
            dataPosition = savedInstanceState.getInt("dataPosition");
            originalValue = savedInstanceState.getInt("originalValue");
            id = savedInstanceState.getLong("id");
        }
        setListener(R.id.bundle_op1_type, R.id.bundle_op1_amount);
        setListener(R.id.bundle_op2_type, R.id.bundle_op2_amount);
        setListener(R.id.bundle_op3_type, R.id.bundle_op3_amount);
        findViewById(R.id.save_bundle_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveVB();
                } catch (NumberFormatException e) {
                    displayError(e);
                }
            }
        });
        findViewById(R.id.cancel_bundle_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelVB();
            }
        });
        findViewById(R.id.delete_bundle_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVB();
            }
        });
        findViewById(R.id.view_log_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLog();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        includeInBundle(outState);
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

    private void includeInBundle(Bundle target) {
        target.putCharSequence("caption", ((EditText)findViewById(R.id.bundle_name_field)).getText());
        target.putCharSequence("value", ((EditText)findViewById(R.id.bundle_value_field)).getText());
        target.putCharSequence("op1Type", ((Button)findViewById(R.id.bundle_op1_type)).getText());
        target.putCharSequence("op1Amount", ((EditText)findViewById(R.id.bundle_op1_amount)).getText());
        target.putCharSequence("op2Type", ((Button)findViewById(R.id.bundle_op2_type)).getText());
        target.putCharSequence("op2Amount", ((EditText)findViewById(R.id.bundle_op2_amount)).getText());
        target.putCharSequence("op3Type", ((Button)findViewById(R.id.bundle_op3_type)).getText());
        target.putCharSequence("op3Amount", ((EditText)findViewById(R.id.bundle_op3_amount)).getText());
        target.putCharSequence("logLength", ((EditText)findViewById(R.id.bundle_log_length)).getText());
        target.putStringArrayList("log", log);
        target.putInt("dataPosition", dataPosition);
        target.putSerializable("originalValue", originalValue);
        target.putLong("id", id);
    }

    /**
     * Moves to the <code>Activity</code> which displays the log file for this bundle.
     */
    private void displayLog() {
        Intent intent = new Intent(this, LogViewActivity.class);
        intent.putStringArrayListExtra("log", log);
        Bundle bd = new Bundle();
        includeInBundle(bd);
        intent.putExtra("savedState",bd);
        startActivity(intent);
    }

    /**
     * Shows the user an error - specifically that they have formatted a number incorrectly.
     * @param e The caught <code>NumberFormatException</code>.
     */
    private void displayError(NumberFormatException e) {
        Toast.makeText(this, "You must enter an integer: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Turns the data in the various fields of this activity into a <code>VariableBundle</code>.
     * @return A <code>VariableBundle</code> consisting of the information in this <code>Activity</code>.
     */
    private VariableBundle makeVB() {
        int newValue = Integer.parseInt(((EditText)findViewById(R.id.bundle_value_field)).getText().toString());
        VariableBundle temp = (new VariableBundle())
                .setCaption(((EditText)findViewById(R.id.bundle_name_field)).getText().toString())
                .setValue(originalValue==null ? 0 : originalValue)
                .setOp(1, ((Button)findViewById(R.id.bundle_op1_type)).getText().toString(),
                        ((EditText)findViewById(R.id.bundle_op1_amount)).getText().toString())
                .setOp(2, ((Button)findViewById(R.id.bundle_op2_type)).getText().toString(),
                        ((EditText)findViewById(R.id.bundle_op2_amount)).getText().toString())
                .setOp(3, ((Button)findViewById(R.id.bundle_op3_type)).getText().toString(),
                        ((EditText)findViewById(R.id.bundle_op3_amount)).getText().toString())
                .setId(id)
                .setLogLength(Integer.parseInt(((EditText)findViewById(R.id.bundle_log_length))
                        .getText().toString())).setLog(log);
        if ((originalValue==null && newValue!=0) || (originalValue!=null && newValue!=originalValue)) {
            temp.applyOperation((new EqualsN()).setValue(newValue));
        }
        return temp;
    }

    /**
     * Returns the data from this <code>Activity</code> as its result.
     */
    private void saveVB() {
        Intent result = new Intent();
        result.putExtra("vbData", makeVB());
        if (dataPosition==-1) {
            result.putExtra("instruction", ADD_INSTRUCTION);
        } else {
            result.putExtra("dataPosition", dataPosition);
        }
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
     * Deletes the bundle being edited.
     */
    private void deleteVB() {
        if (dataPosition==-1) {
            cancelVB();
        } else {
            Intent result = new Intent();
            result.putExtra("dataPosition", dataPosition);
            result.putExtra("instruction", DELETE_INSTRUCTION);
            setResult(RESULT_OK, result);
            finish();
        }
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
            ((EditText)findViewById(R.id.bundle_log_length)).setText(String.valueOf(VariableBundle.DEFAULT_LOG_LENGTH));
        } else {
            ((EditText)findViewById(R.id.bundle_name_field)).setText(vb.getCaption());
            ((EditText)findViewById(R.id.bundle_value_field)).setText(String.valueOf(vb.getValue()));
            Operation<Integer> op = vb.getOperation(1);
            if (op==null) {
                ((Button)findViewById(R.id.bundle_op1_type)).setText(NONE_OP);
                findViewById(R.id.bundle_op1_amount).setVisibility(View.GONE);
            } else {
                ((Button)findViewById(R.id.bundle_op1_type)).setText(op.opType());
                EditText op1Amount = findViewById(R.id.bundle_op1_amount);
                op1Amount.setVisibility(View.VISIBLE);
                op1Amount.setText(op.toString().substring(1));
            }
            op = vb.getOperation(2);
            if (op==null) {
                ((Button)findViewById(R.id.bundle_op2_type)).setText(NONE_OP);
                findViewById(R.id.bundle_op2_amount).setVisibility(View.GONE);
            } else {
                ((Button)findViewById(R.id.bundle_op2_type)).setText(op.opType());
                EditText op2Amount = findViewById(R.id.bundle_op2_amount);
                op2Amount.setVisibility(View.VISIBLE);
                op2Amount.setText(op.toString().substring(1));
            }
            op = vb.getOperation(3);
            if (op==null) {
                ((Button)findViewById(R.id.bundle_op3_type)).setText(NONE_OP);
                findViewById(R.id.bundle_op3_amount).setVisibility(View.GONE);
            } else {
                ((Button)findViewById(R.id.bundle_op3_type)).setText(op.opType());
                EditText op3Amount = findViewById(R.id.bundle_op3_amount);
                op3Amount.setVisibility(View.VISIBLE);
                op3Amount.setText(op.toString().substring(1));
            }
            ((EditText)findViewById(R.id.bundle_log_length)).setText(String.valueOf(vb.getLogLength()));
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
