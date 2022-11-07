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

import au.edu.unsw.infs3634.unswlearning.Api.Step;
import au.edu.unsw.infs3634.unswlearning.R;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepViewHolder> {
    Context context;
    List<Step> list;

    public InstructionStepAdapter(Context context, List<Step> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionStepViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_steps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionStepViewHolder holder, int position) {
        holder.textView_instructions_step_number.setText(String.valueOf(list.get(position).number));
        holder.textView_instructions_step_title.setText(list.get(position).step);
        //set view for the ingredients
        holder.recycler_instructions_ingredients.setHasFixedSize(true);
        holder.recycler_instructions_ingredients.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        InstructionsIngredientsAdapter instructionsIngredientsAdapter = new InstructionsIngredientsAdapter(context, list.get(position).ingredients);
        holder.recycler_instructions_ingredients.setAdapter(instructionsIngredientsAdapter);

        //set view for the equipment used
        holder.recycler_instructions_equipment.setHasFixedSize(true);
        holder.recycler_instructions_equipment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        InstructionsEquipmentAdapter instructionsEquipmentAdapter = new InstructionsEquipmentAdapter(context, list.get(position).equipment);
        holder.recycler_instructions_equipment.setAdapter(instructionsEquipmentAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionStepViewHolder extends RecyclerView.ViewHolder {
    TextView textView_instructions_step_number, textView_instructions_step_title;
    RecyclerView recycler_instructions_equipment, recycler_instructions_ingredients;

    public InstructionStepViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_instructions_step_number = itemView.findViewById(R.id.textView_instructions_step_number);
        textView_instructions_step_title = itemView.findViewById(R.id.textView_instructions_step_title);
        recycler_instructions_equipment = itemView.findViewById(R.id.recycler_instructions_equipment);
        recycler_instructions_ingredients = itemView.findViewById(R.id.recycler_instructions_ingredients);

    }
}
