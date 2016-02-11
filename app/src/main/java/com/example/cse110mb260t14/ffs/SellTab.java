package com.example.cse110mb260t14.ffs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//import com.example.yoonchung.fragmenttest.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class SellTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sell_layout,container,false);
        return v;
    }
}