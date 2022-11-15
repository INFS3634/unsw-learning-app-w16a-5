package au.edu.unsw.infs3634.unswlearning.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Adapters.QuizListAdapter;
import au.edu.unsw.infs3634.unswlearning.Model.QuizListModel;
import au.edu.unsw.infs3634.unswlearning.Model.QuizListViewModel;
import au.edu.unsw.infs3634.unswlearning.R;


public class ListFragment extends Fragment implements QuizListAdapter.OnQuizItemClicked {

    private NavController navController;
    private RecyclerView listView;
    private QuizListViewModel quizListViewModel;
    //initialise adapter class
    private QuizListAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        //setting our recyclerView
        listView = view.findViewById(R.id.list_view);
        adapter = new QuizListAdapter(this);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initialise the QuizListViewModel class
        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        //checks for changes on quizListViewModel live data
        quizListViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                adapter.setQuizListModels(quizListModels);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onItemClicked(int position) {

        ListFragmentDirections.ActionListFragmentToDetailFragment action = ListFragmentDirections.actionListFragmentToDetailFragment();
        //position is stated in the nav_graph
        action.setPosition(position);
        //DetailFragment is passed the position of the item since we are using the action
        navController.navigate(action);
    }
}
