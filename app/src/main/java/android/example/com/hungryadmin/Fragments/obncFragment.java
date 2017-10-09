package android.example.com.hungryadmin.Fragments;

import android.app.Fragment;
import android.example.com.hungryadmin.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aakash on 07-10-2017.
 */

public class obncFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View obv= inflater.inflate(R.layout.activity_obnc,null);

        return obv;
    }


    //title of fragment


    @Override
    public String toString() {
        return "NOT CONSUMED";
    }
}
