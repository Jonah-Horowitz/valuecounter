package net.hashjellyfish.valuecounter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hashjellyfish.valuecounter.vb.VariableBundle;
import net.hashjellyfish.valuecounter.vb.ops.Operation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VariableAdapter extends RecyclerView.Adapter<VariableAdapter.ViewHolder> {
    public static final int BUNDLE_SETTINGS_RESULT_CODE = 39;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutHolder;
        private VariableBundle currentData = null;
        private int dataPosition = -1;
        private Activity parentActivity = null;

        /**
         * Creates a <code>RecyclerView.ViewHolder</code> specific to this type of <code>RecyclerView</code>.
         * @param v The <code>View</code> which holds the <code>Layout</code> in which this goes.
         */
        ViewHolder(LinearLayout v) {
            super(v);
            layoutHolder = v;
        }

        /**
         * Instantiates this <code>RecyclerView.ViewHolder</code> with its associated data.
         * @param context Whichever <code>Activity</code> is using this adapter.
         * @param position The position of this holder's data in the adapter's <code>dataList</code>.
         * @param vb The <code>net.hashjellyfish.valuecouter.vb.VariableBundle</code> consisting of the data for this holder.
         */
        void setBundle(Activity context, int position, VariableBundle vb) {
            parentActivity = context;
            dataPosition = position;
            ((TextView)layoutHolder.findViewById(R.id.caption)).setText(vb.getCaption());
            ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(vb.getValue()));
            Operation<Integer> op = vb.getOperation(1);
            if (op==null) {
                layoutHolder.findViewById(R.id.left_button).setVisibility(View.GONE);
            } else {
                layoutHolder.findViewById(R.id.left_button).setVisibility(View.VISIBLE);
                ((Button)layoutHolder.findViewById(R.id.left_button)).setText(op.toString());
            }
            op = vb.getOperation(2);
            if (op==null) {
                layoutHolder.findViewById(R.id.left_middle_button).setVisibility(View.GONE);
            } else {
                layoutHolder.findViewById(R.id.left_middle_button).setVisibility(View.VISIBLE);
                ((Button)layoutHolder.findViewById(R.id.left_middle_button)).setText(op.toString());
            }
            op = vb.getOperation(3);
            if (op==null) {
                layoutHolder.findViewById(R.id.right_middle_button).setVisibility(View.GONE);
            } else {
                layoutHolder.findViewById(R.id.right_middle_button).setVisibility(View.VISIBLE);
                ((Button)layoutHolder.findViewById(R.id.right_middle_button)).setText(op.toString());
            }
            currentData=vb;
            layoutHolder.findViewById(R.id.left_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    currentData.applyOperation(1);
                    ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(currentData.getValue()));
                }
            });
            layoutHolder.findViewById(R.id.left_middle_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    currentData.applyOperation(2);
                    ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(currentData.getValue()));
                }
            });
            layoutHolder.findViewById(R.id.right_middle_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    currentData.applyOperation(3);
                    ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(currentData.getValue()));
                }
            });
            layoutHolder.findViewById(R.id.right_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    Intent intent = new Intent(parentActivity, BundleSettingsActivity.class);
                    intent.putExtra("vbData",currentData);
                    intent.putExtra("dataPosition",dataPosition);
                    parentActivity.startActivityForResult(intent, BUNDLE_SETTINGS_RESULT_CODE);
                }
            });
        }
    }

    protected ArrayList<VariableBundle> dataList;
    private Activity parentActivity;

    /**
     * Creates an appropriate instance of <code>VariableAdapter</code>.
     * @param context Whichever <code>Activity</code> is using this <code>RecyclerView.Adapter</code>.
     * @param data The data to be displayed by this adapter.
     */
    VariableAdapter(Activity context, ArrayList<VariableBundle> data) {
        dataList = data;
        parentActivity = context;
    }

    @Override
    @NotNull
    public VariableAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.variable_bundle_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        holder.setBundle(parentActivity, position, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
