package au.edu.unsw.infs3634.unswlearning.Adapters;

import android.content.Context;
import android.media.Image;
import android.telephony.ims.ImsMmTelManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Api.ExtendedIngredient;
import au.edu.unsw.infs3634.unswlearning.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewholder> {

    //context or current state of the app
    Context context;
    List<ExtendedIngredient> list;

    //constructor that is parsed the values of application context and list that holds the JSON objects from the ExtendedIngredients class
    public IngredientsAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public IngredientsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientsViewholder(LayoutInflater.from(context).inflate(R.layout.list_ingredients, parent, false));
    }

    //delegate the implementation of binding to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull IngredientsViewholder holder, int position) {
        holder.textView_ingredients_name.setText(list.get(position).name);
        //make the textview as selected
        holder.textView_ingredients_name.setSelected(true);
        holder.textView_ingredients_quantity.setText(list.get(position).original);
        //make the textview as selected
        holder.textView_ingredients_quantity.setSelected(true);

        //load the imageView using Picasso
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/" + list.get(position).image).into(holder.imageView_ingredients);
    }

    /*returns the total number of items in the dataset held by the called by InstructionsResponse
      after being populated using the get method in RecipeApiResponse
     */
    @Override
    public int getItemCount() {
        return list.size();
    }


}


//initialise the UI
class IngredientsViewholder extends RecyclerView.ViewHolder {
    TextView textView_ingredients_quantity, textView_ingredients_name;
    ImageView imageView_ingredients;

    public IngredientsViewholder(@NonNull View itemView) {
        super(itemView);
        textView_ingredients_name = itemView.findViewById(R.id.textView_ingredients_name);
        textView_ingredients_quantity = itemView.findViewById(R.id.textView_ingredients_quantity);
        imageView_ingredients = itemView.findViewById(R.id.imageView_ingredients);
    }
}
