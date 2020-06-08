package com.milk.vendor.feature.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.milk.vendor.R
import com.milk.vendor.common.model.ProductList
import com.milk.vendor.databinding.ActivityCartBinding
import kotlinx.android.synthetic.main.activity_cart.view.*
import java.nio.file.Files.size
import com.google.gson.reflect.TypeToken
import android.R.attr.data
import android.content.Intent
import android.view.View
import com.google.gson.Gson


class CartActivity : AppCompatActivity(),CartRecyclerAdapter.UpdateUI{

    lateinit var databinding : ActivityCartBinding
    lateinit var cartVM: CartVM
    lateinit var recyclerAdapter: CartRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initVar()
    }

    companion object{
        var arraylistCart : ArrayList<ProductList> = ArrayList<ProductList>()
        var arraylistCartProduct : ArrayList<ProductList> = ArrayList<ProductList>()
        var totalAmount : Int = 0
    }

    private fun initView(){
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        cartVM = ViewModelProvider(this).get(CartVM::class.java)
        databinding.cartVM = cartVM
        databinding.lifecycleOwner = this

        val gson = Gson()
        val stringLocation = intent.getStringExtra("productList")
        if (stringLocation != null) {
            val type = object : TypeToken<List<ProductList>>() {
            }.type
            arraylistCartProduct = gson.fromJson(stringLocation, type)
            arraylistCart.clear()
            arraylistCartProduct.forEach{
                if(it.count>0) {
                    arraylistCart.add(it)
                }
            }
            updateAmount(arraylistCart)
        }

        databinding.root.layout_payment.setOnClickListener(View.OnClickListener {
            var intent : Intent = Intent()
            intent.putExtra("Status","Success")
            setResult(111, intent)
            finish()
        })
    }

    private fun initVar(){
        if(arraylistCart.size>0) {
            recyclerAdapter = CartRecyclerAdapter(arraylistCart, this,this)
            var linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            databinding.root.recycle_cartlist.layoutManager = linearLayoutManager
            databinding.root.recycle_cartlist.adapter = recyclerAdapter
        }
    }

    override fun updateAmount(arrayList: ArrayList<ProductList>) {
        //arraylistCart.clear()
        //arraylistCart=arrayList
        totalAmount=0
        arrayList.forEach{
            totalAmount= totalAmount+(it.amount*it.count)
        }
        databinding.root.pay_amount.text = "\u20B9 "+totalAmount.toString()
    }
}