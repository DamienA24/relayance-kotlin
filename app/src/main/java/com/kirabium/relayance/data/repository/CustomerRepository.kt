package com.kirabium.relayance.data.repository

import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.domain.model.Customer
import javax.inject.Inject

class CustomerRepository @Inject constructor() {
    private val customers = DummyData.customers.toMutableList()

    fun getCustomers(): List<Customer> = customers

    fun getCustomerById(id: Int): Customer? = customers.find { it.id == id }

    fun addCustomer(customer: Customer) {
        customers.add(customer)
    }

    fun updateCustomer(customer: Customer) {
        val index = customers.indexOfFirst { it.id == customer.id }
        if (index != -1) {
            customers[index] = customer
        }
    }

    fun deleteCustomer(id: Int) {
        customers.removeAll { it.id == id }
    }
}
