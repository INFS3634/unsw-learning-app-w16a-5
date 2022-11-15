package au.edu.unsw.infs3634.unswlearning.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import au.edu.unsw.infs3634.unswlearning.R;


public class ResultFragment extends Fragment {

    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String currUserId;
    private String quizId;
    private Long resultPercent;
    private TextView results_correct, results_wrong, results_missed, results_percent;
    private ProgressBar results_progress;
    private Button results_home_btn;

    public ResultFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();

        //get user ID
        if (firebaseAuth.getCurrentUser() != null) {
            currUserId = firebaseAuth.getCurrentUser().getUid();
        }
        else {
            //go back to home page
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();

        //Initialise UI elements
        results_correct = view.findViewById(R.id.results_correct_text);
        results_wrong = view.findViewById(R.id.results_wrong_text);
        results_missed = view.findViewById(R.id.results_missed_text);
        results_percent = view.findViewById(R.id.results_percent);
        results_home_btn = view.findViewById(R.id.results_home_btn);
        results_progress = view.findViewById(R.id.results_progress);

        results_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_resultFragment_to_listFragment);
            }
        });

        //get results
        firebaseFirestore.collection("QuizList")
                .document(quizId).collection("Results")
                .document(currUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {

                            //calculate progress
                            DocumentSnapshot result = task.getResult();
                            resultPercent = quizPercent(result);
                            results_correct.setText(result.get("correct").toString());
                            results_wrong.setText(result.get("incorrect").toString());
                            results_missed.setText(result.get("missed").toString());

                            results_percent.setText(resultPercent + "%");
                            //set the progress bar to reflect the percentage
                            results_progress.setProgress(resultPercent.intValue());

                        }
                    }
                });

    }

    //method for calculating percentage progress
    public Long quizPercent(DocumentSnapshot databaseKey) {
        Long correct = databaseKey.getLong("correct");
        Long wrong = databaseKey.getLong("incorrect");
        Long missed = databaseKey.getLong("missed");

        Long totalQuestions = correct + wrong + missed;
        Long percent = (correct * 100)/ totalQuestions;
        return percent;
    }
}