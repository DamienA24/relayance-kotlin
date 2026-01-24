package com.kirabium.relayance.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.kirabium.relayance.data.repository.CustomerRepository
import com.kirabium.relayance.domain.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerDetailViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    fun getCustomerById(id: Int): Customer? = customerRepository.getCustomerById(id)
}
