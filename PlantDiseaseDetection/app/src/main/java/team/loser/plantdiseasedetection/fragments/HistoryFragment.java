package team.loser.plantdiseasedetection.fragments;

import android.app.ProgressDialog;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.loser.plantdiseasedetection.Api.ApiServices;
import team.loser.plantdiseasedetection.HomeActivity;
import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.Sqlite.HistoryDatabase;
import team.loser.plantdiseasedetection.adapters.HistoryAdapter;
import team.loser.plantdiseasedetection.models.Disease;
import team.loser.plantdiseasedetection.models.DiseaseSolution;
import team.loser.plantdiseasedetection.utils.SpacingItemDecorator;

public class HistoryFragment extends Fragment {
    private RecyclerView rcvHistory;
    private HistoryAdapter historyAdapter;
    private List<Disease> mListDiseases;
    private ProgressDialog mLoader;
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
        mLoader = new ProgressDialog(getActivity());
        mLoader.setMessage("Please wait...");
        rcvHistory = view.findViewById(R.id.rcv_predict_history);
        historyAdapter = new HistoryAdapter(mListDiseases, new HistoryAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Disease disease) {
               deleteHistory(disease);
            }
            @Override
            public void onClickShowSolution(Disease disease){
                callApiSolution(disease.getResponse_class());
            };

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
    private void callApiSolution(String nameDisease) {
 //       Toast.makeText(getContext(), "Đợi chút", Toast.LENGTH_SHORT).show();
        mLoader.show();
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), nameDisease);
        ApiServices.apiServices.getSolution(requestBodyName).enqueue(new Callback<DiseaseSolution>() {
            @Override
            public void onResponse(Call<DiseaseSolution> call, Response<DiseaseSolution> response) {
                mLoader.dismiss();
                DiseaseSolution diseaseInfo = response.body();
                if(diseaseInfo != null) {
//                    Toast.makeText(getContext(),"ddasdasda"+diseaseInfo.toString(), Toast.LENGTH_SHORT).show();
                    //TODO: intent => Solution Page
                    ((HomeActivity)getActivity()).ShowSolutionFragment(diseaseInfo);
                }

            }

            @Override
            public void onFailure(Call<DiseaseSolution> call, Throwable t) {
                mLoader.dismiss();
                Toast.makeText(getActivity(), "Call api failed",Toast.LENGTH_LONG).show();
            }
        });
    }

}
