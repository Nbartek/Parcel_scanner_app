package com.example.qr_scan_app

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

//Trzeba odejść z Exposed
class DBconnectionExposed constructor(login:String, password:String/*nie wiem czy w tym się to przechowywuje*/) {
    private var log = login;
    private var pasw =password
    private val dbConn: Database by lazy {
        val dbConnectMS = "jdbc:sqlserver://io-poczta.database.windows.net:1433;database=PocztaDB;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;sslProtocol=TLSv1.2;"
        val dbConnectjTDS = "jdbc:jtds:sqlserver://io-poczta.database.windows.net:1433/PocztaDB;ssl=request;"
        val dbDriverMs = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
        val dbDriverjTDS = "net.sourceforge.jtds.jdbc.Driver"
        Database.connect(
//            dbConnectjTDS,
//            dbDriverjTDS,
            //Moze kiedyś naprawią
            dbConnectMS,
            dbDriverMs,
            user = login,
            password = password
            //Jeśli bierzemy jTDS
        )
    }
    suspend fun isConnected(): Boolean {

        return withContext(Dispatchers.IO){
        try {
            transaction(dbConn){
                val conn = TransactionManager.current().connection
                val query = "SELECT 1"
                val statement = conn.prepareStatement(query,false)
                statement.executeQuery()
            }
            TransactionManager.defaultDatabase = dbConn
            true
        } catch (e: Exception) {
            Log.e("Database coonect: ","Connection failed: ${e.message}")
            false
        }
        }
    }
    fun getLogin():String{
        return log;
    }
    fun getPassword():String{
        return pasw
    }
}