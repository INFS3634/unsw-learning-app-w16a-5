package au.edu.unsw.infs3634.unswlearning.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Api.InstructionsReponse;
import au.edu.unsw.infs3634.unswlearning.R;
import kotlin.jvm.internal.Lambda;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsViewHolder> {

    //context or current state of the app
    Context context;
    List<InstructionsReponse> list;

    //constructor that passes the values of application context and list that holds the name of the dish and the public steps;
    //converted from JSON object
    public InstructionsAdapter(Context context, List<InstructionsReponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions, parent, false));
    }

    //delegate the implementation of binding to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull InstructionsViewHolder holder, int position) {
        //adapter for the father container for all instructions
        //creates an adapter class from the InstructionStepAdapter and is
        holder.textView_instruction_name.setText(list.get(position).name);
        holder.recycler_instruction_steps.setHasFixedSize(true);
        holder.recycler_instruction_steps.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        InstructionStepAdapter stepAdapter = new InstructionStepAdapter(context, list.get(position).steps);
        holder.recycler_instruction_steps.setAdapter(stepAdapter);
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
class InstructionsViewHolder extends RecyclerView.ViewHolder {
    TextView textView_instruction_name;
    RecyclerView recycler_instruction_steps;

    public InstructionsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_instruction_name = itemView.findViewById(R.id.textView_instruction_name);
        recycler_instruction_steps = itemView.findViewById(R.id.recycler_instruction_steps);
    }
}