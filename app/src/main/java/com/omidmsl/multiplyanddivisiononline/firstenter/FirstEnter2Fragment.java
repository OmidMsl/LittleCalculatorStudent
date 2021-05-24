package com.omidmsl.multiplyanddivisiononline.firstenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.models.Student;

public class FirstEnter2Fragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_first_enter_2, container, false);

        final EditText et = root.findViewById(R.id.ffe2_editText);
        root.findViewById(R.id.ffe2_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et.getText().toString().trim();
                if (name.isEmpty())
                    Snackbar.make(root, "نام پدر خود را وارد کنید",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else {
                    Bundle args = new Bundle();
                    args.putString(Student.KEY_NAME, getArguments().getString(Student.KEY_NAME));
                    args.putString(Student.KEY_FATHER_NAME, name);
                    Navigation.findNavController(v).navigate(R.id.action_nav_2_to_nav_3, args);
                }
            }
        });

        return root;
    }
}
