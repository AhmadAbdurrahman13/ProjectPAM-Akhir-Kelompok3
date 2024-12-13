package com.example.projectpamnew.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectpamnew.Model.BrandModel
import com.example.projectpamnew.Model.ItemsModel
import com.example.projectpamnew.Model.SliderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel():ViewModel() {
    private val firebaseDatabase=FirebaseDatabase.getInstance()

    private val _banner= MutableLiveData<List<SliderModel>>()
    private val _brand=MutableLiveData<MutableList<BrandModel>>()
    private val _popular=MutableLiveData<MutableList<ItemsModel>>()
    private val _userName = MutableLiveData<String>()


    val userName: LiveData<String> get() = _userName
    val brands:LiveData<MutableList<BrandModel>> =_brand
    val popular:LiveData<MutableList<ItemsModel>> =_popular
    val banner: LiveData<List<SliderModel>> = _banner

    fun loadUserName() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            FirebaseDatabase.getInstance().reference.child("users").child(it.uid)
                .get().addOnSuccessListener { snapshot ->
                    val name = snapshot.child("name").value?.toString() ?: "Guest"
                    _userName.postValue(name)
                }.addOnFailureListener {
                    _userName.postValue("Guest") // Fallback jika gagal
                }
        } ?: run {
            _userName.postValue("Guest") // Fallback jika tidak ada pengguna
        }
    }
    fun loadBanners(){
        val Ref=firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children){
                    val list=childSnapshot.getValue(SliderModel::class.java)
                    if (list!=null){
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }
            override fun onCancelled(error: DatabaseError) {
        }
    })
}
    fun loadBrands(){
        val Ref=firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<BrandModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(BrandModel::class.java)
                    if (list!=null){
                        lists.add(list)
                    }
                }
                _brand.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun loadPopular(){
        val Ref=firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list!=null){
                        lists.add(list)
                    }
                }
                _popular.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
