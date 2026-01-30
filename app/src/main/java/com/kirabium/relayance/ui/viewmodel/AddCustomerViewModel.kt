package com.kirabium.relayance.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirabium.relayance.data.repository.CustomerRepository
import com.kirabium.relayance.domain.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _addCustomerState = MutableStateFlow<AddCustomerState>(AddCustomerState.Idle)
    val addCustomerState: StateFlow<AddCustomerState> = _addCustomerState.asStateFlow()

    sealed class AddCustomerState {
        object Idle : AddCustomerState()
        object Success : AddCustomerState()
        data class Error(val type: ErrorType) : AddCustomerState()
    }

    enum class ErrorType {
        EMPTY_FIELDS,
        INVALID_EMAIL
    }

    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    fun addCustomer(name: String, email: String) {
        viewModelScope.launch {
            when {
                name.isBlank() || email.isBlank() -> {
                    _addCustomerState.value = AddCustomerState.Error(ErrorType.EMPTY_FIELDS)
                }
                !validateEmail(email) -> {
                    _addCustomerState.value = AddCustomerState.Error(ErrorType.INVALID_EMAIL)
                }
                else -> {
                    val newId = (customerRepository.getCustomers().maxByOrNull { it.id }?.id ?: 0) + 1
                    val newCustomer = Customer(newId, name, email, Date())
                    customerRepository.addCustomer(newCustomer)
                    _addCustomerState.value = AddCustomerState.Success
                }
            }
        }
    }

    fun resetState() {
        _addCustomerState.value = AddCustomerState.Idle
    }
}
