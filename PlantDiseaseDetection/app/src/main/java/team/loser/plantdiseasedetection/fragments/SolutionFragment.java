package team.loser.plantdiseasedetection.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.models.DiseaseSolution;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SolutionFragment extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static SolutionFragment newInstance(DiseaseSolution solution) {
        SolutionFragment fragment = new SolutionFragment(solution);
        return fragment;
    }

    public SolutionFragment(DiseaseSolution solution) {
        // Required empty public constructor
        //TODO: set data
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solution, container, false);
    }
}