package com.example.assignment3.plan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.databinding.RvLayoutBinding;
import com.example.assignment3.entity.WorkoutPlan;
import com.example.assignment3.report.ReportActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static List<WorkoutPlan> plans;

    public RecyclerViewAdapter(List<WorkoutPlan> plans){
        this.plans = plans;
    }

    @NonNull
    @Override
    public  RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvLayoutBinding binding = RvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final WorkoutPlan plan = plans.get(position);
        holder.binding.tvRvName.setText(plan.getPlanName());
        holder.binding.tvRvCategory.setText(plan.getCategory()+" exercise");
        holder.binding.tvRvTime.setText(plan.getTime()+(" mins"));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void addPlan(List<WorkoutPlan> workoutPlans){
        plans = workoutPlans;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RvLayoutBinding binding;
        public ViewHolder(RvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.cardView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),PlanDetailsActivity.class);
                    WorkoutPlan plan = plans.get(getAdapterPosition());
                    intent.putExtra("name",plan.getPlanName());
                    intent.putExtra("length",plan.getPlanLength());
                    intent.putExtra("time",plan.getTime());
                    intent.putExtra("category",plan.getCategory());
                    intent.putExtra("routine",plan.getPlanRoutine());

                    v.getContext().startActivity(intent);

                }
            });
        }
    }
}
