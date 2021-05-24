package com.omidmsl.multiplyanddivisiononline.firstenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.MainActivity;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.dialog.OfflineAlert;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class FirstEnter3Fragment extends Fragment {

    AutoCompleteTextView city, school, teacherName;
    ArrayList<String> cities, schools;
    ArrayList<Teacher> allTeachers, teachers;
    ArrayAdapter<String> cityAdapter, schoolAdapter;
    ArrayAdapter<Teacher> teacherAdapter;
    Context context;
    LinearLayout confirmButton;
    Progress progress;
    boolean needInternet, isGetTeachersOperation;
    int teacherId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_first_enter_3, container, false);

        context = getActivity();
        city = root.findViewById(R.id.ffe3_autoCompleteTextView_city);
        school = root.findViewById(R.id.ffe3_autoCompleteTextView_school);
        teacherName = root.findViewById(R.id.ffe3_autoCompleteTextView_teacher);
        confirmButton = root.findViewById(R.id.ffe3_linearLayout_confirm);

        confirmButton.setVisibility(View.INVISIBLE);
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

        if (!Connection.isOnline(context)) {
            new OfflineAlert(context);
            needInternet = true;
            isGetTeachersOperation = true;
        } else {
            progress.showProgress(context, "Loading ...", true);
            needInternet = false;
            teacherTask task = new teacherTask();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllTeachers.php");
        }

        school.setEnabled(false);
        teacherName.setEnabled(false);

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
                    Snackbar.make(getView(), "شهر مورد نظر یافت نشد", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    school.setEnabled(false);
                }
                if (!school.getText().toString().isEmpty())
                    school.setText("");
                if (!teacherName.getText().toString().isEmpty())
                    teacherName.setText("");

                teacherName.setEnabled(false);
                confirmButton.setVisibility(View.INVISIBLE);
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
                    Snackbar.make(getView(), "مدرسه مورد نظر یافت نشد", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    teacherName.setEnabled(false);
                }
                if (!teacherName.getText().toString().isEmpty())
                    teacherName.setText("");
                confirmButton.setVisibility(View.INVISIBLE);
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
                        break;
                    }
                }
                if (flag){
                    confirmButton.setVisibility(View.VISIBLE);
                } else {
                    confirmButton.setVisibility(View.INVISIBLE);
                    Snackbar.make(getView(), "معلم مورد نظر یافت نشد", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Connection.isOnline(context)) {
                    new OfflineAlert(context);
                    needInternet = true;
                    isGetTeachersOperation = false;
                    confirmButton.setVisibility(View.INVISIBLE);
                } else {
                    progress.showProgress(context, "در حال ثبت نام ...", true);
                    Bundle extras = getArguments();
                    Connection.RequestPackage rp = new Connection.RequestPackage();
                    rp.setFileName("addStudent.php");
                    rp.setMethod("POST");
                    rp.setParameter("name", extras.getString(Student.KEY_NAME));
                    rp.setParameter("father_name", extras.getString(Student.KEY_FATHER_NAME));
                    rp.setParameter("teacher_id" , String.valueOf(teacherId));
                    new addStudentTask().execute(rp);
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        if (needInternet) {
            if (!Connection.isOnline(context))
                new OfflineAlert(context);
            else {
                needInternet = false;
                progress.showProgress(context, "Loading ...", true);
                if (isGetTeachersOperation) {
                    teacherTask task = new teacherTask();
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllTeachers.php");
                } else {
                    confirmButton.setVisibility(View.VISIBLE);
                }
            }
        }
        super.onResume();
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

    private class addStudentTask extends AsyncTask<Connection.RequestPackage , Void , String>{

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                int id = JsonParser.getLastStudentId(s);
                SharedPreferences sp = context.getSharedPreferences("studentInfo", MODE_PRIVATE);
                sp.edit().putInt(Student.KEY_ID, id).putInt(Teacher.KEY_ID, teacherId).apply();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Student.KEY_ID, id);
                context.startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }
}