package au.edu.unsw.infs3634.unswlearning.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.FirebaseRepository.FirebaseRepository;

public class QuizListViewModel extends ViewModel implements FirebaseRepository.OnFirestoreTaskComplete {

    //sets the data received from FirebaseRepository as LiveData object
    //if anything is changed, it will notify the ListFragment
    private MutableLiveData<List<QuizListModel>> quizListModelData = new MutableLiveData<>();


    //ViewModel class stores and manages the fragment data so it can react to configuration changes
    //LiveData is an observable data holder that is life-cycle aware of changes
    //is used to wrap the List<QuizListModel> list of objects
    public LiveData<List<QuizListModel>> getQuizListModelData() {
        return quizListModelData;
    }

    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);

    public QuizListViewModel() {
        firebaseRepository.getQuizData();
    }


    @Override
    public void quizListDataAdded(List<QuizListModel> quizListModelsList) {
        quizListModelData.setValue(quizListModelsList);
    }

    @Override
    public void onError(Exception e) {

    }
}
