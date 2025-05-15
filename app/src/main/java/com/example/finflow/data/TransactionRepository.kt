package com.example.finflow.data

import android.content.Context
import com.example.finflow.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class TransactionRepository(context: Context) {
    private val transactionDao = TransactionDatabase.getDatabase(context).transactionDao()

    suspend fun saveTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.insertTransaction(TransactionEntity.fromTransaction(transaction))
        }
    }

    suspend fun deleteTransaction(transactionId: String) {
        withContext(Dispatchers.IO) {
            transactionDao.deleteTransactionById(transactionId)
        }
    }

    suspend fun getAllTransactions(): List<Transaction> {
        return withContext(Dispatchers.IO) {
            transactionDao.getAllTransactions().map { it.toTransaction() }
        }
    }

    suspend fun getTransactionsForMonth(month: Int, year: Int): List<Transaction> {
        return withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, 1, 0, 0, 0)
            val startDate = calendar.time

            calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
            val endDate = calendar.time

            transactionDao.getTransactionsForDateRange(startDate, endDate)
                .map { it.toTransaction() }
        }
    }

    suspend fun getExpensesForMonth(month: Int, year: Int): List<Transaction> {
        return withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, 1, 0, 0, 0)
            val startDate = calendar.time

            calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
            val endDate = calendar.time

            transactionDao.getExpensesForDateRange(startDate, endDate)
                .map { it.toTransaction() }
        }
    }

    suspend fun getIncomeForMonth(month: Int, year: Int): List<Transaction> {
        return withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, 1, 0, 0, 0)
            val startDate = calendar.time

            calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
            val endDate = calendar.time

            transactionDao.getIncomeForDateRange(startDate, endDate)
                .map { it.toTransaction() }
        }
    }

    suspend fun getTotalExpensesForMonth(month: Int, year: Int): Double {
        return getExpensesForMonth(month, year).sumOf { it.amount }
    }

    suspend fun getTotalIncomeForMonth(month: Int, year: Int): Double {
        return getIncomeForMonth(month, year).sumOf { it.amount }
    }

    suspend fun getExpensesByCategory(month: Int, year: Int): Map<String, Double> {
        val expenses = getExpensesForMonth(month, year)
        return expenses.groupBy { it.category }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }
    }

    suspend fun backupToInternalStorage(context: Context): Boolean {
        return try {
            val transactions = getAllTransactions()
            val transactionsJson = com.google.gson.Gson().toJson(transactions)
            context.openFileOutput("transactions_backup.json", Context.MODE_PRIVATE).use {
                it.write(transactionsJson.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun restoreFromInternalStorage(context: Context): Boolean {
        return try {
            context.openFileInput("transactions_backup.json").use { inputStream ->
                val transactionsJson = inputStream.bufferedReader().use { it.readText() }
                val type = object : com.google.gson.reflect.TypeToken<List<Transaction>>() {}.type
                val transactions: List<Transaction> = com.google.gson.Gson().fromJson(transactionsJson, type)
                transactions.forEach { saveTransaction(it) }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
