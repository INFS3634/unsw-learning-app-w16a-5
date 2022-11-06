package au.edu.unsw.infs3634.unswlearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SearchView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gives a pop-up showing a loading animation when retrieving recipes
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");

        manager = new ApiRequestManager(this);
        manager.getRecipes(recipeResponseListener, tags);
        dialog.show();

        searchView = findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tags.clear();
                tags.add(s);
                manager.getRecipes(recipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private final RecipeResponseListener recipeResponseListener = new RecipeResponseListener() {
        @Override
        public void fetched(RecipeApiResponse response, String message) {
            //gets rid of the loading animation
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_recipes);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
            //passes values into the constructor of RecipeAdapter
            recipeAdapter = new RecipeAdapter(MainActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(recipeAdapter);

        }

        @Override
        public void error(String message) {
            System.out.println(message);
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