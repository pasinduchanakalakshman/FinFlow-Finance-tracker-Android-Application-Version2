package com.example.finflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finflow.model.Transaction
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val date: Date,
    val isExpense: Boolean
) {
    fun toTransaction(): Transaction {
        return Transaction(
            id = id,
            title = title,
            amount = amount,
            category = category,
            date = date,
            isExpense = isExpense
        )
    }

    companion object {
        fun fromTransaction(transaction: Transaction): TransactionEntity {
            return TransactionEntity(
                id = transaction.id,
                title = transaction.title,
                amount = transaction.amount,
                category = transaction.category,
                date = transaction.date,
                isExpense = transaction.isExpense
            )
        }
    }
} 