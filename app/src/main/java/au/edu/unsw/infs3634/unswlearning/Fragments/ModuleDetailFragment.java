package au.edu.unsw.infs3634.unswlearning.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.ModuleListModel;
import au.edu.unsw.infs3634.unswlearning.Model.ModuleListViewModel;
import au.edu.unsw.infs3634.unswlearning.R;


public class ModuleDetailFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ModuleListViewModel moduleListViewModel;
    private String currUserId;
    private String clickedModuleId;
    private int position;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView module_learn_info, module_learn_title;
    private Button module_learn_complete;

    private List<ModuleListModel> moduleListModels;

    public ModuleDetailFragment() {
        //empty constructor for Firebase
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_module_detail, container, false);
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
        position = ModuleDetailFragmentArgs.fromBundle(getArguments()).getPosition();
        //returns the position that the user clicks

        //initialise UI
        module_learn_info = view.findViewById(R.id.module_learn_info);
        module_learn_title = view.findViewById(R.id.module_learn_title);
        module_learn_complete = view.findViewById(R.id.module_learn_complete);


        //set onclick listener for the Mark as Complete button
        module_learn_complete.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        moduleListViewModel = new ViewModelProvider(getActivity()).get(ModuleListViewModel.class);
        //checks for changes on ModuleListViewModel mutable data class
        moduleListViewModel.getModuleListModelData().observe(getViewLifecycleOwner(), new Observer<List<ModuleListModel>>() {
            @Override
            public void onChanged(List<ModuleListModel> moduleListModels) {
                //once Module ListModel loads in
                module_learn_title.setText(moduleListModels.get(position).getTitle());
                module_learn_info.setText(moduleListModels.get(position).getDesc());
                clickedModuleId = moduleListModels.get(position).getModule_id();
            }
        });


    }

    @Override
    public void onClick(View view) {
        //update the Firebase data to reflect module completion
        firebaseFirestore.collection("LearnList")
                .document(clickedModuleId)
                .update("completed", "Completed");

        navController.navigate(R.id.action_moduleDetailFragment_to_moduleFragment);
    }
}