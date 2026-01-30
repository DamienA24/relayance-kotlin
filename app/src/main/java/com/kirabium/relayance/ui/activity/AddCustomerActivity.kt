package com.kirabium.relayance.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kirabium.relayance.R
import com.kirabium.relayance.databinding.ActivityAddCustomerBinding
import com.kirabium.relayance.ui.viewmodel.AddCustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCustomerBinding
    private val viewModel: AddCustomerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
        setupListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding() {
        binding = ActivityAddCustomerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setupListeners() {
        binding.saveFab.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            viewModel.addCustomer(name, email)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addCustomerState.collect { state ->
                    when (state) {
                        is AddCustomerViewModel.AddCustomerState.Success -> {
                            Toast.makeText(
                                this@AddCustomerActivity,
                                getString(R.string.customer_added_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        is AddCustomerViewModel.AddCustomerState.Error -> {
                            val messageResId = when (state.type) {
                                AddCustomerViewModel.ErrorType.EMPTY_FIELDS ->
                                    R.string.error_empty_fields
                                AddCustomerViewModel.ErrorType.INVALID_EMAIL ->
                                    R.string.error_invalid_email
                            }
                            Toast.makeText(
                                this@AddCustomerActivity,
                                getString(messageResId),
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.resetState()
                        }
                        is AddCustomerViewModel.AddCustomerState.Idle -> {
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}