package com.kirabium.relayance.ui.viewmodel

import com.kirabium.relayance.data.repository.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class AddCustomerViewModelTest {

    private lateinit var viewModel: AddCustomerViewModel
    private lateinit var repository: CustomerRepository
    private val testDispatcher = UnconfinedTestDispatcher()

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
        viewModel = AddCustomerViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Idle`() {
        val state = viewModel.addCustomerState.value
        assertTrue(state is AddCustomerViewModel.AddCustomerState.Idle)
    }

    @Test
    fun `validateEmail should return true for valid email format`() {
        assertTrue(viewModel.validateEmail("john.smith@example.com"))
        assertTrue(viewModel.validateEmail("user@domain.co.uk"))
        assertTrue(viewModel.validateEmail("test.user+tag@example.com"))
    }

    @Test
    fun `validateEmail should return false for invalid email format`() {
        assertFalse(viewModel.validateEmail("invalid-email"))
        assertFalse(viewModel.validateEmail("user@"))
        assertFalse(viewModel.validateEmail("@domain.com"))
        assertFalse(viewModel.validateEmail("user domain.com"))
        assertFalse(viewModel.validateEmail(""))
    }

    @Test
    fun `addCustomer with valid data should emit Success state`() = runTest(testDispatcher) {
        val initialCount = repository.getCustomers().size

        viewModel.addCustomer("John Smith", "john.smith@example.com")

        // Vérifier l'état
        val state = viewModel.addCustomerState.value
        assertTrue(state is AddCustomerViewModel.AddCustomerState.Success)

        // Vérifier que le client a été ajouté au repository
        assertEquals(initialCount + 1, repository.getCustomers().size)
    }

    @Test
    fun `addCustomer with invalid email should emit InvalidEmail error`() = runTest(testDispatcher) {
        val initialCount = repository.getCustomers().size

        viewModel.addCustomer("Jane Doe", "invalid-email")

        val state = viewModel.addCustomerState.value
        assertTrue(state is AddCustomerViewModel.AddCustomerState.Error)
        assertEquals(
            AddCustomerViewModel.ErrorType.INVALID_EMAIL,
            (state as AddCustomerViewModel.AddCustomerState.Error).type
        )

        // Vérifier que le client n'a PAS été ajouté
        assertEquals(initialCount, repository.getCustomers().size)
    }

    @Test
    fun `addCustomer with blank name should emit EmptyFields error`() = runTest(testDispatcher) {
        val initialCount = repository.getCustomers().size

        viewModel.addCustomer("", "john@example.com")

        val state = viewModel.addCustomerState.value
        assertTrue(state is AddCustomerViewModel.AddCustomerState.Error)
        assertEquals(
            AddCustomerViewModel.ErrorType.EMPTY_FIELDS,
            (state as AddCustomerViewModel.AddCustomerState.Error).type
        )
        assertEquals(initialCount, repository.getCustomers().size)
    }

    @Test
    fun `addCustomer with blank email should emit EmptyFields error`() = runTest(testDispatcher) {
        val initialCount = repository.getCustomers().size

        viewModel.addCustomer("John Smith", "")

        val state = viewModel.addCustomerState.value
        assertTrue(state is AddCustomerViewModel.AddCustomerState.Error)
        assertEquals(
            AddCustomerViewModel.ErrorType.EMPTY_FIELDS,
            (state as AddCustomerViewModel.AddCustomerState.Error).type
        )
        assertEquals(initialCount, repository.getCustomers().size)
    }

    @Test
    fun `addCustomer with both fields blank should emit EmptyFields error`() = runTest(testDispatcher) {
        val initialCount = repository.getCustomers().size

        viewModel.addCustomer("", "")

        val state = viewModel.addCustomerState.value
        assertTrue(state is AddCustomerViewModel.AddCustomerState.Error)
        assertEquals(
            AddCustomerViewModel.ErrorType.EMPTY_FIELDS,
            (state as AddCustomerViewModel.AddCustomerState.Error).type
        )
        assertEquals(initialCount, repository.getCustomers().size)
    }

    @Test
    fun `addCustomer should generate correct customer ID`() = runTest(testDispatcher) {
        val maxId = repository.getCustomers().maxByOrNull { it.id }?.id ?: 0

        viewModel.addCustomer("Test User", "test@example.com")

        val addedCustomer = repository.getCustomers().find { it.name == "Test User" }
        assertNotNull(addedCustomer)
        assertEquals(maxId + 1, addedCustomer?.id)
    }

    @Test
    fun `addCustomer should set current date for new customer`() = runTest(testDispatcher) {
        val beforeAdd = Date().time

        viewModel.addCustomer("Test User", "test@example.com")

        val addedCustomer = repository.getCustomers().find { it.name == "Test User" }
        assertNotNull(addedCustomer)
        assertTrue(addedCustomer!!.createdAt.time >= beforeAdd)
    }

    @Test
    fun `resetState should change state back to Idle`() = runTest(testDispatcher) {
        // Créer une erreur d'abord
        viewModel.addCustomer("", "")
        assertTrue(viewModel.addCustomerState.value is AddCustomerViewModel.AddCustomerState.Error)

        // Reset
        viewModel.resetState()

        // Vérifier que l'état est revenu à Idle
        assertTrue(viewModel.addCustomerState.value is AddCustomerViewModel.AddCustomerState.Idle)
    }

    @Test
    fun `multiple addCustomer calls should update state each time`() = runTest(testDispatcher) {
        // Première erreur
        viewModel.addCustomer("", "test@example.com")
        assertTrue(viewModel.addCustomerState.value is AddCustomerViewModel.AddCustomerState.Error)

        // Reset
        viewModel.resetState()
        assertTrue(viewModel.addCustomerState.value is AddCustomerViewModel.AddCustomerState.Idle)

        // Succès
        viewModel.addCustomer("Valid Name", "valid@example.com")
        assertTrue(viewModel.addCustomerState.value is AddCustomerViewModel.AddCustomerState.Success)
    }
}
