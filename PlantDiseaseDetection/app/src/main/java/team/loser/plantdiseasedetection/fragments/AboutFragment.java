package team.loser.plantdiseasedetection.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import team.loser.plantdiseasedetection.R;

public class AboutFragment extends Fragment {
    public static final String ABOUT_FRAGMENT_NAME = AboutFragment.class.getName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container,false);
    }
}
