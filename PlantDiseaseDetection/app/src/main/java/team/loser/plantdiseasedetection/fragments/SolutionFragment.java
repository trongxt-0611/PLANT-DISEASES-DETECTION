package team.loser.plantdiseasedetection.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.models.DiseaseSolution;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SolutionFragment extends Fragment {
    private View mView;
    private TextView tvTest;
    private DiseaseSolution mDiseaseSolution;
    // TODO: Rename and change types and number of parameters
    public static SolutionFragment newInstance(DiseaseSolution solution) {
        SolutionFragment fragment = new SolutionFragment(solution);
        return fragment;
    }

    public SolutionFragment(DiseaseSolution solution) {
        // Required empty public constructor
        this.mDiseaseSolution = solution;
        //TODO: set data
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_solution, container, false);
        tvTest = mView.findViewById(R.id.tv_solution);

        tvTest.setText(mDiseaseSolution.getNameDisease_VN());
        return mView;
    }
}