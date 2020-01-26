package com.teamdakatia.rentask;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolsder>{
    private Context context;
    private List<AddData> postList;
    private String id;

    public CustomAdapter(Context context,List<AddData> postList, String id) {
        this.context = context;
        this.postList = postList;
        this.id = id;
    }

    public static class CustomViewHolsder extends RecyclerView.ViewHolder{
        public TextView mArea, mPrice, mHome;
        CardView card;

        public CustomViewHolsder(@NonNull View itemView) {
            super(itemView);
            mArea = itemView.findViewById(R.id.area_name);
            mPrice = itemView.findViewById(R.id.mprice);
            mHome = itemView.findViewById(R.id.mhomeType);
            card = itemView.findViewById(R.id.card);
        }
    }

    @NonNull
    @Override
    public CustomViewHolsder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dashboard, parent, false);
        CustomViewHolsder item = new CustomViewHolsder(v);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolsder holder, final int position) {
        AddData currentItem = postList.get(position);

        holder.mArea.setText(currentItem.getArea_name());
        holder.mHome.setText(currentItem.getHome_type());
        holder.mPrice.setText(currentItem.getPrice() + " TK");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("img1",postList.get(position).getImgUrl1());
                intent.putExtra("img2",postList.get(position).getImgUrl2());
                intent.putExtra("img3",postList.get(position).getImgUrl3());
                intent.putExtra("home_type",postList.get(position).getHome_type());
                intent.putExtra("fullAddress",postList.get(position).getShort_address());
                intent.putExtra("price",postList.get(position).getPrice()+" TK");
                intent.putExtra("room",postList.get(position).getnRooms());
                intent.putExtra("bath",postList.get(position).getnBath());
                intent.putExtra("contact",postList.get(position).getPost_phone_number());
                intent.putExtra("floor",postList.get(position).getFloorN());
                intent.putExtra("rent",postList.get(position).getRent_start());
                intent.putExtra("checkvalue",postList.get(position).getCheck_value());
                intent.putExtra("key",postList.get(position).getPostId());
                intent.putExtra("id",id);
                context.getApplicationContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
