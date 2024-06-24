package com.example.flashcardapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// This template is almost always the same for any database
// Act as a database holder
@Database(
    entities = [Card::class, Deck::class],
    version = 1,
    exportSchema = false
)
abstract class FlashardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    abstract fun deckDao(): DeckDao

    // help maintain a single instance of the database opened at a given time,
    // which is an expensive resource to create and maintain. While the
    // @Volatile is used to annotate a variable that is never cached,
    // and all reads and writes are to and from the main memory.
    // These features help ensure the value of `Instance` is always
    // up to data and all changes made by any thread to `Instance`
    // are immediately visible to all other threads.
    companion object {
        @Volatile
        private var Instance: FlashardDatabase? = null

        fun getDatabase(context: Context): FlashardDatabase {
            // `synchronized` block is used so that only one thread
            // of execution at a time can enter this block of code,
            // which makes sure the database only get initialized
            // once to avoid the "Race Condition".
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FlashardDatabase::class.java,
                    "flashard_database"
                ).fallbackToDestructiveMigration().build()
                .also { Instance = it }
            }
        }
    }
}