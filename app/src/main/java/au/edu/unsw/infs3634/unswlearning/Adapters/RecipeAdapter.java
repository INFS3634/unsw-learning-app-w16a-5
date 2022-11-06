package au.edu.unsw.infs3634.unswlearning.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Api.Recipe;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeClickListener;
import au.edu.unsw.infs3634.unswlearning.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    Context context;
    List<Recipe> list;
    RecipeClickListener recipeClickListener;

    //constructor class
    public RecipeAdapter(Context context, List<Recipe> list, RecipeClickListener recipeClickListener) {
        this.context = context;
        this.list = list;
        this.recipeClickListener = recipeClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //populate the ViewHolder with the xml file list_recipes.xml
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_recipes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        //binding the API responses to the individual xml components
        holder.textView_title.setText(list.get(position).title);
        holder.textView_title.setSelected(true);
        holder.textView_servings.setText(list.get(position).servings + " servings");
        holder.textView_likes.setText(list.get(position).aggregateLikes + "likes");
        holder.textView_time.setText(list.get(position).readyInMinutes + "mins");
        Picasso.get().load(list.get(position).image).into(holder.imageView_dish);

        //if any recipes are clicked
        holder.recipe_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calls the listener passed at the constructor
                //calls the method onRecipeClicked method from the interface RecipeClickListener
                //gets the adapter position of the item clicked
                recipeClickListener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class RecipeViewHolder extends RecyclerView.ViewHolder {
    CardView recipe_list_container;
    TextView textView_title, textView_servings, textView_likes, textView_time;
    ImageView imageView_dish;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        //linking the ids found on the list_recipes.xml file
        recipe_list_container = itemView.findViewById(R.id.recipe_list_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_servings = itemView.findViewById(R.id.textView_servings);
        textView_likes = itemView.findViewById(R.id.textView_likes);
        textView_time = itemView.findViewById(R.id.textView_time);
        imageView_dish = itemView.findViewById(R.id.imageView_dish);


    }
}
