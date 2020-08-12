package com.browsejoy.games.app.view.fragments.redeem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baoyz.widget.PullRefreshLayout;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.model.response.RedeemListModel;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.utils.SimpleDividerItemDecoration;
import com.browsejoy.games.app.utils.TimeByZone;
import com.browsejoy.games.app.view.dialogs.WithDrawPopUp;
import com.kennyc.view.MultiStateView;
import java.util.List;

/**
 * Created by Gerti on 5/24/2018.
 */
//Tab #3
public  class RedeemFragment extends Fragment implements RedeemFragmentInterface,MultiStateView.StateListener  {

    SaveData saveData;

    WithDrawPopUp withDrawPopUp;

    AlertDialog alertDialog;

    RecyclerView mRecyclerView;

    MultiStateView mMultiStateView;

    PullRefreshLayout swipeToRefresh;

    RedeemAdapter adapter;

    RedeemFragmentPresenter redeemFragmentPresenter;

    TimeByZone timeByZone;

    View v;

    public static RedeemFragment newInstance() {

        RedeemFragment fragment = new RedeemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveData = new SaveData(getContext());
        timeByZone = new TimeByZone();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.redeem_fragment, container, false);

        mMultiStateView = (MultiStateView) v.findViewById(R.id.multiStateView);

        withDrawPopUp = new WithDrawPopUp(getActivity(),alertDialog);

        redeemFragmentPresenter = new RedeemFragmentPresenter(getActivity(), this);

        redeemFragmentPresenter.getListRedeem();

        mMultiStateView.setStateListener(this);

        swipeToRefresh = (PullRefreshLayout) v.findViewById(R.id.swipeToRefresh);

        swipeToRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                redeemFragmentPresenter.getListRedeem();
            }
        });

        mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                        redeemFragmentPresenter.getListRedeem();
                    }
                });

        return v;
    }

    @Override
    public void showList(List<RedeemListModel> array) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mRecyclerView.setPadding(25, 25, 25, 25);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(itemAnimator);

        adapter = new RedeemAdapter(getActivity(),withDrawPopUp,array,timeByZone,saveData);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void viewStateError() {

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void viewStateLoading() {

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void viewState() {

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onStateChanged(int i) {

    }
}