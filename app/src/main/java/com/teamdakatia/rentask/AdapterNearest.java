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

public class AdapterNearest extends RecyclerView.Adapter<CustomAdapter.CustomViewHolsder>{
    private Context context;
    private List<AddData> mList;

    public AdapterNearest(Context context, List<AddData> mList) {
        this.context = context;
        this.mList = mList;
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
    public CustomAdapter.CustomViewHolsder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearest_list, parent, false);
        CustomAdapter.CustomViewHolsder item = new CustomAdapter.CustomViewHolsder(v);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolsder holder, final int i) {
        AddData currentItem = mList.get(i);

        holder.mArea.setText(currentItem.getArea_name());
        holder.mHome.setText(currentItem.getHome_type());
        holder.mPrice.setText(currentItem.getPrice() + " TK");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdDetailsCustomer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("img1",mList.get(i).getImgUrl1());
                intent.putExtra("img2",mList.get(i).getImgUrl2());
                intent.putExtra("img3",mList.get(i).getImgUrl3());
                intent.putExtra("home_type",mList.get(i).getHome_type());
                intent.putExtra("fullAddress",mList.get(i).getShort_address());
                intent.putExtra("price",mList.get(i).getPrice()+" TK");
                intent.putExtra("room",mList.get(i).getnRooms());
                intent.putExtra("bath",mList.get(i).getnBath());
                intent.putExtra("contact",mList.get(i).getPost_phone_number());
                intent.putExtra("floor",mList.get(i).getFloorN() + " Floor");
                intent.putExtra("rent",mList.get(i).getRent_start());
                intent.putExtra("checkvalue",mList.get(i).getCheck_value());
                intent.putExtra("division",mList.get(i).getDivision());
                intent.putExtra("district",mList.get(i).getDistrict());
                intent.putExtra("area",mList.get(i).getArea_name());
                context.getApplicationContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

/* public AdapterNearest(Context context,List<AddData> mList) {
        this.context = context;
        this.mList = mList;
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
    public CustomAdapter.CustomViewHolsder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_model, parent, false);
        CustomAdapter.CustomViewHolsder item = new CustomAdapter.CustomViewHolsder(v);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolsder holder, final int i) {
        AddData currentItem = mList.get(i);

        holder.mArea.setText(currentItem.getArea_name());
        holder.mHome.setText(currentItem.getHome_type());
        holder.mPrice.setText(currentItem.getPrice() + " TK");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdDetailsCustomer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("img1",mList.get(i).getImgUrl1());
                intent.putExtra("img2",mList.get(i).getImgUrl2());
                intent.putExtra("img3",mList.get(i).getImgUrl3());
                intent.putExtra("home_type",mList.get(i).getHome_type());
                intent.putExtra("fullAddress",mList.get(i).getShort_address());
                intent.putExtra("price",mList.get(i).getPrice()+" TK");
                intent.putExtra("room",mList.get(i).getnRooms());
                intent.putExtra("bath",mList.get(i).getnBath());
                intent.putExtra("contact",mList.get(i).getPost_phone_number());
                intent.putExtra("floor",mList.get(i).getFloorN() + " Floor");
                intent.putExtra("rent",mList.get(i).getRent_start());
                intent.putExtra("checkvalue",mList.get(i).getCheck_value());
                intent.putExtra("division",mList.get(i).getDivision());
                intent.putExtra("district",mList.get(i).getDistrict());
                intent.putExtra("area",mList.get(i).getArea_name());
                context.getApplicationContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }*/
}
