package com.browsejoy.games.app.view.fragments.redeem;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.browsejoy.games.R;
import com.browsejoy.games.app.data.model.response.RedeemListModel;
import com.browsejoy.games.app.data.prefs.SaveData;
import com.browsejoy.games.app.utils.RecyclerViewItem;
import com.browsejoy.games.app.utils.TimeByZone;
import com.browsejoy.games.app.view.dialogs.WithDrawPopUp;
import static com.browsejoy.games.app.utils.Constants.DOLLAR_SIGN;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Gerti on 12/27/2018.
 */

public class RedeemAdapter extends RecyclerView.Adapter   {

    private List<RedeemListModel> feedItemList;

    TimeByZone timeByZone;

    SaveData saveData;

    WithDrawPopUp withDrawPopUp;

    FragmentActivity activity;

    private static final int REDEEM_HEADER = 0;
    public final double MINIMUM_DOLLARS = 2.5;

    private static final int REDEEM_ITEM = 2;

    public RedeemAdapter(FragmentActivity activity,WithDrawPopUp withDrawPopUp,List<RedeemListModel> feedItemList, TimeByZone timeByZone,SaveData saveData) {

        this.feedItemList = feedItemList;
        this.timeByZone =  timeByZone;
        this.saveData =  saveData;
        this.withDrawPopUp = withDrawPopUp;
        this.activity = activity;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
        if (viewType == REDEEM_HEADER) {
            row = inflater.inflate(R.layout.adapter_redeem_header, parent, false);
            return new RedeemHeader(row);

        } else if (viewType == REDEEM_ITEM) {
            row = inflater.inflate(R.layout.adapter_redeem_item, parent, false);
            return new RedeemItem(row);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        RecyclerViewItem recyclerViewItem = feedItemList.get(position);

        if (holder instanceof RedeemHeader) {
            RedeemHeader redeemHeader = (RedeemHeader) holder;
            RedeemListModel header = (RedeemListModel) recyclerViewItem;

            if (Double.parseDouble(saveData.getBalance())>MINIMUM_DOLLARS){
                BigDecimal value = new BigDecimal(saveData.getBalance());
                value = value.setScale(2, BigDecimal.ROUND_FLOOR);

                String msg = DOLLAR_SIGN+String.format("%.2f", value) +" available to withdraw.";

                redeemHeader.txtPoints.setText(msg);

            } else {

                redeemHeader.bt_withdraw.setVisibility(View.INVISIBLE);
            }

            redeemHeader.bt_withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    withDrawPopUp.showMaterialDialogService(activity);

                }
            });

        }  else if (holder instanceof RedeemItem) {
            RedeemListModel feedItem = (RedeemListModel) recyclerViewItem;
            //set data

            if(feedItem.getAmount() != null) {
               ((RedeemItem) holder).textViewAmount.setText(feedItem.getAmount().toString());
            }

            if (feedItem.getRequestedAt()!=null){
                ((RedeemItem) holder).textViewRequestedAt.setText(timeByZone.timeZone(feedItem.getRequestedAt()));

            }

            if(feedItem.getPaidAt() != null){
                ((RedeemItem) holder).textViewPaidAt.setText(timeByZone.timeZone(feedItem.getPaidAt()));

            }

            if(feedItem.getEmail() != null){
               ((RedeemItem) holder).textViewEmail.setText(feedItem.getEmail());
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);

    }

    private class RedeemItem extends RecyclerView.ViewHolder {

        TextView textViewAmount;
        TextView textViewEmail;
        TextView textViewRequestedAt;
        TextView textViewPaidAt;

        public RedeemItem(View itemView) {
            super(itemView);

            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewRequestedAt = itemView.findViewById(R.id.textViewRequestedAt);
            textViewPaidAt = itemView.findViewById(R.id.textViewPaidAt);
        }
    }

    //header holder
    private class RedeemHeader extends RecyclerView.ViewHolder {

        TextView txtPoints;
        TextView  bt_withdraw;

        RedeemHeader(View itemView) {
            super(itemView);
            txtPoints = itemView.findViewById(R.id.txtPoints);
            bt_withdraw = (TextView) itemView.findViewById(R.id.bt_withdraw);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position==0){

            return REDEEM_HEADER;
        }
        else  if (position>0){

            return REDEEM_ITEM;
        }
        else
            return super.getItemViewType(position);
    }
}







