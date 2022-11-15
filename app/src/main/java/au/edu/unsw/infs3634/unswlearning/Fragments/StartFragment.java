package au.edu.unsw.infs3634.unswlearning.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import au.edu.unsw.infs3634.unswlearning.R;

public class StartFragment extends Fragment {

    private ProgressBar startProgress;
    private TextView startFeedbackText;
    private static final String START_TAG = "START_LOG";
    private NavController navController;

    private FirebaseAuth firebaseAuth;

    public StartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);

        startProgress = view.findViewById(R.id.start_progress);
        startFeedbackText = view.findViewById(R.id.start_feedback);
        startFeedbackText.setText("Checking user account...");
    }

    @Override
    public void onStart() {
        super.onStart();
        //create new user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //check if user exists
        if (currentUser == null) {
            //if account doesnt exits
            startFeedbackText.setText("Creating Account...");
            //create a new account
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        startFeedbackText.setText("Account Created...");
                        navController.navigate(R.id.action_startFragment_to_listFragment);
                    }
                    else {
                        Log.d(START_TAG, "Start log: " +  task.getException());
                    }
                }
            });
        }
        else {
            //navigate to home page if user is already logged in
            startFeedbackText.setText("Logged in...");
            navController.navigate(R.id.action_startFragment_to_listFragment);

        }

    }
}