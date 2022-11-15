package au.edu.unsw.infs3634.unswlearning.FirebaseRepository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.ModuleListModel;

public class FirebaseRepositoryLearnData {
    private OnFirestoreLearnTaskComplete onFirestoreLearnTaskComplete;

    //instance of the Firestore quiz data
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference moduleRef = firebaseFirestore.collection("LearnList");

    //constructor that requires and interface to check data loaded
    public FirebaseRepositoryLearnData(OnFirestoreLearnTaskComplete onFirestoreLearnTaskComplete) {
        this.onFirestoreLearnTaskComplete = onFirestoreLearnTaskComplete;
    }

    //method to query data from Firestore
    public void getQuizData() {
        moduleRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    //gives us data that is in QuizList collection on Firebase and assign to QuizListModel class
                    onFirestoreLearnTaskComplete.moduleListDataAdded(task.getResult().toObjects(ModuleListModel.class));
                }
                else {
                    onFirestoreLearnTaskComplete.onError(task.getException());

                }
            }
        });
    }

    public interface OnFirestoreLearnTaskComplete {
        //gets entire list of data from LearnListModel class
        void moduleListDataAdded(List<ModuleListModel> moduleListModelList);
        void onError(Exception e);
    }


}
