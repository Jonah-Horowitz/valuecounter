package net.hashjellyfish.valuecounter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hashjellyfish.valuecounter.vb.VariableBundle;

import java.util.ArrayList;

public class VariableAdapter extends RecyclerView.Adapter<VariableAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutHolder;
        private VariableBundle currentData = null;

        public ViewHolder(LinearLayout v) {
            super(v);
            layoutHolder = v;
            ((View)layoutHolder.findViewById(R.id.left_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    if (currentData.op1!=null) {
                        currentData.mainValue = currentData.op1.apply(currentData.mainValue);
                        ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(currentData.mainValue));
                    }
                }
            });
            ((View)layoutHolder.findViewById(R.id.left_middle_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    if (currentData.op2!=null) {
                        currentData.mainValue = currentData.op2.apply(currentData.mainValue);
                        ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(currentData.mainValue));
                    }
                }
            });
            ((View)layoutHolder.findViewById(R.id.right_middle_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    if (currentData.op3!=null) {
                        currentData.mainValue = currentData.op3.apply(currentData.mainValue);
                        ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(currentData.mainValue));
                    }
                }
            });
            ((View)layoutHolder.findViewById(R.id.right_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    Snackbar.make(vv,"Settings button pressed on number "+getAdapterPosition(),Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show(); // TODO
                }
            });
        }

        public void setBundle(VariableBundle vb) {
            ((TextView)layoutHolder.findViewById(R.id.caption)).setText(vb.caption);
            ((TextView)layoutHolder.findViewById(R.id.value_display)).setText(String.valueOf(vb.mainValue));
            if (vb.op1==null)
                ((Button)layoutHolder.findViewById(R.id.left_button)).setVisibility(View.GONE);
            else
                ((Button)layoutHolder.findViewById(R.id.left_button)).setText(vb.op1.toString());
            if (vb.op2==null)
                ((Button)layoutHolder.findViewById(R.id.left_middle_button)).setVisibility(View.GONE);
            else
                ((Button)layoutHolder.findViewById(R.id.left_middle_button)).setText(vb.op2.toString());
            if (vb.op3==null)
                ((Button)layoutHolder.findViewById(R.id.right_middle_button)).setVisibility(View.GONE);
            else
                ((Button)layoutHolder.findViewById(R.id.right_middle_button)).setText(vb.op3.toString());
            currentData=vb;
        }
    }

    private ArrayList<VariableBundle> dataList;

    public VariableAdapter(ArrayList<VariableBundle> data) {
        dataList = data;
    }

    @Override
    public VariableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.variable_bundle_view, parent, false);
        // TODO
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setBundle(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
