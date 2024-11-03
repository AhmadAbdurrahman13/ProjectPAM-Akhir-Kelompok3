package com.example.projectpamnew.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1762.Helper.ChangeNumberItemsListener
import com.example.project1762.Helper.ManagmentCart
import com.example.projectpamnew.Adapter.CartAdapter
import com.example.projectpamnew.R
import com.example.projectpamnew.databinding.ActivityCartBinding

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart=ManagmentCart(this)

        setVariable()
        initCartlist()
        calculateCart()
    }

    private fun initCartlist() {
        val cartItems = managmentCart.getListCart()
        Log.d("CartActivity", "Cart items loaded: ${cartItems.size}")
        binding.viewCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter = CartAdapter(cartItems, this, object : ChangeNumberItemsListener {
            override fun onChanged() {
                calculateCart()
            }
        })

        with(binding) {
            emptyTxt.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility = if (cartItems.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun calculateCart(){
        val percentTax=0.02
        val delivery=10.0
        tax=Math.round((managmentCart.getTotalFee()*percentTax)*100)/100/.0
        val total=Math.round((managmentCart.getTotalFee()+tax+delivery)*100)/100
        val itemTotal=Math.round(managmentCart.getTotalFee()*100)/100

        with(binding){
            totalFeeTxt.text="$$itemTotal"
            taxTxt.text="$$tax"
            deliveryTxt.text="$$delivery"
            TotalTxt.text="$$total"
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener{finish()}
    }
}