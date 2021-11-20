package team.loser.plantdiseasedetection.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.Sqlite.HistoryDatabase;
import team.loser.plantdiseasedetection.adapters.HistoryAdapter;
import team.loser.plantdiseasedetection.models.Disease;
import team.loser.plantdiseasedetection.utils.SpacingItemDecorator;

public class HistoryFragment extends Fragment {
    private RecyclerView rcvHistory;
    private HistoryAdapter historyAdapter;
    private List<Disease> mListDiseases;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setControls(view);
        setEvents();
        return view;
    }

    private void setEvents() {
        loadData();
    }
    private  void loadData(){
        mListDiseases = HistoryDatabase.getInstance(getContext()).historyDAO().getListDiseases();
        historyAdapter.setData(mListDiseases);
    }
    private void setControls(View view) {
        rcvHistory = view.findViewById(R.id.rcv_predict_history);
        historyAdapter = new HistoryAdapter(mListDiseases, new HistoryAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Disease disease) {
                deleteHistory(disease);
            }
        });
        mListDiseases = new ArrayList<>();
        historyAdapter.setData(mListDiseases);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvHistory.setLayoutManager(linearLayoutManager);
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(10);
        rcvHistory.addItemDecoration(itemDecorator);

        rcvHistory.setAdapter(historyAdapter);

    }

    private void deleteHistory(Disease disease) {
        HistoryDatabase.getInstance(getContext()).historyDAO().deleteDisease(disease);
        Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
        loadData();
    }
}
