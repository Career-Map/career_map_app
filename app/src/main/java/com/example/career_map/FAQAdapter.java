package com.example.career_map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.career_map.databinding.FaqItemBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FAQAdapter extends FirestoreRecyclerAdapter<FAQModel, FAQAdapter.ViewHolder> {

    private FaqItemBinding binding;
    private Context context;

    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public FAQAdapter(@NonNull FirestoreRecyclerOptions<FAQModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FAQModel model) {
        holder.tvQuestion.setText(model.getFaqQuestion());
        holder.tvAnswer.setText(model.getFaqAnswer());

        boolean isExpanded = model.isExpanded();
        holder.tvAnswer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FaqItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion, tvAnswer;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = binding.tvAboutQuestion;
            tvAnswer = binding.tvAboutAnswer;
            cardView = binding.faqCard;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(getAdapterPosition());
                    FAQModel faqModel = getItem(getAdapterPosition());
                    faqModel.setExpanded(!faqModel.isExpanded());
                }
            });


        }
    }
}
