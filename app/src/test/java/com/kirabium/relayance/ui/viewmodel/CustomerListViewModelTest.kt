package com.kirabium.relayance.ui.viewmodel

import com.kirabium.relayance.data.repository.CustomerRepository
import com.kirabium.relayance.domain.model.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class CustomerListViewModelTest {

    private lateinit var viewModel: CustomerListViewModel
    private lateinit var repository: CustomerRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        com.kirabium.relayance.data.DummyData.customers.clear()
        com.kirabium.relayance.data.DummyData.customers.addAll(
            listOf(
                com.kirabium.relayance.domain.model.Customer(1, "Alice Wonderland", "alice@example.com", com.kirabium.relayance.data.DummyData.generateDate(12)),
                com.kirabium.relayance.domain.model.Customer(2, "Bob Builder", "bob@example.com", com.kirabium.relayance.data.DummyData.generateDate(6)),
                com.kirabium.relayance.domain.model.Customer(3, "Charlie Chocolate", "charlie@example.com", com.kirabium.relayance.data.DummyData.generateDate(3)),
                com.kirabium.relayance.domain.model.Customer(4, "Diana Dream", "diana@example.com", com.kirabium.relayance.data.DummyData.generateDate(1)),
                com.kirabium.relayance.domain.model.Customer(5, "Evan Escape", "evan@example.com", com.kirabium.relayance.data.DummyData.generateDate(0))
            )
        )
        repository = CustomerRepository()
        viewModel = CustomerListViewModel(repository)
    }

    @After
    fun tearDown() {
        testScope.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun `customers flow should emit initial customers on init`() = testScope.runTest {
        val job = launch { viewModel.customers.collect {} }

        val customers = viewModel.customers.value

        assertTrue(customers.isNotEmpty())
        assertEquals(repository.getCustomers().size, customers.size)

        job.cancel()
    }

    @Test
    fun `customers flow should contain all customers from repository`() = testScope.runTest {
        val job = launch { viewModel.customers.collect {} }

        val customersFromFlow = viewModel.customers.value
        val customersFromRepo = repository.getCustomers()

        assertEquals(customersFromRepo.size, customersFromFlow.size)
        customersFromRepo.forEach { repoCustomer ->
            assertTrue(customersFromFlow.any { it.id == repoCustomer.id })
        }

        job.cancel()
    }

    @Test
    fun `customers flow should update automatically when customer is added to repository`() = testScope.runTest {
        val job = launch { viewModel.customers.collect {} }

        val initialCount = viewModel.customers.value.size

        val newCustomer = Customer(999, "Test User", "test@example.com", Date())
        repository.addCustomer(newCustomer)

        val updatedCustomers = viewModel.customers.value
        assertEquals(initialCount + 1, updatedCustomers.size)
        assertTrue(updatedCustomers.any { it.id == 999 })

        job.cancel()
    }

    @Test
    fun `customers flow should update automatically when multiple customers are added`() = testScope.runTest {
        val job = launch { viewModel.customers.collect {} }

        val beforeAdd = viewModel.customers.value.size

        repository.addCustomer(Customer(100, "User 1", "user1@example.com", Date()))
        repository.addCustomer(Customer(101, "User 2", "user2@example.com", Date()))

        val afterAdd = viewModel.customers.value.size
        assertEquals(beforeAdd + 2, afterAdd)

        job.cancel()
    }

    @Test
    fun `customers flow should reflect latest repository state`() = testScope.runTest {
        val job = launch { viewModel.customers.collect {} }

        // État initial
        val initial = viewModel.customers.value.size

        // Ajouter un client
        repository.addCustomer(Customer(200, "Added User", "added@example.com", Date()))
        assertEquals(initial + 1, viewModel.customers.value.size)

        // Ajouter un autre client
        repository.addCustomer(Customer(201, "Another User", "another@example.com", Date()))
        assertEquals(initial + 2, viewModel.customers.value.size)

        val finalCustomers = viewModel.customers.value
        assertTrue(finalCustomers.any { it.id == 200 })
        assertTrue(finalCustomers.any { it.id == 201 })

        job.cancel()
    }
}
