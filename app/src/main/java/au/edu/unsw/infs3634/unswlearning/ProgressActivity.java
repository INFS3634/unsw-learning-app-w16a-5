package au.edu.unsw.infs3634.unswlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Adapters.ProgressModuleCheckAdapter;
import au.edu.unsw.infs3634.unswlearning.Model.ModuleListModel;
import au.edu.unsw.infs3634.unswlearning.Model.ModuleListViewModel;

public class ProgressActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        //initialise bottom nav bar
        bottomNavigationView = findViewById(R.id.bottom_nav);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);
        //ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.quiz:
                        startActivity(new Intent(getApplicationContext(), QuizActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.learn:
                        startActivity(new Intent(getApplicationContext(), LearnActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });




    }


}