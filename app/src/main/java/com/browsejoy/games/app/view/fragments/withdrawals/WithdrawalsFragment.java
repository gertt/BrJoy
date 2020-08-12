package com.browsejoy.games.app.view.fragments.withdrawals;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.kennyc.view.MultiStateView;

import java.util.List;

/**
 * Created by Gerti on 12/22/2018.
 */

public class WithdrawalsFragment extends Fragment implements WithdrawalsFragmentInterface, MultiStateView.StateListener {

    public static WithdrawalsFragment newInstance() {
        WithdrawalsFragment fragment = new WithdrawalsFragment();
        return fragment;
    }

    View view;
    WithdrawalsFragmentPresenter withDrawFragmentPresenter;

    MultiStateView mMultiStateView;
    PullRefreshLayout swipeToRefresh;

    WithdrawalsAdapter adapter;
    RecyclerView mRecyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_withdraw, null);


        mMultiStateView = (MultiStateView) view.findViewById(R.id.multiStateView);

        withDrawFragmentPresenter = new WithdrawalsFragmentPresenter(getActivity(), this);
        withDrawFragmentPresenter.getListWithDraw();

        swipeToRefresh = (PullRefreshLayout) view.findViewById(R.id.swipeToRefresh);

        swipeToRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                withDrawFragmentPresenter.getListWithDraw();
            }
        });


        mMultiStateView.setStateListener(this);
        mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                        withDrawFragmentPresenter.getListWithDraw();
                    }
                });

        return view;
    }

    @Override
    public void showList(List<RedeemListModel> feedItemList) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //lejo 2scrolle, jasht dhe n recycleView
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mRecyclerView.setPadding(25, 25, 25, 25);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(itemAnimator);

        adapter = new WithdrawalsAdapter(feedItemList);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //line seperator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void viewStateLoading() {
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void viewStateError() {
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        swipeToRefresh.setRefreshing(false);
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
