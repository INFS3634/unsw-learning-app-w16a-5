package au.edu.unsw.infs3634.unswlearning.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.ModuleListModel;
import au.edu.unsw.infs3634.unswlearning.ProgressActivity;
import au.edu.unsw.infs3634.unswlearning.R;

public class ProgressModuleCheckAdapter extends RecyclerView.Adapter<ProgressModuleCheckAdapter.ProgressModuleCheckViewholder> {

    private List<ModuleListModel> moduleListModels;

    public void setProgressListModels (List<ModuleListModel> moduleListModels) {
        this.moduleListModels = moduleListModels;
    }
    @NonNull
    @Override
    public ProgressModuleCheckViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modules_completed_item, parent, false);
        return new ProgressModuleCheckViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressModuleCheckViewholder holder, int position) {
        holder.module_title.setText((moduleListModels.get(position).getTitle()));
        holder.module_completion.setText((moduleListModels.get(position).getCompleted()));
    }

    @Override
    public int getItemCount() {
        if (moduleListModels == null) {
            return 0;
        }
        else {
            return moduleListModels.size();
        }
    }


    public class ProgressModuleCheckViewholder extends RecyclerView.ViewHolder {

        private TextView module_title, module_completion;

        public ProgressModuleCheckViewholder(@NonNull View itemView) {
            super(itemView);

            //initialise UI
            module_title = itemView.findViewById(R.id.progress_module_title);
            module_completion = itemView.findViewById(R.id.progress_module_completion);


        }
    }
}
