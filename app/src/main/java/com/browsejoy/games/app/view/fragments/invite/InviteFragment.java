package com.browsejoy.games.app.view.fragments.invite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.browsejoy.games.R;
import com.browsejoy.games.app.utils.MyExceptionHandler;
import com.browsejoy.games.app.view.fragments.home.HomeFragment;

/**
 * Created by Gerti on 9/20/2018.
 */

public class InviteFragment extends Fragment {

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.invite_fragment, null);

        //Force restart the app if it crashes
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity()));

        return view;
    }
}

