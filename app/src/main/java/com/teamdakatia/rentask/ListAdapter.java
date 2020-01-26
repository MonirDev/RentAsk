package com.teamdakatia.rentask;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;
    ArrayList<AddData> adList;

    public ListAdapter(Context context, ArrayList<AddData> adList) {
        this.context = context;
        this.adList = adList;
    }

    @Override
    public int getCount() {
        return adList.size();
    }

    @Override
    public Object getItem(int i) {
        return adList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_model,viewGroup,false);
        }

        TextView mArea = view.findViewById(R.id.areaName);
        TextView mHome = view.findViewById(R.id.mhomeType);
        TextView mPrice = view.findViewById(R.id.mprice);
        TextView mroom = view.findViewById(R.id.nRoom);
        TextView mBath = view.findViewById(R.id.nBath);
        TextView mFloor = view.findViewById(R.id.floorN);
        TextView mAdress = view.findViewById(R.id.fullAddress);
        TextView mrent = view.findViewById(R.id.rent);

        AddData s = (AddData) this.getItem(i);

        mArea.setText(s.getArea_name());
        mHome.setText(s.getHome_type());
        mPrice.setText(s.getPrice()+" TK");
        mroom.setText(s.getnRooms());
        mBath.setText(s.getnBath());
        mFloor.setText(s.getFloorN()+ " Floor");
        mAdress.setText(s.getShort_address());
        mrent.setText(s.getRent_start());
        return view;
    }
}
