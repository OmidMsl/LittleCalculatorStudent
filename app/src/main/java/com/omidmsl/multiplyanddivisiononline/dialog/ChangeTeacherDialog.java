package com.omidmsl.multiplyanddivisiononline.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;

import java.util.ArrayList;
import java.util.Objects;

public class ChangeTeacherDialog {

    AutoCompleteTextView city, school, teacherName;
    ArrayList<String> cities, schools;
    ArrayList<Teacher> allTeachers, teachers;
    ArrayAdapter<String> cityAdapter, schoolAdapter;
    ArrayAdapter<Teacher> teacherAdapter;
    Progress progress;
    Button confirmBtn;
    int teacherId;
    String teacherName1;

    public ChangeTeacherDialog(final Context context, final TextView tvTeacher) {
        final Dialog mDialog = new Dialog(context);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_change_teacher);

        city = mDialog.findViewById(R.id.dct_autoCompleteTextView_city);
        school = mDialog.findViewById(R.id.dct_autoCompleteTextView_school);
        teacherName = mDialog.findViewById(R.id.dct_autoCompleteTextView_teacher);
        confirmBtn = mDialog.findViewById(R.id.dct_dialog_button_confirm);
        progress = Progress.getInstance();
        cities = new ArrayList<>();
        schools = new ArrayList<>();
        allTeachers = new ArrayList<>();
        teachers = new ArrayList<>();
        cityAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, cities);
        schoolAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, schools);
        teacherAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, teachers);
        city.setAdapter(cityAdapter);
        school.setAdapter(schoolAdapter);
        teacherName.setAdapter(teacherAdapter);

        progress.showProgress(context, "Loading ...", true);
        teacherTask task = new teacherTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllTeachers.php");

        school.setEnabled(false);
        teacherName.setEnabled(false);
        confirmBtn.setEnabled(false);

        city.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                String c = city.getText().toString().trim();
                if (cities.contains(c)) {
                    schools = new ArrayList<>();
                    for (Teacher teacher : allTeachers) {
                        if (teacher.getCity().equals(c) && !schools.contains(teacher.getSchool())) {
                            schools.add(teacher.getSchool());
                        }
                    }
                    schoolAdapter.notifyDataSetChanged();
                    schoolAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, schools);
                    school.setAdapter(schoolAdapter);
                    school.setEnabled(true);
                } else {
                    Toast.makeText(context, "شهر مورد نظر یافت نشد", Toast.LENGTH_SHORT).show();
                    school.setEnabled(false);
                }
                if (!school.getText().toString().isEmpty())
                    school.setText("");
                if (!teacherName.getText().toString().isEmpty())
                    teacherName.setText("");

                teacherName.setEnabled(false);
                confirmBtn.setEnabled(false);
            }
        });

        school.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                String c = school.getText().toString().trim();
                if (schools.contains(c)){
                    teachers = new ArrayList<>();
                    for (Teacher teacher : allTeachers){
                        if (teacher.getSchool().equals(c) && !teachers.contains(teacher)){
                            teachers.add(teacher);
                        }
                    }
                    teacherAdapter.notifyDataSetChanged();
                    teacherAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, teachers);
                    teacherName.setAdapter(teacherAdapter);
                    teacherName.setEnabled(true);
                } else {
                    Toast.makeText(context, "مدرسه مورد نظر یافت نشد", Toast.LENGTH_SHORT).show();
                    teacherName.setEnabled(false);
                }
                if (!teacherName.getText().toString().isEmpty())
                    teacherName.setText("");
                confirmBtn.setEnabled(false);
            }
        });

        teacherName.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                String c = teacherName.getText().toString().trim();
                boolean flag = false;
                for (Teacher teacher : teachers){
                    if (teacher.getName().equals(c)){
                        flag = true;
                        teacherId = teacher.getId();
                        teacherName1 = teacher.getName();
                        break;
                    }
                }
                if (flag){
                    confirmBtn.setEnabled(true);

                } else {
                    confirmBtn.setEnabled(false);
                    Toast.makeText(context, "معلم مورد نظر یافت نشد", Toast.LENGTH_SHORT).show();
                }
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTeacher.setText(teacherName1);
                tvTeacher.setTag(String.valueOf(teacherId));
                mDialog.dismiss();
            }
        });

        mDialog.findViewById(R.id.dct_dialog_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        // you can change or add this line according to your need
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_shape);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    private void updateDisplay(ArrayList<Teacher> teachers){
        this.allTeachers = teachers;
        for (Teacher teacher : allTeachers){
            if (!cities.contains(teacher.getCity())){
                cities.add(teacher.getCity());
            }
        }
        cityAdapter.notifyDataSetChanged();

    }

    private class teacherTask extends AsyncTask<String, String, ArrayList<Teacher>> {
        @Override
        protected ArrayList<Teacher> doInBackground(String... params) {
            return (ArrayList<Teacher>) JsonParser.parseTeacher(Connection.getDataFromServer(params[0]));
        }

        @Override
        protected void onPostExecute(ArrayList<Teacher> result) {
            updateDisplay(result);
            progress.hideProgress();
        }
    }
}
