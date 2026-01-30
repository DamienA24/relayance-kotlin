package com.kirabium.relayance.data.repository

import android.util.Log
import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.domain.model.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor() {
    private val customers = DummyData.customers

    private val _customersFlow = MutableStateFlow<List<Customer>>(customers.toList())
    val customersFlow: Flow<List<Customer>> = _customersFlow.asStateFlow()

    fun getCustomers(): List<Customer> {
        return customers
    }

    fun getCustomerById(id: Int): Customer? = customers.find { it.id == id }

    fun addCustomer(customer: Customer) {
        customers.add(customer)
        _customersFlow.value = customers.toList()
    }
}
