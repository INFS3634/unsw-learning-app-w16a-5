package au.edu.unsw.infs3634.unswlearning.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.ModuleListModel;
import au.edu.unsw.infs3634.unswlearning.R;

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ModuleListViewholder> {

    private List<ModuleListModel> moduleListModels;
    private OnModuleItemClicked onModuleItemClicked;

    public ModuleListAdapter(OnModuleItemClicked onModuleItemClicked) {
        this.onModuleItemClicked = onModuleItemClicked;
    }

    public void setModuleListModels(List<ModuleListModel> moduleListModels) {
        this.moduleListModels = moduleListModels;
    }


    @NonNull
    @Override
    public ModuleListViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_list_item, parent, false);
        return new ModuleListViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleListViewholder holder, int position) {
        holder.module_title.setText((moduleListModels.get(position).getTitle()));
        //holder.module_completion.setText((moduleListModels.get(position).getCompleted()));

        //get the URL as a string
        String imageUrl = moduleListModels.get(position).getImage();
        //load image in to imageViews of the module_list_item
        Picasso.get().load(imageUrl).into(holder.module_button);
    }

    @Override
    public int getItemCount() {
        //if a null value is here, it will get an error since quizListModels will be empty when first launched
        if (moduleListModels == null) {
            return 0;
        }
        else {
            return moduleListModels.size();
        }
    }

    public class ModuleListViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView module_title, module_completion;
        private ImageButton module_button;

        public ModuleListViewholder(@NonNull View itemView) {
            super(itemView);

            //initialise UI
            //module_completion = itemView.findViewById(R.id.module_completion);
            module_title = itemView.findViewById(R.id.module_title);
            module_button = itemView.findViewById(R.id.module_button);


            module_button.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            //when user clicks the ImageButton get position of the user click
            onModuleItemClicked.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnModuleItemClicked {
        void onItemClicked(int position);
    }
}
