package com.example.fashion.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fashion.MainActivity;
import com.example.fashion.R;
import com.example.fashion.endpoint.ClothEndpoint;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static androidx.core.content.ContextCompat.startActivity;

public class ClothsAdapter extends
        RecyclerView.Adapter<ClothsAdapter.ViewHolder> {
    private ArrayList<String> listCloths;
    private boolean isRound;
    private ClothEndpoint clothEndpoint;

    public ClothsAdapter(ArrayList<String> listCloths, ClothEndpoint clothEndpoint) {
        this.listCloths = listCloths;
        this.clothEndpoint = clothEndpoint;
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

    public ResponseBody showCloth(String id) throws IOException {
//        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.131:5000")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//        ClothEndpoint service = retrofit.create(ClothEndpoint.class);
        Call<ResponseBody> call = this.clothEndpoint.getCloth("000003_1.jpg");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody list = response.body();
                System.out.println("aaaa");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("aaaa");
            }
        });
        return null;
    }

    @Override
    public void onBindViewHolder(ClothsAdapter.ViewHolder holder, int position) {
        String clothLeft = listCloths.get(position * 2);
        String root = "http://192.168.1.131/ACGPN_traindata/train_color/";
        try {
            byte[] bytes = showCloth(clothLeft).bytes();
            final Bitmap bitmapLeft = BitmapFactory.decodeByteArray(showCloth(clothLeft).bytes(), 0, bytes.length);
            ImageView imageViewLeft = holder.imageViewLeft;
            imageViewLeft.setImageBitmap(bitmapLeft);
            imageViewLeft.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("cloth", bitmapLeft);
                    v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isRound) {
            String clothRight = listCloths.get(position * 2 + 1);
            try {
                byte[] bytes = showCloth(clothLeft).bytes();
                final Bitmap bitmapRight = BitmapFactory.decodeByteArray(showCloth(clothLeft).bytes(), 0, bytes.length);
                ImageView imageViewRight = holder.imageViewRight;
                imageViewRight.setImageBitmap(bitmapRight);
                imageViewRight.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.putExtra("cloth", bitmapRight);
                        v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return listCloths.size() / 2;
    }
}