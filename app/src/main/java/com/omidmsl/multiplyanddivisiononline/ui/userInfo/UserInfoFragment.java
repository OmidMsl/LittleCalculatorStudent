package com.omidmsl.multiplyanddivisiononline.ui.userInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.dialog.ChangeTeacherDialog;
import com.omidmsl.multiplyanddivisiononline.dialog.DeleteAccountDialog;
import com.omidmsl.multiplyanddivisiononline.dialog.OfflineAlert;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.firstenter.FirstEnterActivity;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Teacher;

import static android.content.Context.MODE_PRIVATE;

public class UserInfoFragment extends Fragment {

    EditText name, fatherName;
    TextView teacherName;
    Button changeTeacher, deleteAccount;
    Progress progress;
    int id, operation;
    boolean needInternet;
    Context context;
    DeleteAccountDialog dad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_info, container, false);

        name = root.findViewById(R.id.fui_editText_name);
        fatherName = root.findViewById(R.id.fui_editText_fname);
        teacherName = root.findViewById(R.id.fui_textView_tname);
        changeTeacher = root.findViewById(R.id.fui_button_change_teacher);
        deleteAccount = root.findViewById(R.id.fui_button_delete_account);

        context = getActivity();
        progress = Progress.getInstance();
        SharedPreferences sp = context.getSharedPreferences("studentInfo", MODE_PRIVATE);
        id = sp.getInt(Student.KEY_ID, -2);
        setHasOptionsMenu(true);

        if (!Connection.isOnline(context)) {
            new OfflineAlert(context);
            needInternet = true;
            operation = 1;
        } else {
            progress.showProgress(context, "Loading ...", false);
            needInternet = false;
            Connection.RequestPackage rp = new Connection.RequestPackage();
            rp.setFileName("getStudentWithTeachersName.php");
            rp.setMethod("POST");
            rp.setParameter("id", String.valueOf(id));
            new getStudentTask().execute(rp);
        }

        changeTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Connection.isOnline(context)) {
                    new OfflineAlert(context);
                    needInternet = true;
                    operation = 2;
                } else {
                    new ChangeTeacherDialog(context, teacherName);
                    needInternet = false;
                }
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dad = new DeleteAccountDialog(context, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Connection.isOnline(context)) {
                            new OfflineAlert(context);
                            needInternet = true;
                            operation = 3;
                        } else {
                            progress.showProgress(context, "Loading ...", false);
                            needInternet = false;
                            Connection.RequestPackage rp = new Connection.RequestPackage();
                            rp.setFileName("deleteStudent.php");
                            rp.setMethod("POST");
                            rp.setParameter("id", String.valueOf(id));
                            new deleteStudentTask().execute(rp);
                        }
                    }
                });

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
                if (operation == 1) {
                    progress.showProgress(context, "Loading ...", false);
                    needInternet = false;
                    Connection.RequestPackage rp = new Connection.RequestPackage();
                    rp.setFileName("getStudentWithTeachersName.php");
                    rp.setMethod("POST");
                    rp.setParameter("id", String.valueOf(id));
                    new getStudentTask().execute(rp);
                }
                needInternet = false;
            }
        }
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.add("ذخیره").setIcon(R.drawable.ic_save_white_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String nstr=name.getText().toString().trim(),
                        fnstr=fatherName.getText().toString().trim();
                if (nstr.isEmpty())
                    Snackbar.make(getView(), "باید نام خود را وارد کنید.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else if (fnstr.isEmpty())
                    Snackbar.make(getView(), "باید نام پدر را وارد کنید.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else if (!Connection.isOnline(context))
                    new OfflineAlert(context);
                else {
                    progress.showProgress(context, "Loading ...", false);
                    needInternet = false;
                    Connection.RequestPackage rp = new Connection.RequestPackage();
                    rp.setFileName("editStudentInfo.php");
                    rp.setMethod("POST");
                    rp.setParameter("id", String.valueOf(id));
                    rp.setParameter("name", nstr);
                    rp.setParameter("father_name", fnstr);
                    rp.setParameter("teacher_id", (String) teacherName.getTag());
                    new EditStudentTask().execute(rp);
                }
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void updateInfo(StudentWithTeacher swtn){
        name.setText(swtn.student.getName());
        fatherName.setText(swtn.student.getFatherName());
        teacherName.setText(swtn.getTeacherName());
        teacherName.setTag(String.valueOf(swtn.getTeacherId()));
    }

    public static class StudentWithTeacher {
        public Student student;
        private String teacherName;
        private int teacherId;

        public StudentWithTeacher() {
            student = new Student();
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public int getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(int teacherId) {
            this.teacherId = teacherId;
        }
    }

    private class getStudentTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                updateInfo(JsonParser.parseUWTN(s));
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }

    private class EditStudentTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                Toast.makeText(context, "اطلاعات با موفقیت ذخیره شدند.", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = context.getSharedPreferences("studentInfo", MODE_PRIVATE);
                sp.edit().putInt(Teacher.KEY_ID, Integer.parseInt((String)teacherName.getTag())).apply();
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }

    private class deleteStudentTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                SharedPreferences sp = context.getSharedPreferences("studentInfo", MODE_PRIVATE);
                sp.edit().clear().apply();
                dad.dismiss();
                context.startActivity(new Intent(context, FirstEnterActivity.class));
                getActivity().finish();
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }
}
