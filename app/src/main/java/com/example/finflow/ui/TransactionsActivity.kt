package com.example.finflow.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.finflow.R
import com.example.finflow.data.PreferencesManager
import com.example.finflow.data.TransactionRepository
import com.example.finflow.databinding.ActivityTransactionsBinding
import com.example.finflow.model.Transaction
import com.example.finflow.ui.adapters.TransactionsAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionsActivity : AppCompatActivity(), TransactionsAdapter.OnTransactionClickListener {
    private lateinit var binding: ActivityTransactionsBinding
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var adapter: TransactionsAdapter

    private val calendar = Calendar.getInstance()
    private var currentFilter = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionRepository = TransactionRepository(this)
        preferencesManager = PreferencesManager(this)

        setupBottomNavigation()
        setupMonthSelector()
        setupFilterSpinner()
        setupTransactionsList()

        binding.fabAddTransaction.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_transactions
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_transactions -> true
                R.id.nav_budget -> {
                    startActivity(Intent(this, BudgetActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupMonthSelector() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.tvCurrentMonth.text = dateFormat.format(calendar.time)

        binding.btnPreviousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateTransactionsList()
            binding.tvCurrentMonth.text = dateFormat.format(calendar.time)
        }

        binding.btnNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateTransactionsList()
            binding.tvCurrentMonth.text = dateFormat.format(calendar.time)
        }
    }

    private fun setupFilterSpinner() {
        val filters = arrayOf("All", "Expenses", "Income")
        val spinnerAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, filters
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = spinnerAdapter

        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentFilter = filters[position].lowercase()
                updateTransactionsList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupTransactionsList() {
        adapter = TransactionsAdapter(emptyList(), preferencesManager.getCurrency(), this)
        binding.recyclerTransactions.adapter = adapter
        updateTransactionsList()
    }

    private fun updateTransactionsList() {
        lifecycleScope.launch {
            val transactions = when (currentFilter) {
                "expenses" -> transactionRepository.getExpensesForMonth(
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
                )
                "income" -> transactionRepository.getIncomeForMonth(
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
                )
                else -> transactionRepository.getTransactionsForMonth(
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
                )
            }.sortedByDescending { it.date }
            
            adapter.updateTransactions(transactions)
        }
    }

    override fun onTransactionClick(transaction: Transaction) {
        val intent = Intent(this, EditTransactionActivity::class.java).apply {
            putExtra("transaction_id", transaction.id)
            putExtra("transaction_title", transaction.title)
            putExtra("transaction_amount", transaction.amount)
            putExtra("transaction_category", transaction.category)
            putExtra("transaction_date", transaction.date.time)
            putExtra("transaction_is_expense", transaction.isExpense)
        }
        startActivity(intent)
    }

    override fun onTransactionLongClick(transaction: Transaction) {
        val intent = Intent(this, DeleteTransactionActivity::class.java).apply {
            putExtra("transaction_id", transaction.id)
            putExtra("transaction_title", transaction.title)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        updateTransactionsList()
    }
}