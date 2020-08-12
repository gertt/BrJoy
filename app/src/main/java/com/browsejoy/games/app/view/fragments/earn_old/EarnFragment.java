package com.browsejoy.games.app.view.fragments.earn_old;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.networks.AdGemProvider;
import com.browsejoy.games.app.view.activities.tv.TvBetaActivity;
import com.browsejoy.games.app.networks.AdscendProvider;
import com.browsejoy.games.R;
import com.kennyc.view.MultiStateView;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gerti on 5/25/2018.
 */

//Tab #1
public class EarnFragment extends Fragment implements EarnFragmentView, MultiStateView.StateListener {

    public static EarnFragment newInstance() {

        EarnFragment fragment = new EarnFragment();
        return fragment;

    }

    View view;

    EarnFragmentPresenter fragmentTabOnePresenter;

    ExpandableListAdapter listAdapter;

    ExpandableListView expListView;

    MultiStateView mMultiStateView;

    ConstraintLayout earn_unlimited;

    CardView videos;

    ConstraintLayout live_games;

    ConstraintLayout more_offers;

    String SubId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.earn_fragment, container, false);
        // expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        //Force restart the app if it crashes
       // Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity()));

        mMultiStateView = (MultiStateView) view.findViewById(R.id.multiStateView);

        earn_unlimited = (ConstraintLayout) view.findViewById(R.id.card_earn_unlimited);
        videos = (CardView) view.findViewById(R.id.card_video);

        more_offers = (ConstraintLayout) view.findViewById(R.id.card_more_offers);

        earn_unlimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "1", Toast.LENGTH_LONG).show();
            }
        });

        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TvBetaActivity.class);
                startActivity(intent);
            }
        });

        more_offers.setOnClickListener(new View.OnClickListener() {

            public void startAdscend() {

                AdscendProvider provider = new AdscendProvider();
                SaveData saveData = new SaveData(getContext());
                String userId = saveData.getStoredValue("authId");
                provider.startAdscendOfferWall(getContext(), userId);
            }

            public void startAdGem() {
                Intent intent = new Intent(getActivity(), AdGemProvider.class);
                startActivity(intent);
            }

            @Override
            public void onClick(View v) {
                //startAdscend();
               startAdGem();
            }
        });

        fragmentTabOnePresenter = new EarnFragmentPresenter(this, getActivity());
        fragmentTabOnePresenter.getCategoryAndSubCategory();

        return view;
    }

    @Override
    public void getCategoryAndSubCategory(List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
         listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
         expListView.setAdapter(listAdapter);
    }

    @Override
    public void viewStateLoading() {
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void viewStateError() {
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void viewState() {
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }


    @Override
    public void onStateChanged(int i) {

    }
}
