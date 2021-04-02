package com.example.fashion.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fashion.MainActivity;
import com.example.fashion.R;

import java.util.List;

public class ClothsAdapter extends
        RecyclerView.Adapter<ClothsAdapter.ViewHolder> {
    private List<Bitmap> listCloths;
    List<String> listPaths;
    Context context;

    public ClothsAdapter(Context context, List<Bitmap> listCloths,List<String> listPaths) {
        this.listCloths = listCloths;
        this.listPaths = listPaths;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemCloth);

        }

    }

    @Override
    public ClothsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_cloth_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ClothsAdapter.ViewHolder holder, int position) {
        Bitmap clothLeft = listCloths.get(position);
        holder.imageView.setImageBitmap(clothLeft);
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("cloth", clothLeft);
            intent.putExtra("path",listPaths.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listCloths.size();
    }
}