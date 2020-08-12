package com.browsejoy.games.app.view.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.browsejoy.games.app.view.activities.main.MainActivity;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.model.response.EarningList;
import com.browsejoy.games.app.utils.TimeByZone;
import java.util.List;

/**
 * Created by Gerti on 1/28/2019.
 */

public class HomeFragmentAdapter extends RecyclerView.Adapter   {

    private List<EarningList> feedItemList;

    TimeByZone timeByZone;

    HomeFragment homeFragment;

    String mybalance;

    Context context;

    private static final int HOME_HEADER = 0;

    private static final int HOME_ITEM = 2;

    public HomeFragmentAdapter(HomeFragment homeFragment, Context context, List<EarningList> feedItemList, TimeByZone timeByZone, String myBalance) {
        this.feedItemList = feedItemList;
        this.timeByZone = timeByZone;
        this.mybalance = myBalance;
        this.context =  context;
        this.homeFragment =  homeFragment;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
        if (viewType == HOME_HEADER) {
            row = inflater.inflate(R.layout.adapter_home_header, parent, false);
            return new HomeFragmentAdapter.HomeHeader(row);

        } else if (viewType == HOME_ITEM) {
            row = inflater.inflate(R.layout.adapter_home_item, parent, false);
            return new HomeFragmentAdapter.HomeItem(row);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        EarningList recyclerViewItem = feedItemList.get(position);
        if (holder instanceof HomeFragmentAdapter.HomeHeader) {
            HomeFragmentAdapter.HomeHeader redeemHeader = (HomeFragmentAdapter.HomeHeader) holder;
            EarningList header = (EarningList) recyclerViewItem;
            //set data
               redeemHeader.txtBalance.setText(mybalance);

               redeemHeader.start_earning.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       /* In creation activity that wants to launch a tabbed activity */
                       Intent intent = new Intent(context, MainActivity.class);
                       intent.putExtra(MainActivity.SELECTED_TAB_EXTRA_KEY, MainActivity.EARNINGS_TAB);
                       context.startActivity(intent);
//                       Intent intent = new Intent(context, TvBetaActivity.class);
//                       context.startActivity(intent);

                   }
               });

               redeemHeader.btnLogout.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       homeFragment.logout();
                   }
               });

        }  else if (holder instanceof HomeFragmentAdapter.HomeItem) {
            HomeFragmentAdapter.HomeItem redeemItem = (HomeFragmentAdapter.HomeItem) holder;
            EarningList feedItem = (EarningList) recyclerViewItem;
            //set data

            if(feedItem.getTable().getName() != null && feedItem.getTable().getName() !="") {

             ((HomeFragmentAdapter.HomeItem) holder).textViewAmount.setText(feedItem.getTable().getRate().toString());

            }

            if (feedItem.getTable().getName() != null && feedItem.getTable().getName() !=""){

               ((HomeFragmentAdapter.HomeItem) holder).textViewName.setText(feedItem.getTable().getName());

            }

            if(feedItem.getTable().getCreatedAt() != null && feedItem.getTable().getCreatedAt() !=""){

               ((HomeFragmentAdapter.HomeItem) holder).textViewCreated.setText(feedItem.getTable().getCreatedAt());

            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);

    }

    private class HomeItem extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewCreated;
        TextView textViewAmount;

        public HomeItem(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView19);
            textViewCreated = itemView.findViewById(R.id.textView30);  
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
        }
    }

    //header holder
    private class HomeHeader extends RecyclerView.ViewHolder {

        TextView txtBalance;
        TextView  start_earning;
        TextView btnLogout;

        HomeHeader(View itemView) {
            super(itemView);

            txtBalance = itemView.findViewById(R.id.txtBalance);
            start_earning = (TextView) itemView.findViewById(R.id.textView12);
            btnLogout = (TextView) itemView.findViewById(R.id.btnLogout);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position==0){

            return HOME_HEADER;
        }
        else  if (position>0){

            return HOME_ITEM;
        }

        else
            return super.getItemViewType(position);
    }
}


