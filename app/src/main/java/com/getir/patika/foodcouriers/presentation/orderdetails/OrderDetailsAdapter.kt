package com.getir.patika.foodcouriers.presentation.orderdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.getir.patika.chatapp.data.model.OrderItem
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.databinding.ItemFoodOrderBinding


class OrderDetailsAdapter(private val events: (OrderDetailsEvent) -> Unit) : ListAdapter<OrderItem, RecyclerView.ViewHolder>(
    ItemDiff
) {
    private val asyncListDiffer = AsyncListDiffer(this, ItemDiff)

    fun saveData(orderItems: List<OrderItem>) {
        asyncListDiffer.submitList(orderItems)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodOrderBinding.inflate(inflater, parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderDetailViewHolder -> {
                holder.bind(asyncListDiffer.currentList[position])
            }
        }
    }

    inner class OrderDetailViewHolder(private val binding: ItemFoodOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItem) {
            binding.apply {
                tvFoodPrice.text = binding.root.context.getString(R.string.price, orderItem.price)
                tvCount.text = orderItem.quantity.toString()
                tvFoodTitle.text = orderItem.name
                tvFoodSubHeader.text = orderItem.place

                btnPlus.setOnClickListener {
                    events(OrderDetailsEvent.OnPlusClick(orderItem.id))
                }
                btnMinus.setOnClickListener {
                    events(OrderDetailsEvent.OnMinusClick(orderItem.id))
                }
            }
        }
    }

    companion object {
        val ItemDiff = object : DiffUtil.ItemCallback<OrderItem>() {
            override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}