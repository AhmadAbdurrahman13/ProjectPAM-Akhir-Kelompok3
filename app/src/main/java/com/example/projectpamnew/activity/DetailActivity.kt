package com.example.projectpamnew.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1762.Helper.ManagmentCart
import com.example.projectpamnew.Adapter.ColorAdapter
import com.example.projectpamnew.Adapter.SizeAdapter
import com.example.projectpamnew.Adapter.SliderAdapter
import com.example.projectpamnew.Model.ItemsModel
import com.example.projectpamnew.Model.SliderModel
import com.example.projectpamnew.R
import com.example.projectpamnew.databinding.ActivityDetailBinding
import com.example.projectpamnew.databinding.ViewholderColorBinding
import java.util.ResourceBundle.getBundle

class DetailActivity : BaseActivity() {
private lateinit var binding: ActivityDetailBinding
private lateinit var item:ItemsModel
private var numberOder=1
    private lateinit var managementCart:ManagmentCart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart=ManagmentCart(this)

        getBundle()
        banners()
        initList()
        }

    private fun initList() {
        val sizeList=ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size.toString())
        }

        binding.sizeList.adapter= SizeAdapter(sizeList)
        binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList=ArrayList<String>()
        for (imageUrl in item.picUrl){
            colorList.add(imageUrl)
        }

        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)

    }

    private fun banners() {
        val sliderItems=ArrayList<SliderModel>()
        for (imageUrl in item.picUrl){
            sliderItems.add(SliderModel(imageUrl))
        }
        binding.slider.adapter=SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding = false
        binding.slider.clipChildren = false
        binding.slider.offscreenPageLimit = 3
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        if (sliderItems.size>1){
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    private fun getBundle(){
        item=intent.getParcelableExtra("object")!!

        binding.titleTxt.text=item.title
        binding.descriptionTxt.text=item.description
        binding.priceText.text="Rp"+item.price
        binding.ratingTxt.text="${item.Rating} Rating"
        binding.addToCartBtn.setOnClickListener{
            item.numberInCart=numberOder
            managementCart.insertFood(item)
        }
        binding.bckBtn.setOnClickListener{finish()}
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }

    }
    }
