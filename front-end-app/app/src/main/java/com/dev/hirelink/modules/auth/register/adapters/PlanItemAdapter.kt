package com.dev.hirelink.modules.auth.register.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.hirelink.R
import com.dev.hirelink.models.Plan
import com.google.android.material.card.MaterialCardView

class PlanItemAdapter(
    private val context: Context,
    private val dataset: List<Plan>
) : Adapter<PlanItemAdapter.ItemViewHolder>() {

    private var currentSelectedItemPosition: Int = 0;

    class ItemViewHolder(private val view: View) : ViewHolder(view) {
        private val planTitle: TextView = view.findViewById(R.id.text_view_plan_title)
        private val planPrice: TextView = view.findViewById(R.id.text_view_plan_price)

        fun bind(plan: Plan) {
            planTitle.text = plan.name
            planPrice.text = view.context.getString(R.string.cash_str, plan.price)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val adapterLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.plan_grid_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataset[position])
        val cardPlan: MaterialCardView = holder.itemView.findViewById(R.id.card_plan)

        if (position == currentSelectedItemPosition) {
            cardPlan.strokeWidth = 5
            cardPlan.strokeColor = context.getColor(R.color.indigo_main)
        } else {
            cardPlan.strokeWidth = 1
            cardPlan.strokeColor = context.getColor(R.color.stroke_color)
        }

        holder.itemView.setOnClickListener {
            val previousSelectedItemPosition = currentSelectedItemPosition
            currentSelectedItemPosition = position

            notifyItemChanged(previousSelectedItemPosition)
            notifyItemChanged(currentSelectedItemPosition)
        }
    }

    override fun getItemCount() = dataset.size
}