package com.example.fitfluent.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseReader(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "users.db"
        private const val TABLE = "users"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private const val AGE = "age"
        private const val HEIGHT = "height"
        private const val WEIGHT = "weight"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        if(!tableExists()){
            val createTable = ("CREATE TABLE " + TABLE + "(" + USERNAME + " TEXT PRIMARY KEY, " + PASSWORD + " TEXT, " + AGE + " INTEGER, " + HEIGHT + " INTEGER, " + WEIGHT + " INTEGER)")
            p0?.execSQL(createTable)
            register_person(User("Admin", "Admin1", 182, 70, 18))
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(p0)
    }

    fun register_person(user: User): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, user.username)
        contentValues.put(PASSWORD, user.password)
        contentValues.put(AGE, user.age)
        contentValues.put(HEIGHT, user.height_in_cm)
        contentValues.put(WEIGHT, user.weight_in_kg)
        val success = db.insert(TABLE, null, contentValues)
        db.close()
        return success
    }

    fun getUser(username:String, password:String) : User{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE WHERE username = '$username' AND password = '$password'"

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(query, null)
        }
        catch (e: Exception)
        {
            return User("", "", 0, 0, 0)
        }

        cursor.moveToFirst()

        var name = cursor.getString(0)
        var password = cursor.getString(1)
        var age = cursor.getInt(2)
        var height = cursor.getInt(3)
        var weight = cursor.getInt(4)



        return User(name, password, height, weight, age)
    }

    fun tableExists() : Boolean{
        var result = false
        val sql = "select count(*) xcount from sqlite_master where type='table' and name='" + TABLE + "'"
        val cursor: Cursor?
        val db = this.readableDatabase
        cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        if (cursor.getInt(0) > 0){
            result = true
        }
        cursor.close()

        return result
    }
}