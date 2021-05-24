package com.omidmsl.multiplyanddivisiononline.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.TestActivity;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Test;

public class TestOptionsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public static final String KEY_NUM_OF_QUESTIONS = "numOfQuestions";
    public static final String KEY_NUM_OF_FIRST_DIGIT = "numOfFirstDigit";


    EditText numOfQuestions;
    ToggleButton m11, m21, m31, m41, d21;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_test_options, container, false);

        numOfQuestions = root.findViewById(R.id.fto_editText_qNum);
        m11 = root.findViewById(R.id.fto_mul_button_1_1);
        m21 = root.findViewById(R.id.fto_mul_button_2_1);
        m31 = root.findViewById(R.id.fto_mul_button_3_1);
        m41 = root.findViewById(R.id.fto_mul_button_4_1);
        d21 = root.findViewById(R.id.fto_div_button_2_1);

        m11.setOnCheckedChangeListener(this);
        m21.setOnCheckedChangeListener(this);
        m31.setOnCheckedChangeListener(this);
        m41.setOnCheckedChangeListener(this);
        d21.setOnCheckedChangeListener(this);


        root.findViewById(R.id.fto_linearLayout_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noqStr = numOfQuestions.getText().toString();
                if (noqStr.isEmpty() || noqStr.equals("0"))
                    Snackbar.make(v, "تعداد سوالات را وارد کنید.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else if (!m11.isChecked() && !m21.isChecked() && !m31.isChecked() &&
                        !m41.isChecked() && !d21.isChecked())
                    Snackbar.make(v, "نوع آزمون را انتخاب کنید.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else {
                    Intent intent = new Intent(getContext(), TestActivity.class);
                    intent.putExtra(KEY_NUM_OF_QUESTIONS, Integer.parseInt(noqStr));
                    intent.putExtra(KEY_NUM_OF_FIRST_DIGIT, m11.isChecked()? 1 :
                            m31.isChecked()? 3 : m41.isChecked()? 4 : 2);
                    intent.putExtra(Test.KEY_IS_MUL, !d21.isChecked());
                    startActivity(intent);
                    Navigation.findNavController(root)
                            .navigate(R.id.action_TestOptionsFragment_to_TestsFragment);
                }
            }
        });

        return root;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            switch (buttonView.getId()) {
                case R.id.fto_mul_button_1_1:
                    m21.setChecked(false);
                    m31.setChecked(false);
                    m41.setChecked(false);
                    d21.setChecked(false);
                    break;

                case R.id.fto_mul_button_2_1:
                    m11.setChecked(false);
                    m31.setChecked(false);
                    m41.setChecked(false);
                    d21.setChecked(false);
                    break;

                case R.id.fto_mul_button_3_1:
                    m11.setChecked(false);
                    m21.setChecked(false);
                    m41.setChecked(false);
                    d21.setChecked(false);
                    break;

                case R.id.fto_mul_button_4_1:
                    m11.setChecked(false);
                    m21.setChecked(false);
                    m31.setChecked(false);
                    d21.setChecked(false);
                    break;

                case R.id.fto_div_button_2_1:
                    m11.setChecked(false);
                    m21.setChecked(false);
                    m31.setChecked(false);
                    m41.setChecked(false);
                    break;

            }
    }
}
