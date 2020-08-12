package com.browsejoy.games.app.view.fragments.earn

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.browsejoy.games.R
import com.browsejoy.games.app.data.model.response.EarnDataModel
import com.browsejoy.games.app.data.prefs.SaveData
import com.browsejoy.games.app.networks.AdscendProvider
import com.browsejoy.games.app.view.activities.tv.TvBetaActivity


class EarnsFragmentAdapter(private val activity: Context, private val listViewType: Array<EarnDataModel>): RecyclerView.Adapter<EarnsFragmentAdapter.ViewHolder>() {

    object EarnViewTypes {
        val HEADER = 0
        val LISTITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            EarnViewTypes.HEADER -> ViewHolderHeader(inflater.inflate(R.layout.adapter_earn_header, parent,false))
            else -> ViewHolderItem(inflater.inflate(R.layout.adapter_earn_item, parent,false))
        }
    }

    private fun setupViewHolderFromEarnModel(viewHolderItem: ViewHolderItem, recyclerViewItem :EarnDataModel)  {
        viewHolderItem.tittle.text = recyclerViewItem.earnTittle
        viewHolderItem.point.text = recyclerViewItem.point
        viewHolderItem.earnText.text = recyclerViewItem.earnTittle
        viewHolderItem.cardEarn.setOnClickListener(recyclerViewItem.onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder) {
            is ViewHolderHeader-> null;
            is ViewHolderItem -> {
                val viewHolderItem = holder as ViewHolderItem
                setupViewHolderFromEarnModel(
                        viewHolderItem,
                    listViewType.get(position)
                )
            }
        }
    }

    override fun getItemCount(): Int = listViewType.size

    override fun getItemViewType(position: Int): Int {
       return when(position) {
           0 -> EarnViewTypes.HEADER;
           else -> EarnViewTypes.LISTITEM;
       }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderHeader(itemView: View) : ViewHolder(itemView) {

    }

    inner class ViewHolderItem(itemView: View) : ViewHolder(itemView){

        val tittle: TextView = itemView.findViewById(R.id.txtTittle)
        val cardEarn: ConstraintLayout = itemView.findViewById(R.id.card_earn_unlimited)
        val point: TextView = itemView.findViewById(R.id.txtPoint)
        val earnText: TextView = itemView.findViewById(R.id.txtEarn)
    }

    object Utils {
        fun startNewActivity(context: Context, clazz: Class<*>) {
            val intent = Intent(context, clazz)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}

