package au.edu.unsw.infs3634.unswlearning.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.QuizListModel;
import au.edu.unsw.infs3634.unswlearning.R;
import retrofit2.http.POST;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    private List<QuizListModel> quizListModels;
    private OnQuizItemClicked onQuizItemClicked;

    public QuizListAdapter(OnQuizItemClicked onQuizItemClicked) {
        this.onQuizItemClicked = onQuizItemClicked;

    }

    public void setQuizListModels(List<QuizListModel> quizListModels) {
        this.quizListModels = quizListModels;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.list_title.setText((quizListModels.get(position).getName()));

        //get the URL as a string
        String imageUrl = quizListModels.get(position).getImage();
        //load image in to imageViews of the quiz_list_item
        Picasso.get().load(imageUrl).into(holder.list_image);
        holder.list_desc.setText(quizListModels.get(position).getDesc());
        holder.list_difficulty.setText(quizListModels.get(position).getLevel());
    }

    @Override
    public int getItemCount() {
        //if a null value is here, it will get an error since quizListModels will be empty when first launched
        if (quizListModels == null) {
            return 0;
        }
        else {
            return quizListModels.size();
        }

    }

    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private TextView list_title, list_desc, list_difficulty;
        private Button list_btn;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.list_image);
            list_title = itemView.findViewById(R.id.list_title);
            list_desc = itemView.findViewById(R.id.list_desc);
            list_difficulty = itemView.findViewById(R.id.list_difficulty);
            list_btn = itemView.findViewById(R.id.list_btn);

            list_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //when the user clicks button
            onQuizItemClicked.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnQuizItemClicked {
        void onItemClicked(int position);
    }
}
