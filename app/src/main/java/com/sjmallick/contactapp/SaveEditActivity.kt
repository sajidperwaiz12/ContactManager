package com.sjmallick.contactapp

import android.app.Activity
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sjmallick.contactapp.databinding.ActivitySaveEditBinding
import com.sjmallick.contactapp.mvvmarch.SaveEditActivityViewModel
import com.sjmallick.contactapp.roomdb.entity.Contact

@Suppress("DEPRECATION")
class SaveEditActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySaveEditBinding.inflate(layoutInflater)
    }
    private var contact = Contact()
    private lateinit var viewModel: ViewModel
    private var flag = -1

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SaveEditActivityViewModel::class.java]

        if (intent.hasExtra("FLAG")) {
            flag = intent.getIntExtra("FLAG", -1)
            contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("DATA", Contact::class.java)!!
            } else {
                intent.getSerializableExtra("DATA") as Contact
            }

            binding.btnSave.text = getString(R.string.update_contact)

            val imageByte = contact.profile
            if (imageByte != null) {
                val image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
                binding.ivProfileImage.setImageBitmap(image)
            }

            binding.etEmail.editText?.setText(contact.email)
            binding.etName.editText?.setText(contact.name)
            binding.etPhoneNo.editText?.setText(contact.phoneNo)
        }

        binding.ivProfileImage.setOnLongClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.image_dialogue)

            val image = dialog.findViewById<ImageView>(R.id.ivProfileImageDialogue)
            val imageObject = binding.ivProfileImage.drawable
            image.setImageDrawable(imageObject)

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val lp = WindowManager.LayoutParams()
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.blurBehindRadius = 100
            lp.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            dialog.window?.attributes = lp

            dialog.show()

            true
        }

        binding.ivProfileImage.setOnClickListener {
            ImagePicker.with(this)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.btnSave.setOnClickListener {

            contact.name = binding.etName.editText?.text.toString()
            contact.phoneNo = binding.etPhoneNo.editText?.text.toString()
            contact.email = binding.etEmail.editText?.text.toString()

            if (flag == 1) {
                (viewModel as SaveEditActivityViewModel).updateData(contact) {
                     if (it != null && it > 0) {
                         Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                         finish()
                     }
                }
            } else {
                (viewModel as SaveEditActivityViewModel).storeData(contact) {
                    if (it != null && it > 0) {
                        Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    binding.ivProfileImage.setImageURI(fileUri)

                    val imageBytes = contentResolver.openInputStream(fileUri)?.readBytes()
                    contact.profile = imageBytes
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
}