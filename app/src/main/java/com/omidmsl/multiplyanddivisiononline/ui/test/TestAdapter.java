package com.omidmsl.multiplyanddivisiononline.ui.test;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.models.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    Context context;
    private List<Test> tests;
    private final int[] colors;

    public TestAdapter(Context context, List<Test> tests, int[] colors) {
        this.context = context;
        this.tests = tests;
        this.colors = colors;
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_test, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.display(position);
    }

    public void updateData(List<Test> tests) {
        this.tests = tests;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView type;
        private final TextView res;
        private final TextView date;
        private final GradientDrawable mInitialsBackground;


        MyViewHolder(final View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.textView_type);
            res = itemView.findViewById(R.id.textView_result);
            date = itemView.findViewById(R.id.textView_date);
            mInitialsBackground = (GradientDrawable) type.getBackground();

        }


        void display(int position) {
            Test test= tests.get(position);
            String t;
            if (test.isMul()){
                t = "ضرب " + test.getNumOfDigits() + " رقمی\n در 1 رقمی";
            } else {
                t = "تقسیم " + test.getNumOfDigits() + " رقمی\n بر 1 رقمی";
            }
            type.setText(t);
            res.setText(test.getCorrectNum() + "/" + test.getAllNum());
            Date d = test.getDate().getTime();
            String tstr = test.getDate().getPersianLongDate();
            date.setText(tstr.substring(0, tstr.lastIndexOf(' ')) + "\nساعت " + d.getHours() + ":" + d.getMinutes());
            mInitialsBackground.setColor(colors[test.getNumOfDigits()-1]);
        }
    }
}