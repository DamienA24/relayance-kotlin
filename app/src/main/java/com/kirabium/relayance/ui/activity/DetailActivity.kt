package com.kirabium.relayance.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kirabium.relayance.ui.composable.DetailScreen
import com.kirabium.relayance.ui.viewmodel.CustomerDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CUSTOMER_ID = "customer_id"
    }

    private val viewModel: CustomerDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val customerId = intent.getIntExtra(EXTRA_CUSTOMER_ID, -1)
        viewModel.getCustomerById(customerId)?.let {
            setContent {
                DetailScreen(customer = it) {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }
}


