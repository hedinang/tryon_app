package com.example.fashion.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fashion.R;
import com.example.fashion.service.ClothService;
import java.util.ArrayList;
import java.util.List;

public class ClothsAdapter extends
        RecyclerView.Adapter<ClothsAdapter.ViewHolder> {
    private List<String> listCloths;
    private boolean isRound;
    ClothService clothService = new ClothService();

    public ClothsAdapter() {
        this.listCloths = new ArrayList<>();

    }

    public void setData(List<String> listCloths) {
        this.listCloths = listCloths;
        if (listCloths.size() / 2 * 2 < listCloths.size())
            this.isRound = true;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewLeft;
        public ImageView imageViewRight;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewLeft = itemView.findViewById(R.id.cloth_item_left);
            imageViewRight = itemView.findViewById(R.id.cloth_item_right);
        }
    }

    @Override
    public ClothsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_cloth, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ClothsAdapter.ViewHolder holder, int position) {
        String clothLeft = listCloths.get(position * 2);
        try {
            clothService.showCloth(clothLeft, holder.imageViewLeft);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isRound) {
            String clothRight = listCloths.get(position * 2 + 1);
            try {
                clothService.showCloth(clothRight, holder.imageViewRight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return listCloths.size() / 2;
    }
}