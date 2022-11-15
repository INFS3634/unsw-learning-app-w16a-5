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

import au.edu.unsw.infs3634.unswlearning.Adapters.ModuleListAdapter;
import au.edu.unsw.infs3634.unswlearning.Api.Recipe;
import au.edu.unsw.infs3634.unswlearning.Model.ModuleListModel;
import au.edu.unsw.infs3634.unswlearning.Model.ModuleListViewModel;
import au.edu.unsw.infs3634.unswlearning.Model.QuizListModel;
import au.edu.unsw.infs3634.unswlearning.Model.QuizListViewModel;
import au.edu.unsw.infs3634.unswlearning.R;


public class ModuleFragment extends Fragment implements ModuleListAdapter.OnModuleItemClicked {

    private NavController navController;
    private RecyclerView listModules;
    private ModuleListViewModel moduleListViewModel;
    //initialise adapter class
    private ModuleListAdapter adapter;


    public ModuleFragment() {
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_module, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        //set the recyclerView
        listModules = view.findViewById(R.id.list_modules_view);
        adapter = new ModuleListAdapter(this);
        listModules.setLayoutManager(new LinearLayoutManager(getContext()));
        listModules.setHasFixedSize(true);
        listModules.setAdapter(adapter);
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
                adapter.setModuleListModels(moduleListModels);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClicked(int position) {

        ModuleFragmentDirections.ActionModuleFragmentToModuleDetailFragment action = ModuleFragmentDirections.actionModuleFragmentToModuleDetailFragment();
        action.setPosition(position);
        navController.navigate(action);


    }
}