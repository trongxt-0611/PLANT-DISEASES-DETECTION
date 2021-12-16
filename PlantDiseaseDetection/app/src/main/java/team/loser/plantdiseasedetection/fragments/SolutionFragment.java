package team.loser.plantdiseasedetection.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.models.DiseaseSolution;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SolutionFragment extends Fragment {
    public static final String SOLUTION_FRAGMENT_NAME = SolutionFragment.class.getName();
    private View mView;
    private TextView tvNameEN, tvNameVN, tvSymptom, tvPathogen, tvCure;
    private ImageView img1 ,img2, img3, img4;
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
        setControls(mView);
        setEvents();
        return mView;
    }

    private void setEvents() {
        tvNameEN.setText(mDiseaseSolution.getNameDisease_ENG());
        tvNameVN.setText(mDiseaseSolution.getNameDisease_VN());
        ArrayList<String> urls = new ArrayList<>();
        urls.add(mDiseaseSolution.getImage1());
        urls.add(mDiseaseSolution.getImage2());
        urls.add(mDiseaseSolution.getImage3());
        urls.add(mDiseaseSolution.getImage4());

        Glide.with(getActivity()).load(urls.get(0)).error(R.drawable.logo).into(img1);
        Glide.with(getActivity()).load(urls.get(1)).error(R.drawable.logo).into(img2);
        Glide.with(getActivity()).load(urls.get(2)).error(R.drawable.logo).into(img3);
        Glide.with(getActivity()).load(urls.get(3)).error(R.drawable.logo).into(img4);

        String symptom[] = mDiseaseSolution.getSymptom().split("!");
        String sym = "";
        for (int i=0; i< symptom.length; i++){
            sym += symptom[i] + "\n";
        }
        tvSymptom.setText(sym);

        String pathogen[] = mDiseaseSolution.getPathogens().split("!");
        String pat = "";
        for (int i=0; i< pathogen.length; i++){
            pat += pathogen[i] + "\n";
        }
        tvPathogen.setText(pat);

        String cure[] = mDiseaseSolution.getHowtocure().split("!");
        String cu = "";
        for (int i=0; i< cure.length; i++){
            cu += cure[i] + "\n";
        }
        tvCure.setText(cu);


    }
    private void setControls(View view) {
        tvNameEN = view.findViewById(R.id.tv_solution_name_EN);
        tvNameVN = view.findViewById(R.id.tv_solution_name_VN);
        tvSymptom = view.findViewById(R.id.tv_symptom);
        tvPathogen = view.findViewById(R.id.tv_pathogen);
        tvCure = view.findViewById(R.id.tv_howtocure);
        img1 = view.findViewById(R.id.img_1) ;
        img2 = view.findViewById(R.id.img_2) ;
        img3 = view.findViewById(R.id.img_3) ;
        img4 = view.findViewById(R.id.img_4) ;
    }
}