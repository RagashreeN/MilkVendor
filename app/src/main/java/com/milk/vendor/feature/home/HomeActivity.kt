package com.milk.vendor.feature.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.milk.vendor.R
import com.milk.vendor.common.model.ProductList
import com.milk.vendor.databinding.ActivityHomeBinding
import com.milk.vendor.feature.cart.CartActivity
import kotlinx.android.synthetic.main.activity_home.view.*



class HomeActivity : AppCompatActivity() ,HomeRecyclerAdapter.UpdateUI{

    lateinit var databinding : ActivityHomeBinding
    lateinit var viewModelHome : HomeViewModel

    lateinit var productInner : ProductList

    lateinit var mFirebaseDatabase : DatabaseReference
    lateinit var mFirebaseInstance : FirebaseDatabase

    lateinit var recyclerAdapter: HomeRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initVar()
    }

    companion object{
        var arraylistProduct : ArrayList<ProductList> = ArrayList<ProductList>()
        var totalAmount : Int = 0
    }

    private fun initView(){
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        viewModelHome = ViewModelProvider(this).get(HomeViewModel::class.java)
        databinding.userHomeVM = viewModelHome
        databinding.lifecycleOwner = this

        databinding.root.progressbar_login.visibility = View.VISIBLE

        databinding.root.cart_id.setOnClickListener(View.OnClickListener {

            var totalCart : Int=0
            arraylistProduct.forEach{
                if(it.count>0){
                    totalCart++
                }
            }

            if(totalCart>0) {
                val gson = Gson()
                val type = object : TypeToken<List<ProductList>>() {

                }.getType()
                val json = gson.toJson(arraylistProduct, type)

                val intent = Intent(this,CartActivity::class.java)
                intent.putExtra("productList",json)
                startActivityForResult(intent,111)
            }else{
                Toast.makeText(this,"Your Cart is empty",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initVar(){
        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference()
        //lateinit var productList : ProductList
        var productList = ProductList(
            1,
            "Milk",
            "30ml",
            "https://www.gstatic.com/webp/gallery3/1.png",
            20,
            0
        )
        mFirebaseDatabase.child("ProductList").child("1").setValue(productList)

        productList = ProductList(
            2,
            "Milk",
            "1 litre",
            "https://www.gstatic.com/webp/gallery3/2.png",
            50
        )
        mFirebaseDatabase.child("ProductList").child("2").setValue(productList)

        productList = ProductList(
            3,
            "Curd",
            "30ml",
            "https://www.gstatic.com/webp/gallery3/1.png",
            30
        )
        mFirebaseDatabase.child("ProductList").child("3").setValue(productList)

        productList = ProductList(
            4,
            "Curd",
            "1 litre",
            "https://www.gstatic.com/webp/gallery3/2.png",
            60
        )
        mFirebaseDatabase.child("ProductList").child("4").setValue(productList)

        productList = ProductList(
            5,
            "Buttermilk",
            "1 litre",
            "https://www.gstatic.com/webp/gallery3/1.png",
            15
        )
        mFirebaseDatabase.child("ProductList").child("5").setValue(productList)

        productList = ProductList(
            6,
            "Buttermilk",
            "30ml",
            "https://www.gstatic.com/webp/gallery3/2.png",
            30
        )
        mFirebaseDatabase.child("ProductList").child("6").setValue(productList)

        productList = ProductList(
            7,
            "Butter",
            "30ml",
            "https://www.gstatic.com/webp/gallery3/1.png",
            40
        )
        mFirebaseDatabase.child("ProductList").child("7").setValue(productList)

        productList = ProductList(
            8,
            "Butter",
            "1 litre",
            "https://www.gstatic.com/webp/gallery3/2.png",
            80
        )
        mFirebaseDatabase.child("ProductList").child("8").setValue(productList)

        productList = ProductList(
            9,
            "Cheese",
            "30ml",
            "https://www.gstatic.com/webp/gallery3/1.png",
            50
        )
        mFirebaseDatabase.child("ProductList").child("9").setValue(productList)

        productList = ProductList(
            10,
            "Cheese",
            "1 litre",
            "https://www.gstatic.com/webp/gallery3/2.png",
            100
        )
        mFirebaseDatabase.child("ProductList").child("10").setValue(productList)


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("ProductList")


        mFirebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    // TODO: handle the post
                    productInner = postSnapshot.getValue(ProductList::class.java)!!
                    arraylistProduct.add(productInner)
                }
                databinding.root.progressbar_login.visibility = View.GONE
                databinding.root.layout_payment.visibility=View.VISIBLE
                displayList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun displayList(){
        if(arraylistProduct.size>0) {
            recyclerAdapter = HomeRecyclerAdapter(arraylistProduct, this,this)
            var linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            databinding.root.recycle_milklist.layoutManager = linearLayoutManager
            databinding.root.recycle_milklist.adapter = recyclerAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            111 -> {arraylistProduct.forEach{
                var messageStatus = data?.getStringExtra("Status")
                if(messageStatus.equals("Success")) {
                    Toast.makeText(this, "Payment done Successfully", Toast.LENGTH_LONG).show()
                    it.count = 0
                    displayList()
                    databinding.root.pay_amount.text = "\u20B9 "+0
                }
            }}
        }
    }

    override fun updateAmount(arrayList: ArrayList<ProductList>) {
        //arraylistCart.clear()
        //arraylistCart=arrayList
        totalAmount=0
        arrayList.forEach{
            totalAmount= totalAmount+(it.amount*it.count)
        }
        databinding.root.pay_amount.text = /*"\u20B9 "+totalAmount.toString()*/ "Amount : "+totalAmount+"\u20B9"
    }
}
