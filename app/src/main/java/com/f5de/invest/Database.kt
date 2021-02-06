package com.f5de.invest

import android.content.Context
import androidx.room.*

@Entity(tableName = "Stock")
data class Stock(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "company_token") val companyToken: String,
    @ColumnInfo(name = "company_name") val companyName: String,
    @ColumnInfo(name = "money") val money: Float,
    @ColumnInfo(name = "stock_amount") val stockAmount: Int,
    @ColumnInfo(name = "stock_price") val price: Float
)

@Dao
interface StockDao {
    @Query("SELECT * FROM Stock")
    fun getAll(): List<Stock>

    @Query("SELECT * FROM Stock WHERE id = :stockIds")
    fun loadAllByIds(stockIds: IntArray): List<Stock>

    @Insert
    fun insert(stock: Stock)

    @Update
    fun update(stock: Stock)

    @Delete
    fun delete(stock: Stock)

    @Query("DELETE FROM stock WHERE company_token = :companyToken")
    fun deleteByToken(companyToken: String)

    @Query("UPDATE stock SET stock_amount = :stockAmount, stock_price = :stockPrice, money = :money WHERE company_token = :companyToken")
    fun updateByToken(companyToken: String, stockAmount: Int, stockPrice: Float, money: Float)
}

@Database(entities = arrayOf(Stock::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "stockDB.db"
        )
            .build()
    }
}