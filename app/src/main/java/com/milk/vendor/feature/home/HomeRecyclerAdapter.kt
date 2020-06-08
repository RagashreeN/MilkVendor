package com.milk.vendor.feature.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.milk.vendor.common.model.ProductList
import com.milk.vendor.databinding.ListProductsBinding

class HomeRecyclerAdapter (var arrayList:ArrayList<ProductList>, var context: Context,var interfaceUpdateUI: UpdateUI) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolderRV(val listProductsBinding: ListProductsBinding) :
        RecyclerView.ViewHolder(listProductsBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val applicationBinding =
            ListProductsBinding.inflate(layoutInflater, parent, false)

        val rowActivityRecyclerViewVM: HomeProductRowVM = ViewModelProviders.of(parent.context as FragmentActivity)
            .get(HomeProductRowVM::class.java)

        applicationBinding.homeProductRowVM = rowActivityRecyclerViewVM

        applicationBinding.lifecycleOwner = parent.context as FragmentActivity

        return ViewHolderRV(applicationBinding)

    }

    override fun getItemCount(): Int {

        return arrayList.size

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val holder = holder as ViewHolderRV

        holder.listProductsBinding.tvItemName.text = arrayList[position].name
        holder.listProductsBinding.quantity.text = arrayList[position].quantity

        Glide.with(context) //1
            .load(arrayList[position].image)
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(holder.listProductsBinding.productImage)

        if(arrayList[position].count!=0) {
            var amount = arrayList[position].amount*arrayList[position].count
            holder.listProductsBinding.amount.text = "\u20B9 "+amount
            holder.listProductsBinding.quantityCount.text = "Quantity : "+arrayList[position].count.toString()
        }else{
            holder.listProductsBinding.amount.text = "\u20B9 "+arrayList[position].amount
        }
        holder.listProductsBinding.btnPlus.setOnClickListener(View.OnClickListener {
            var lastCount = arrayList[position].count
            lastCount++
            arrayList[position].count=lastCount
            interfaceUpdateUI.updateAmount(arrayList)
            notifyDataSetChanged()
        })
        holder.listProductsBinding.btnMinus.setOnClickListener(View.OnClickListener {
            var lastCount = arrayList[position].count
            if(lastCount!=0){
                lastCount--
                arrayList[position].count = lastCount
                interfaceUpdateUI.updateAmount(arrayList)
                notifyDataSetChanged()
            }
        })
    }
    public interface UpdateUI{
        public fun updateAmount(arrayList:ArrayList<ProductList>)
    }

}