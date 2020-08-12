package com.browsejoy.games.app.view.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.browsejoy.games.app.data.model.response.EarningList;
import com.browsejoy.games.app.oauth.Oauth;
import com.browsejoy.games.app.utils.Balance;
import com.browsejoy.games.app.utils.OnDataPass;
import com.browsejoy.games.app.utils.TimeByZone;
import com.kennyc.view.MultiStateView;
import java.util.List;

//Tab #2
public class HomeFragment extends Fragment implements HomeFragmentInterface, MultiStateView.StateListener {


    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    View view;

    HomeFragmentPresenter homeFragmentPresenter;

    MultiStateView mMultiStateView;

    CardView start_aerning;

    CardView logout;

    OnDataPass dataPasser;

    HomeFragmentAdapter adapter;

    RecyclerView mRecyclerView;

    PullRefreshLayout swipeToRefresh;

    TimeByZone timeByZone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, null);


     //  Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity()));

        homeFragmentPresenter = new HomeFragmentPresenter(getActivity(),this);

        mMultiStateView = (MultiStateView) view.findViewById(R.id.multiStateView);
        start_aerning  = (CardView)view.findViewById(R.id.start_earning);
        logout = (CardView)view.findViewById(R.id.logout);

        timeByZone = new TimeByZone();

        getProfileBalance();

        mMultiStateView.setStateListener(this);

        mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                        homeFragmentPresenter.getUserProfile();
                    }
                });


        swipeToRefresh = (PullRefreshLayout) view.findViewById(R.id.swipeToRefresh);

        swipeToRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeFragmentPresenter.getUserProfile();
            }
        });

        return view;
    }

    @Override
    public void redirectUsertoHome() {

        Intent i = new Intent(getActivity(), Oauth.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0,0);
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
    public void setBalanceAndList(String balance, List<EarningList> array) {

        String myBalance = String.format("$%.4f",Float.parseFloat(balance));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        mRecyclerView.setPadding(25, 25, 25, 25);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(itemAnimator);

        adapter = new HomeFragmentAdapter(this,getActivity(),array,timeByZone,myBalance);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    //    mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void logout() {

        homeFragmentPresenter.logout();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        dataPasser = (OnDataPass) context;

    }

    public void setBalance(Double balance) {

        dataPasser.onDataPass(balance + "");
    }

    @Override
    public void onStateChanged(int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getProfileBalance();
    }

    private  void  getProfileBalance(){

        homeFragmentPresenter.getUserProfile();
        homeFragmentPresenter.setBalance();
    }

}