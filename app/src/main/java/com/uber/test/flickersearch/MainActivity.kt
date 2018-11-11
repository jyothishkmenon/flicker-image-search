package com.uber.test.flickersearch

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glowroad.test.recyclerviewcomponents.SpanningGridLayoutManager
import com.uber.test.flickersearch.databinding.ActivityMainBinding
import com.uber.test.flickersearch.databinding.ItemImageBinding
import com.uber.test.flickersearch.imagegrid.GenericAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val adapter = object: GenericAdapter<PhotoData, ItemImageBinding>() {
        override var resId: Int = R.layout.item_image

        override fun bindToView(data: PhotoData, itemBinding: ItemImageBinding) {
            itemBinding.data = data
            itemBinding.executePendingBindings()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.data = viewModel
        binding.imageButtonSearch.setOnClickListener {
            if (binding.editTextSearch.text.toString().isBlank()) {
                Toast.makeText(this, R.string.search_blank_error_message, LENGTH_SHORT).show()
            } else {
                hideSoftKeyboard()
                viewModel.getImagesFromFlicker(binding.editTextSearch.text.toString())
            }
        }
        binding.imageList.post{
            binding.imageList.layoutManager = SpanningGridLayoutManager(this, SPAN_COUNT, binding.imageList.width)
            binding.imageList.adapter = adapter
        }
        viewModel.photoData.observe(this, Observer {
            adapter.setData(it)
        })
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
    }

    companion object {
        private const val SPAN_COUNT = 3
    }
}
