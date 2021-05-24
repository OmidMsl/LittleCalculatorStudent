package com.omidmsl.multiplyanddivisiononline.firstenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.omidmsl.multiplyanddivisiononline.MainActivity;
import com.omidmsl.multiplyanddivisiononline.R;
import com.omidmsl.multiplyanddivisiononline.models.Student;

public class FirstEnterActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_enter);

        SharedPreferences sp = this.getSharedPreferences("studentInfo", MODE_PRIVATE);
        int sid = sp.getInt(Student.KEY_ID, -2);
        if (sid!=-2){
            Intent intent = new Intent(FirstEnterActivity.this, MainActivity.class);
            intent.putExtra(Student.KEY_ID, sid);
            startActivity(intent);
            finish();
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_1, R.id.nav_2, R.id.nav_3)
                .build();


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
