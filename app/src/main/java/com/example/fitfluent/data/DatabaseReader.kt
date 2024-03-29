package com.example.fitfluent.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fitfluent.MainActivity
import java.time.LocalDateTime

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
        private const val CALORIE_INTAKE = "calorie"
        private const val CALORIE_TIME = "claorie_time"
        private const val ACTIVITY_LEVEL = "activity_level"
        private const val GENDER = "gender"
        private const val BMI_SCORE = "bmi_score"


    }

    // This function is called when the database is created for the first time.
    override fun onCreate(p0: SQLiteDatabase?) {
        if(!tableExists(p0)){
            val createTable = ("CREATE TABLE " + TABLE + "(" + USERNAME + " TEXT PRIMARY KEY, " + PASSWORD + " TEXT, " + AGE + " INTEGER, " + HEIGHT + " INTEGER, "
                    + WEIGHT + " INTEGER, " + CALORIE_INTAKE + " INTEGER, " + CALORIE_TIME + " TEXT, " + ACTIVITY_LEVEL + " FLOAT, " + GENDER + " TEXT, "
                    + BMI_SCORE + " FLOAT)" )
            //p0?.execSQL("DROP TABLE " + TABLE)
            p0?.execSQL(createTable)

            val contentValues = ContentValues()
            contentValues.put(USERNAME, "Admin")
            contentValues.put(PASSWORD, "Admin")
            contentValues.put(AGE, 18)
            contentValues.put(HEIGHT, 187)
            contentValues.put(WEIGHT, 78)
            contentValues.put(CALORIE_INTAKE, (655.1 + (9.6 * 78) + (1.8 * 187) - (4.7 * 18)))
            contentValues.put(CALORIE_TIME, LocalDateTime.now().toString())
            contentValues.put(ACTIVITY_LEVEL, 4)
            contentValues.put(GENDER, "male")
            contentValues.put(BMI_SCORE, 4.0)
            val success = p0?.insert(TABLE, null, contentValues)
        }
    }

    // This function is called when the database needs to be upgraded.
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(p0)
    }

    // This function is used to insert a new user into the database.
    fun register_person(user: User): Long {

        val db = writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, user.username)
        contentValues.put(PASSWORD, user.password)
        contentValues.put(AGE, user.age)
        contentValues.put(HEIGHT, user.height_in_cm)
        contentValues.put(WEIGHT, user.weight_in_kg)
        contentValues.put(CALORIE_INTAKE, (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age)))
        contentValues.put(CALORIE_TIME, LocalDateTime.now().toString())
        contentValues.put(ACTIVITY_LEVEL, user.activity_level)
        contentValues.put(GENDER, user.gender)
        contentValues.put(BMI_SCORE, user.bmi_score)
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
            return User("", "", 0, 0, 0, 0.0, LocalDateTime.now().toString(), 0, "", 0F)
        }

        cursor.moveToFirst()

        var name = cursor.getString(0)
        var password = cursor.getString(1)
        var age = cursor.getInt(2)
        var height = cursor.getInt(3)
        var weight = cursor.getInt(4)
        var calorie_intake = cursor.getDouble(5)
        var calorie_time = cursor.getString(6)
        var activityLevel = cursor.getInt(7)
        var gender = cursor.getString(8)
        var bmi_score = cursor.getFloat(9)



        return User(name, password, height, weight, age, calorie_intake.toDouble(), calorie_time, activityLevel, gender, bmi_score)
    }

    fun tableExists(p0: SQLiteDatabase?) : Boolean{
        var result = false
        val sql = "select count(*) xcount from sqlite_master where type='table' and name='" + TABLE + "'"
        val cursor: Cursor?
        //val db = this.readableDatabase
        cursor = p0?.rawQuery(sql, null)
        cursor?.moveToFirst()
        if (cursor?.getInt(0) != null && cursor.getInt(0) > 0){
            result = true
        }
        cursor?.close()

        return result
    }

    fun updateUser(olduser: User, newuser: User){

        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, newuser.username)
        contentValues.put(PASSWORD, newuser.password)
        contentValues.put(AGE, newuser.age)
        contentValues.put(HEIGHT, newuser.height_in_cm)
        contentValues.put(WEIGHT, newuser.weight_in_kg)
        contentValues.put(CALORIE_INTAKE, newuser.calorie_intake)
        contentValues.put(CALORIE_TIME, LocalDateTime.now().toString())
        contentValues.put(GENDER, newuser.gender)
        contentValues.put(BMI_SCORE, newuser.bmi_score)

        val sql = "UPDATE $TABLE SET $AGE = ${newuser.age}, $HEIGHT = ${newuser.height_in_cm}, $WEIGHT = ${newuser.weight_in_kg}, $CALORIE_INTAKE = ${newuser.calorie_intake}, $CALORIE_TIME = '${LocalDateTime.now().toString()}', $GENDER = '${newuser.gender}', $BMI_SCORE = '${newuser.bmi_score}' WHERE $USERNAME = '${olduser.username}'"

        //val selection = "$USERNAME = ? AND $PASSWORD = ? AND $AGE = ? AND $HEIGHT = ? AND $WEIGHT = ? AND $CALORIE_INTAKE = ? AND $CALORIE_TIME = ?"
        //val selectionArgs = arrayOf(olduser.username, olduser.password, olduser.age.toString(), olduser.height_in_cm.toString(), olduser.weight_in_kg.toString(), olduser.calorie_intake.toString(), olduser.calorie_time)

        //db.update(TABLE, contentValues, selection, selectionArgs)
        db.execSQL(sql)
        db.close()


    }

    fun recreateDB(){
        val db = this.writableDatabase

        val createTable = ("CREATE TABLE " + TABLE + "(" + USERNAME + " TEXT PRIMARY KEY, " + PASSWORD + " TEXT, " + AGE + " INTEGER, " + HEIGHT + " INTEGER, "
                + WEIGHT + " INTEGER, " + CALORIE_INTAKE + " INTEGER, " + CALORIE_TIME + " TEXT, " + ACTIVITY_LEVEL + " FLOAT, " + GENDER + " TEXT, "
                + BMI_SCORE + " FLOAT)" )
        db.execSQL("DROP TABLE " + TABLE)
        db.execSQL(createTable)

    }

    fun dropDB(){
        val db = this.writableDatabase
        db.execSQL("DROP TABLE $TABLE")
    }
}