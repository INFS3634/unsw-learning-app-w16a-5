package au.edu.unsw.infs3634.unswlearning.FirebaseRepository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.QuizListModel;

public class FirebaseRepository {
    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    //instance of the Firestore quiz data
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference quizRef = firebaseFirestore.collection("QuizList");

    //constructor that requires an interface
    public FirebaseRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }


    //method to query data from FireStore
    public void getQuizData() {
        quizRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    //gives us data that is in QuizList collection on Firebase and assign to QuizListModel class
                    onFirestoreTaskComplete.quizListDataAdded(task.getResult().toObjects(QuizListModel.class));
                }
                else {
                    onFirestoreTaskComplete.onError(task.getException());

                }
            }
        });
    }


    //to send data from the FirebaseRepository to the QuizViewModel, an interface is required
    public interface OnFirestoreTaskComplete {
        //gets entire list of data from QuizListModel class
        void quizListDataAdded(List<QuizListModel> quizListModelsList);
        void onError(Exception e);
    }
}
