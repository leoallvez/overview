package br.dev.singular.overview.data.local.database

import android.content.Context
import android.content.res.AssetManager
import androidx.sqlite.db.SupportSQLiteDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.io.ByteArrayInputStream

class UtilsTest {

    @Test
    fun `runScriptFromAssets should execute SQL commands from asset file`() {
        val context: Context = mockk()
        val assets: AssetManager = mockk()
        val db: SupportSQLiteDatabase = mockk(relaxed = true)
        val script = "CREATE TABLE test (id INT); INSERT INTO test VALUES (1);"
        val inputStream = ByteArrayInputStream(script.toByteArray())

        every { context.assets } returns assets
        every { assets.open("test.sql") } returns inputStream

        db.runScriptFromAssets(context, "test.sql")

        verify { db.beginTransaction() }
        verify { db.execSQL("CREATE TABLE test (id INT)") }
        verify { db.execSQL("INSERT INTO test VALUES (1)") }
        verify { db.setTransactionSuccessful() }
        verify { db.endTransaction() }
    }
}
