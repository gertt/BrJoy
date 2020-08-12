package com.browsejoy.games.app.view.fragments.withdrawals;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.model.response.RedeemListModel;
import java.util.List;

/**
 * Created by Gerti on 12/22/2018.
 */

public class WithdrawalsAdapter extends RecyclerView.Adapter<WithdrawalsAdapter.ViewHolder>   {

    private List<RedeemListModel> feedItemList;

    public WithdrawalsAdapter(List<RedeemListModel> feedItemList) {
        this.feedItemList = feedItemList;
    }

    @Override
    public WithdrawalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.adapter_home_item, parent, false);

        return new WithdrawalsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WithdrawalsAdapter.ViewHolder holder, int position) {

        final RedeemListModel feedItem = feedItemList.get(position);

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}





