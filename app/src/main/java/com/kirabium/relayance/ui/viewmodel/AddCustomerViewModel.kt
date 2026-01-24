package com.kirabium.relayance.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.kirabium.relayance.data.repository.CustomerRepository
import com.kirabium.relayance.domain.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    fun addCustomer(name: String, email: String): Boolean {
        return if (name.isNotBlank() && email.isNotBlank()) {
            val newId = (customerRepository.getCustomers().maxByOrNull { it.id }?.id ?: 0) + 1
            val newCustomer = Customer(newId, name, email, Date())
            customerRepository.addCustomer(newCustomer)
            true
        } else {
            false
        }
    }
}
