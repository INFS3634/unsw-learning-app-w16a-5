package au.edu.unsw.infs3634.unswlearning.Model;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.FirebaseRepository.FirebaseRepositoryLearnData;

public class ModuleListViewModel extends ViewModel implements FirebaseRepositoryLearnData.OnFirestoreLearnTaskComplete {

    //sets the data received from FirebaseRepository as LiveData object
    //if anything is changed, it will notify the ModuleFragment
    //this follows the MVVM architecture
    private MutableLiveData<List<ModuleListModel>> moduleListModelData = new MutableLiveData<>();

    //ViewModel class stores and manages the fragment data so it can react to configuration changes
    //LiveData is an observable data holder that is life-cycle aware of changes
    //is used to wrap the List<ModuleListModel> list of objects
    public LiveData<List<ModuleListModel>> getModuleListModelData() {
        return moduleListModelData;
    }

    private FirebaseRepositoryLearnData firebaseRepository = new FirebaseRepositoryLearnData(this);

    public ModuleListViewModel() {
        firebaseRepository.getQuizData();
    }


    @Override
    public void moduleListDataAdded(List<ModuleListModel> moduleListModelList) {
        moduleListModelData.setValue(moduleListModelList);

    }

    @Override
    public void onError(Exception e) {

    }
}
