package com.browsejoy.games.app.view.fragments.withdraw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.browsejoy.games.R;
import com.kennyc.view.MultiStateView;

/**
 * Created by Gerti on 12/22/2018.
 */

public class HomeFragment extends Fragment implements MultiStateView.StateListener {

    public static com.browsejoy.games.app.view.fragments.home.HomeFragment newInstance() {
        com.browsejoy.games.app.view.fragments.home.HomeFragment fragment = new com.browsejoy.games.app.view.fragments.home.HomeFragment();
        return fragment;
    }

    View view;

    protected MultiStateView mMultiStateView;
    protected RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.withdraw_fragment, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setPadding(25, 25, 25, 25);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(itemAnimator);

        return view;
    }

    @Override
    public void onStateChanged(int i) {

    }
}