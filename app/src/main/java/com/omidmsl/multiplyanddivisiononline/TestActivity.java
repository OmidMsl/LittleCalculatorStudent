package com.omidmsl.multiplyanddivisiononline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.omidmsl.multiplyanddivisiononline.dialog.OfflineAlert;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Test;
import com.omidmsl.multiplyanddivisiononline.ui.test.TestOptionsFragment;

import java.util.Date;
import java.util.Random;

public class TestActivity extends AppCompatActivity {

    int n1, n2, i, ansMode;
    TextView num1, num2;
    boolean needInternet;
    Progress progress;
    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final EditText et = findViewById(R.id.editText_ans);
        final TextView res = findViewById(R.id.at_textView_res);
        num1 = findViewById(R.id.at_textView_num1);
        num2 = findViewById(R.id.at_textView_num2);
        final Button confirm = findViewById(R.id.button_confirm);
        final ImageView imageView = findViewById(R.id.at_imageView_res);
        ImageView operation = findViewById(R.id.at_imageView_op);
        LinearLayout modLl = findViewById(R.id.at_linearLayout_mod);
        final LinearLayout resLl = findViewById(R.id.at_linearLayout_resImg);
        final EditText bm = findViewById(R.id.editText_baghimandeh);

        progress = Progress.getInstance();

        Bundle extras = getIntent().getExtras();
        test = new Test();
        test.setMul(extras.getBoolean(Test.KEY_IS_MUL, true));
        test.setAllNum(extras.getInt(TestOptionsFragment.KEY_NUM_OF_QUESTIONS));
        test.setNumOfDigits(extras.getInt(TestOptionsFragment.KEY_NUM_OF_FIRST_DIGIT));

        modLl.setVisibility(test.isMul()? View.GONE : View.VISIBLE);
        operation.setImageResource(test.isMul()? R.drawable.multiply_icon : R.drawable.divide_icon);
        i=1;

        final Random random = new Random();
        createRandomNums(test.getNumOfDigits(), random);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ansMode ==0){
                    boolean flag = true;
                    if (test.isMul()){
                        if (!et.getText().toString().isEmpty() && Integer.parseInt(et.getText().toString()) == n1 * n2){
                            imageView.setImageResource(R.drawable.sb_correct);
                            res.setText("آفرین درسته !");
                            test.addCorrectNum();
                            flag = false;
                        }
                    } else {
                        if (!et.getText().toString().isEmpty() &&
                                Integer.parseInt(et.getText().toString())== n1 / n2 &&
                                !bm.getText().toString().isEmpty() &&
                                Integer.parseInt(bm.getText().toString())== n1%n2){
                            imageView.setImageResource(R.drawable.sb_correct);
                            res.setText("آفرین درسته !");
                            test.addCorrectNum();
                            flag = false;
                        }
                    }
                    if (flag){
                        imageView.setImageResource(R.drawable.sb_wrong);
                        res.setText("جوابت اشتباهه");
                    }
                    resLl.setVisibility(View.VISIBLE);
                    if (i <test.getAllNum()) {
                        confirm.setText("سوال بعدی");
                        ansMode =1;
                    } else {
                        confirm.setText("پایان آزمون");
                        ansMode =2;
                    }
                    i++;
                } else if (ansMode ==1){
                    createRandomNums(test.getNumOfDigits(), random);
                    et.setText("");
                    resLl.setVisibility(View.GONE);
                    confirm.setText("ثبت و مشاهده پاسخ");
                    ansMode =0;
                } else {
                    if (!Connection.isOnline(TestActivity.this)) {
                        new OfflineAlert(TestActivity.this);
                        needInternet = true;
                    } else {
                        needInternet = false;
                        progress.showProgress(TestActivity.this, "در حال ذخیره اطلاعات ...", false);
                        SharedPreferences sp = getSharedPreferences("studentInfo", MODE_PRIVATE);
                        Connection.RequestPackage rp = new Connection.RequestPackage();
                        rp.setFileName("addTest.php");
                        rp.setMethod("POST");
                        rp.setParameter("student_id", String.valueOf(sp.getInt(Student.KEY_ID, -2)));
                        rp.setParameter("num_of_all_questions", String.valueOf(test.getAllNum()));
                        rp.setParameter("num_of_correct_answers", String.valueOf(test.getCorrectNum()));
                        rp.setParameter("is_mul", test.isMul()? "1" : "0");
                        rp.setParameter("num_of_digits", String.valueOf(test.getNumOfDigits()));
                        rp.setParameter("date", String.valueOf(new Date().getTime()));
                        new addTestTask().execute(rp);
                    }
                }
            }
        });
    }

    private void createRandomNums(int fd, Random random) {
        n1 = random.nextInt((int) (Math.pow(10, fd)-Math.pow(10, fd-1)))+(int) Math.pow(10, fd-1);
        n2 = random.nextInt(9)+1;
        while (!test.isMul() && n1/n2>9){
            n1 = random.nextInt((int) (Math.pow(10, fd)-Math.pow(10, fd-1)))+(int) Math.pow(10, fd-1);
            n2 = random.nextInt(9)+1;
        }
        num1.setText(String.valueOf(n1));
        num2.setText(String.valueOf(n2));
    }

    @Override
    protected void onResume() {
        if (needInternet){
            if (!Connection.isOnline(TestActivity.this)) {
                new OfflineAlert(TestActivity.this);
                needInternet = true;
            } else {
                needInternet = false;
                progress.showProgress(TestActivity.this, "در حال ذخیره اطلاعات ...", false);
                SharedPreferences sp = getSharedPreferences("studentInfo", MODE_PRIVATE);
                Connection.RequestPackage rp = new Connection.RequestPackage();
                rp.setFileName("addTest.php");
                rp.setMethod("POST");
                rp.setParameter("student_id", String.valueOf(sp.getInt(Student.KEY_ID, -2)));
                rp.setParameter("num_of_all_questions", String.valueOf(test.getAllNum()));
                rp.setParameter("num_of_correct_answers", String.valueOf(test.getCorrectNum()));
                rp.setParameter("is_mul", test.isMul()? "1" : "0");
                rp.setParameter("num_of_digits", String.valueOf(test.getNumOfDigits()));
                rp.setParameter("date", String.valueOf(new Date().getTime()));
                new addTestTask().execute(rp);
            }
        }
        super.onResume();
    }

    private class addTestTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                startActivity(new Intent(TestActivity.this, MainActivity.class));
                finish();
            }else
                Toast.makeText(TestActivity.this, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }
}
