package com.example.projectpamnew.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.projectpamnew.Adapter.BrandAdapter
import com.example.projectpamnew.Adapter.PopularAdapter
import com.example.projectpamnew.Adapter.SliderAdapter
import com.example.projectpamnew.databinding.ActivityMainBinding
import com.example.projectpamnew.Model.SliderModel
import com.example.projectpamnew.ViewModel.MainViewModel


class MainActivity : BaseActivity() {
    private val viewModel=MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initBrand()
        initPopular()
        initBottomMenu()
        initUserName()
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, CartActivity::class.java))
        }
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }
    }

    private fun initUserName() {
        viewModel.userName.observe(this, Observer { name ->
            binding.textView4.text = name // Tampilkan nama pada TextView
        })
        viewModel.loadUserName()
    }



    private fun initBanner(){
        binding.progressBarBanner.visibility= View.VISIBLE
        viewModel.banner.observe(this, Observer { items->
            banners(items)
            binding.progressBarBanner.visibility=View.GONE
        })
        viewModel.loadBanners()
    }
    private fun banners(images:List<SliderModel>){
        binding.viewPagerSlider.adapter= SliderAdapter(images,binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding=false
        binding.viewPagerSlider.clipChildren=false
        binding.viewPagerSlider.offscreenPageLimit=3
        binding.viewPagerSlider.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer=CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }

        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)
        if(images.size>1){
            binding.dotIndicator.visibility=View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPagerSlider)
        }
    }

    private fun initBrand(){
        binding.progressBarBrand.visibility=View.VISIBLE
        viewModel.brands.observe(this, Observer {
            binding.viewBrand.layoutManager=LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter=BrandAdapter(it)
            binding.progressBarBrand.visibility=View.GONE
        })
        viewModel.loadBrands()
    }
    private fun initPopular(){
        binding.progressBarPopular.visibility=View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.viewPopular.layoutManager=GridLayoutManager(this@MainActivity,2)
            binding.viewPopular.adapter=PopularAdapter(it)
            binding.progressBarPopular.visibility=View.GONE
        })
        viewModel.loadPopular()
    }
}
