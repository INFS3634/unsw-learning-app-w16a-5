package au.edu.unsw.infs3634.unswlearning.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.QuizListModel;
import au.edu.unsw.infs3634.unswlearning.Model.QuizListViewModel;
import au.edu.unsw.infs3634.unswlearning.R;


public class DetailFragment extends Fragment implements View.OnClickListener {
    private NavController navController;
    private QuizListViewModel quizListViewModel;
    private int position;
    private FirebaseAuth firebaseAuth;
    private String currUserId;
    private Long lastAttemptResult;
    private FirebaseFirestore firebaseFirestore;
    private ImageView details_image;
    private TextView details_title, details_desc, details_diff, details_questions, details_score;
    private Button details_start_btn;
    private String quizId;
    private String quizTitle;
    private long totalQuestions = 0 ;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //get user ID
        if (firebaseAuth.getCurrentUser() != null) {
            currUserId = firebaseAuth.getCurrentUser().getUid();
        }
        else {
            //go back to home page
        }

        navController = Navigation.findNavController(view);
        //similar to get Intent for Activites, this is the equivalent for Fragments
        position = DetailFragmentArgs.fromBundle(getArguments()).getPosition();
        //returns position of the user clicks
        Log.d("APP_POSITION_LOG", "Position: " + position);

        //initialise from fragment_detail
        details_image = view.findViewById(R.id.details_image);
        details_title = view.findViewById(R.id.details_title);
        details_desc = view.findViewById(R.id.details_desc);
        details_diff = view.findViewById(R.id.details_difficulty_text);
        details_questions = view.findViewById(R.id.details_questions_text);
        details_score = view.findViewById(R.id.details_score_text);
        details_start_btn = view.findViewById(R.id.details_start_btn);

        details_start_btn.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        //checks for changes on quizListViewModel live data
        quizListViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                //once QuizListModel loads in
                details_title.setText(quizListModels.get(position).getName());
                details_desc.setText(quizListModels.get(position).getDesc());
                details_diff.setText(quizListModels.get(position).getLevel());
                details_questions.setText(quizListModels.get(position).getQuestions() + "");

                String imageUrl = quizListModels.get(position).getImage();
                Picasso.get().load(imageUrl).into(details_image);
                //assign the Quiz ID value to variable to pass onto next fragment
                quizId = quizListModels.get(position).getQuiz_id();
                //assign the Quiz Title value to variable to pass onto next fragment
                quizTitle = quizListModels.get(position).getName();
                totalQuestions = quizListModels.get(position).getQuestions();

                //load last attempt data
                loadResultsData();
            }
        });
    }

    private void loadResultsData() {
        firebaseFirestore.collection("QuizList")
                .document(quizId).collection("Results")
                .document(currUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userResult = task.getResult();
                            //checking the existence of a result from user attempt
                            if(userResult != null && userResult.exists()) {
                                lastAttemptResult = quizPercent(userResult);
                                details_score.setText(lastAttemptResult + "%");
                            }
                        }
                        else {
                            //results should still be N/A
                            details_score.setText("Not Attempted");
                        }
                    }
                });
    }

    public Long quizPercent(DocumentSnapshot databaseKey) {
        Long correct = databaseKey.getLong("correct");
        Long wrong = databaseKey.getLong("incorrect");
        Long missed = databaseKey.getLong("missed");

        Long totalQuestions = correct + wrong + missed;
        Long percent = (correct * 100)/ totalQuestions;
        return percent;
    }

    //passing quizId data and totalQuestions data to QuizFragment to populate the UI elements
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.details_start_btn:
                DetailFragmentDirections.ActionDetailFragmentToQuizFragment action = DetailFragmentDirections.actionDetailFragmentToQuizFragment();
                action.setTotalQuestions(totalQuestions);
                action.setQuizId(quizId);
                action.setQuizTitle(quizTitle);
                navController.navigate(action);
        }
    }
}