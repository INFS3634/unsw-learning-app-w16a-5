package au.edu.unsw.infs3634.unswlearning;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Adapters.IngredientsAdapter;
import au.edu.unsw.infs3634.unswlearning.Adapters.InstructionsAdapter;
import au.edu.unsw.infs3634.unswlearning.Adapters.SimilarRecipeAdapter;
import au.edu.unsw.infs3634.unswlearning.Api.InstructionsReponse;
import au.edu.unsw.infs3634.unswlearning.Api.RecipeInfoResponse;
import au.edu.unsw.infs3634.unswlearning.Api.SimilarRecipeApiResponse;
import au.edu.unsw.infs3634.unswlearning.Listeners.InstructionsListener;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeClickListener;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeInfoListener;
import au.edu.unsw.infs3634.unswlearning.Listeners.SimilarRecipeListener;

public class RecipeInfoActivity extends AppCompatActivity {
    int id;
    ProgressDialog dialog;
    ApiRequestManager manager;
    TextView textView_dish_name, textView_dish_source;
    ImageView imageView_dish_image;
    RecyclerView recycler_dish_instructions, recycler_dish_ingredients, recycler_dish_similar;
    //adapter object
    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        findViews();
        //capturing the passed id from intent in MainActivity based on the name
        //parses as int as id is an integer
        id = Integer.parseInt(getIntent().getStringExtra("id"));

        //initialise RequestManager
        manager = new ApiRequestManager(this);
        //passing instance of RecipeDetailsListener and id as established in RequestManager
        manager.getRecipeInfo(recipeInfoListener, id);
        //passing instance of SimilarRecipeListener and id as established in RequestManager
        manager.getSimilarRecipes(similarRecipeListener, id);
        manager.getInstructions(instructionsListener, id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details");
        dialog.show();

    }

    private void findViews() {
        textView_dish_name = findViewById(R.id.textView_dish_name);
        textView_dish_source = findViewById(R.id.textView_dish_source);
        imageView_dish_image = findViewById(R.id.imageView_dish_image);
        recycler_dish_instructions = findViewById(R.id.recycler_dish_instructions);
        recycler_dish_ingredients = findViewById(R.id.recycler_dish_ingredients);
        recycler_dish_similar = findViewById(R.id.recycler_dish_similar);
    }

    //inherit from the interface RecipeInfoListener
    private final RecipeInfoListener recipeInfoListener = new RecipeInfoListener() {
        @Override
        public void fetched(RecipeInfoResponse response, String message) {
            //dismiss dialog loading box when data is fetched
            dialog.dismiss();
            textView_dish_name.setText(response.title);
            textView_dish_source.setText(response.sourceName);
            Picasso.get().load(response.image).into(imageView_dish_image);
            //initialise RecyclerView with id sourced from XML
            recycler_dish_ingredients.setHasFixedSize(true);
            //set layout manager for the above recyclerview
            recycler_dish_ingredients.setLayoutManager(new LinearLayoutManager(RecipeInfoActivity.this,LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeInfoActivity.this, response.extendedIngredients);
            //attach the adapter to the recyclerView
            recycler_dish_ingredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void error(String message) {
            System.out.println(message);
        }
    };

    private final SimilarRecipeListener similarRecipeListener = new SimilarRecipeListener() {
        @Override
        public void fetched(List<SimilarRecipeApiResponse> response, String message) {
            recycler_dish_similar.setHasFixedSize(true);
            recycler_dish_similar.setLayoutManager(new LinearLayoutManager(RecipeInfoActivity.this, LinearLayoutManager.HORIZONTAL, false));
            //object for the adapter
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeInfoActivity.this, response, recipeClickListener);
            //attach similarRecipeAdapter to recycler_meal_similar recyclerView
            recycler_dish_similar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void error(String message) {
            System.out.println(message);
        }
    };



    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            //if user clicks on any similar recipes, it would relaunch the RecipeDetailsActivity with the new recipe ID
            startActivity(new Intent(RecipeInfoActivity.this, RecipeInfoActivity.class).putExtra("id", id));
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void fetched(List<InstructionsReponse> response, String message) {
            recycler_dish_instructions.setHasFixedSize(true);
            recycler_dish_instructions.setLayoutManager(new LinearLayoutManager(RecipeInfoActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(RecipeInfoActivity.this, response);
            recycler_dish_instructions.setAdapter(instructionsAdapter);

        }

        @Override
        public void error(String message) {
            System.out.println(message);
        }
    };

}
