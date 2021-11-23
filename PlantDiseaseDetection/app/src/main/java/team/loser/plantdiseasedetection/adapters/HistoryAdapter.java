package team.loser.plantdiseasedetection.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.loser.plantdiseasedetection.Api.ApiServices;
import team.loser.plantdiseasedetection.HomeActivity;
import team.loser.plantdiseasedetection.R;
import team.loser.plantdiseasedetection.models.Disease;
import team.loser.plantdiseasedetection.models.DiseaseSolution;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private List<Disease> mListDeDiseases;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private IClickListener mIClickListener;

    public interface IClickListener{
        void onClickDeleteItem(Disease disease);
        void onClickShowSolution(Disease disease);
    }
    public HistoryAdapter(List<Disease> list, IClickListener iClickListener){
        this.mListDeDiseases = list;
        this.mIClickListener = iClickListener;
    }
    public void setData(List<Disease> list){
        this.mListDeDiseases = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Disease disease = mListDeDiseases.get(position);
        if(disease == null) return;
        String nameDisease =  disease.getResponse_class();
        viewBinderHelper.bind(holder.swipeRevealLayout, disease.getId()+"");
        holder.tvDiseaseName.setText(disease.getResponse_class());
        holder.tvProbabilities.setText(disease.getResponse_confident());
        holder.tvTimeStamp.setText(disease.getResponse_time());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickShowSolution(disease);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickListener.onClickDeleteItem(disease);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListDeDiseases != null) return mListDeDiseases.size();
        return 0;
    }
    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDiseaseName, tvProbabilities, tvTimeStamp;
        private ImageButton btnDelete;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout linearLayout;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout_history);
            tvDiseaseName = itemView.findViewById(R.id.tv_disease_name);
            tvProbabilities = itemView.findViewById(R.id.tv_probabilities);
            tvTimeStamp = itemView.findViewById(R.id.tv_time_stamp);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            linearLayout = itemView.findViewById(R.id.ll_history);
        }
    }
}
