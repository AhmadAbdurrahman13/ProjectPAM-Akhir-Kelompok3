package com.example.projectpamnew.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projectpamnew.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val user = firebaseAuth.currentUser

        loadUserProfile(user)

        binding.btnSave.setOnClickListener {
            saveProfile(user)
        }

        binding.profileImage.setOnClickListener {
            pickImageFromGallery()
        }
        initBottomMenu()
    }

    private fun initBottomMenu() {
        binding.homeBtn.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, CartActivity::class.java))
        }
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java))
        }
    }

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                binding.profileImage.setImageURI(imageUri)
            }
        }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageResult.launch(intent)
    }

    private fun loadUserProfile(user: FirebaseUser?) {
        user?.let {
            firebaseDatabase.reference.child("users").child(it.uid).get().addOnSuccessListener { snapshot ->
                binding.nameInput.setText(snapshot.child("name").value.toString())
                binding.emailInput.setText(it.email) // Tetap gunakan dari FirebaseUser
                binding.phoneInput.setText(snapshot.child("phone").value.toString())

                val profileImageUrl = snapshot.child("profileImageUrl").value.toString()
                Glide.with(this).load(profileImageUrl).into(binding.profileImage)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load user data.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveProfile(user: FirebaseUser?) {
        val name = binding.nameInput.text.toString()
        val phone = binding.phoneInput.text.toString()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = user?.uid ?: return

        if (imageUri != null) {
            val storageRef = firebaseStorage.reference.child("profile_images/$uid/${UUID.randomUUID()}.jpg")
            storageRef.putFile(imageUri!!).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateDatabaseWithProfile(uri.toString(), name, phone, uid)
                }
            }
        } else {
            updateDatabaseWithProfile(null, name, phone, uid)
        }
    }

    private fun updateDatabaseWithProfile(imageUrl: String?, name: String, phone: String, uid: String) {
        val updates = hashMapOf<String, Any>(
            "name" to name, // Pastikan "name" sesuai dengan key di Firebase
            "phone" to phone
        )
        imageUrl?.let { updates["profileImageUrl"] = it }

        firebaseDatabase.reference.child("users").child(uid).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
            }
    }

}
