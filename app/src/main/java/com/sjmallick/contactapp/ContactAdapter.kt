package com.sjmallick.contactapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.sjmallick.contactapp.databinding.ItemContactCompactBinding
import com.sjmallick.contactapp.databinding.ItemContactExpandedBinding
import com.sjmallick.contactapp.roomdb.entity.Contact

class ContactAdapter(private val context: Context, private val contactArrayList: ArrayList<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_COMPACT = 0
    private val VIEW_TYPE_EXPANDED = 1
    private val expandedPositions = HashSet<Int>()

    private lateinit var myListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onCallButtonClick(position: Int)
    }

    fun setOnItemClickListener(myListener: OnItemClickListener) {
        this.myListener = myListener
    }

    inner class CompactViewHolder(val binding: ItemContactCompactBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ExpandedViewHolder(val binding: ItemContactExpandedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COMPACT -> {
                val binding = ItemContactCompactBinding.inflate(LayoutInflater.from(context), parent, false)
                CompactViewHolder(binding)
            }
            VIEW_TYPE_EXPANDED -> {
                val binding = ItemContactExpandedBinding.inflate(LayoutInflater.from(context), parent, false)
                ExpandedViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return contactArrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentContact = contactArrayList[position]

        val splitName = currentContact.name?.split(" ")
        var sign = ""
        splitName?.forEach {
            if (it.isNotEmpty()) {
                sign += it[0]
            }
        }


        when (holder.itemViewType) {
            VIEW_TYPE_COMPACT -> {
                val compactHolder = holder as CompactViewHolder

                if (currentContact.profile != null) {
                    val imageByte = currentContact.profile

                    if (imageByte != null) {
                        val image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
                        compactHolder.binding.ivProfileImage.setImageBitmap(image)
                        compactHolder.binding.ivProfileImage.visibility = View.VISIBLE
                        compactHolder.binding.tvProfileName.visibility = View.GONE
                    } else {
                        compactHolder.binding.tvProfileName.text = sign
                        compactHolder.binding.ivProfileImage.visibility = View.GONE
                        compactHolder.binding.tvProfileName.visibility = View.VISIBLE
                    }
                } else {
                    compactHolder.binding.tvProfileName.text = sign
                    compactHolder.binding.ivProfileImage.visibility = View.GONE
                    compactHolder.binding.tvProfileName.visibility = View.VISIBLE
                }
                compactHolder.binding.tvName.text = currentContact.name
                compactHolder.binding.expandButton.setOnClickListener {
                    // Toggle the expanded state of the item
                    toggleItemExpansion(position)
                }

                compactHolder.binding.ivCall.setOnClickListener {
                    myListener.onCallButtonClick(position)
                }
            }
            VIEW_TYPE_EXPANDED -> {
                val expandedHolder = holder as ExpandedViewHolder

                if (currentContact.profile != null) {
                    val imageByte = currentContact.profile

                    if (imageByte != null) {
                        val image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
                        expandedHolder.binding.ivProfileImage.setImageBitmap(image)
                        expandedHolder.binding.ivProfileImage.visibility = View.VISIBLE
                        expandedHolder.binding.tvProfileName.visibility = View.GONE
                    } else {
                        expandedHolder.binding.tvProfileName.text = sign
                        expandedHolder.binding.ivProfileImage.visibility = View.GONE
                        expandedHolder.binding.tvProfileName.visibility = View.VISIBLE
                    }
                } else {
                    expandedHolder.binding.tvProfileName.text = sign
                    expandedHolder.binding.ivProfileImage.visibility = View.GONE
                    expandedHolder.binding.tvProfileName.visibility = View.VISIBLE
                }
                expandedHolder.binding.tvName.text = currentContact.name
                expandedHolder.binding.tvPhoneNo.text = "Phone - ${currentContact.phoneNo}"
                expandedHolder.binding.tvEmail.text = "Email - ${currentContact.email}"
                expandedHolder.binding.collapseButton.setOnClickListener {
                    // Toggle the expanded state of the item
                    toggleItemExpansion(position)
                }

                expandedHolder.binding.ivCall.setOnClickListener {
                    myListener.onCallButtonClick(position)
                }
            }
        }

        holder.itemView.setOnClickListener {
            myListener.onItemClick(position)
        }
        anim(holder.itemView)
    }

    private fun anim(view: View) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 1500
        view.startAnimation(animation)
    }

    override fun getItemViewType(position: Int): Int {
        return if (expandedPositions.contains(position)) VIEW_TYPE_EXPANDED else VIEW_TYPE_COMPACT
    }

    private fun toggleItemExpansion(position: Int) {
        if (expandedPositions.contains(position)) {
            expandedPositions.remove(position)
        } else {
            expandedPositions.clear()
            expandedPositions.add(position)
        }
        notifyItemChanged(position)
    }

}