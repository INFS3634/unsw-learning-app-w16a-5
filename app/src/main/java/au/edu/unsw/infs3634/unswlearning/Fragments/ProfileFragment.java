package au.edu.unsw.infs3634.unswlearning.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Adapters.ProgressModuleCheckAdapter;
import au.edu.unsw.infs3634.unswlearning.Model.ModuleListModel;
import au.edu.unsw.infs3634.unswlearning.Model.ModuleListViewModel;
import au.edu.unsw.infs3634.unswlearning.R;


public class ProfileFragment extends Fragment {

    private NavController navController;
    private RecyclerView progressList;
    private ModuleListViewModel moduleListViewModel;
    private ProgressModuleCheckAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        progressList = view.findViewById(R.id.modules_completed_recycler);
        adapter = new ProgressModuleCheckAdapter();
        progressList.setLayoutManager(new LinearLayoutManager(getContext()));
        progressList.setHasFixedSize(true);
        progressList.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initialise the ModuleListViewModel class
        moduleListViewModel = new ViewModelProvider(getActivity()).get(ModuleListViewModel.class);
        //checks for changes on quizListViewModel live data
        moduleListViewModel.getModuleListModelData().observe(getViewLifecycleOwner(), new Observer<List<ModuleListModel>>() {
            @Override
            public void onChanged(List<ModuleListModel> moduleListModels) {
                adapter.setProgressListModels(moduleListModels);
                adapter.notifyDataSetChanged();
            }
        });
    }
}