package au.edu.unsw.infs3634.unswlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Adapters.RecipeAdapter;
import au.edu.unsw.infs3634.unswlearning.Api.RecipeApiResponse;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeClickListener;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeResponseListener;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;
    ApiRequestManager manager;
    RecipeAdapter recipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    List<String> tags = new ArrayList<>();
    Spinner spinner;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gives a pop-up showing a loading animation when retrieving recipes
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");


        //initialise bottom nav bar
        bottomNavigationView = findViewById(R.id.bottom_nav);
        //set home selected with styles referencing nav_bar_selection.xml
        bottomNavigationView.setSelectedItemId(R.id.home);
        //method called when an item in the navigation menu is selected where "item" is the selected item
        //returns a boolean where true displays the item as the selected item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //switch statement corresponds to each navigation item in the bottom nav bar
                switch (item.getItemId()) {
                    //pass intent and switches activity to ProgressActivity
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    //pass intent and switches activity to QuizActivity
                    case R.id.quiz:
                        startActivity(new Intent(getApplicationContext(), QuizActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    //pass intent and switches activity to LearnActivity
                    case R.id.learn:
                        startActivity(new Intent(getApplicationContext(), LearnActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        //initialise the search bar
        searchView = findViewById(R.id.searchView_home);
        //sets a listener for user actions within the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //remove anything from the tags ArrayList
                tags.clear();
                //adds the user searchView input into tags ArrayList
                tags.add(s);
                //calls getRecipes method from the ApiRequestManager
                manager.getRecipes(recipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //initialise spinner
        spinner = findViewById(R.id.spinner_filter);
        // Create an ArrayAdapter using the string array tags from strings.xml and a spinner layout
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.tags, R.layout.spinner_background);
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text);
        // Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
        //when an item on the spinner is clicked
        spinner.setOnItemSelectedListener(spinnerSelectedListener);
        //parses the current state of the app
        manager = new ApiRequestManager(this);
        //calls the getRecipes method from ApiRequestManager
        manager.getRecipes(recipeResponseListener, tags);

        dialog.show();

    }

    private final RecipeResponseListener recipeResponseListener = new RecipeResponseListener() {
        @Override
        public void fetched(RecipeApiResponse response, String message) {
            //gets rid of the loading animation
            dialog.dismiss();
            //initialises the RecyclerView
            recyclerView = findViewById(R.id.recycler_recipes);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
            //passes values into the constructor of RecipeAdapter
            recipeAdapter = new RecipeAdapter(MainActivity.this, response.recipes, recipeClickListener);
            //sets the view of random recipes
            recyclerView.setAdapter(recipeAdapter);

        }

        @Override
        public void error(String message) {
            System.out.println(message);
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tags.clear();
            //get item from the adapter view and convert it to a string format
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRecipes(recipeResponseListener, tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            //prints to console the id of the menu item
            System.out.println(id);
            //creates a new intent which passes the id of the clicked object to the RecipeInfoActivity class
            startActivity(new Intent(MainActivity.this, RecipeInfoActivity.class).putExtra("id", id));
        }
    };
}