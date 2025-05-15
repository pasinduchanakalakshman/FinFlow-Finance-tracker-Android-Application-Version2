package com.example.finflow.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.finflow.R
import com.example.finflow.data.TransactionRepository
import com.example.finflow.databinding.ActivityDeleteTransactionBinding
import com.example.finflow.notification.NotificationManager
import kotlinx.coroutines.launch

class DeleteTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteTransactionBinding
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var notificationManager: NotificationManager

    private var transactionId: String = ""
    private var transactionTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionRepository = TransactionRepository(this)
        notificationManager = NotificationManager(this)

        transactionId = intent.getStringExtra("transaction_id") ?: ""
        transactionTitle = intent.getStringExtra("transaction_title") ?: ""

        binding.tvDeleteConfirmation.text = getString(
            R.string.delete_transaction_confirmation, transactionTitle
        )

        binding.btnDelete.setOnClickListener {
            deleteTransaction()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun deleteTransaction() {
        lifecycleScope.launch {
            transactionRepository.deleteTransaction(transactionId)
            notificationManager.checkBudgetAndNotify()

            Toast.makeText(
                this@DeleteTransactionActivity,
                getString(R.string.transaction_deleted),
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}