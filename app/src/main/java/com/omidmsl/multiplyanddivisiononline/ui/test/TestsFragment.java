package com.omidmsl.multiplyanddivisiononline.ui.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.JsonParser;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.dialog.OfflineAlert;
import com.omidmsl.multiplyanddivisiononline.dialog.Progress;
import com.omidmsl.multiplyanddivisiononline.models.Student;
import com.omidmsl.multiplyanddivisiononline.models.Test;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TestsFragment extends Fragment {

    TestAdapter testAdapter;
    int studentId;
    Context context;
    Progress progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tests, container, false);

        RecyclerView rv = root.findViewById(R.id.rv_tests);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        context = getActivity();
        progress = Progress.getInstance();

        SharedPreferences sp = context.getSharedPreferences("studentInfo", MODE_PRIVATE);
        studentId = sp.getInt(Student.KEY_ID, -2);

        int[] colors = {ContextCompat.getColor(context, android.R.color.holo_green_light),
                ContextCompat.getColor(context, android.R.color.holo_blue_bright),
                ContextCompat.getColor(context, android.R.color.holo_blue_dark),
                ContextCompat.getColor(context, android.R.color.holo_orange_dark),
                ContextCompat.getColor(context, android.R.color.holo_red_light)};

        testAdapter = new TestAdapter(context, new ArrayList<Test>() , colors);
        rv.setAdapter(testAdapter);

        root.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TestsFragment.this)
                        .navigate(R.id.action_TestsFragment_to_TestOptionsFragment);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        if (!Connection.isOnline(context)) {
            new OfflineAlert(context);
        } else {
            progress.showProgress(context, "Loading ...", true);
            Connection.RequestPackage rp = new Connection.RequestPackage();
            rp.setFileName("getStudentTests.php");
            rp.setMethod("POST");
            rp.setParameter("student_id", String.valueOf(studentId));
            new getTestsTask().execute(rp);
        }
        super.onResume();
    }

    private void loadTests(ArrayList<Test> tests) {
        testAdapter.updateData(tests);
    }

    private class getTestsTask extends AsyncTask<Connection.RequestPackage , Void , String> {

        @Override
        protected String doInBackground(Connection.RequestPackage... requestPackages) {
            return Connection.getDataFromServer(requestPackages[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.contains("Error")) {
                loadTests((ArrayList<Test>) JsonParser.parseTest(s));
            }else
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            progress.hideProgress();
        }
    }

}
