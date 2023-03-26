package com.example.fitfluent.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Defining a class `DatabaseReaderWorkouts` that extends the `SQLiteOpenHelper` class
class DatabaseReaderWorkouts(context: Context) : SQLiteOpenHelper(context,
    DatabaseReaderWorkouts.DATABASE_NAME, null,
    DatabaseReaderWorkouts.DATABASE_VERSION
){

    // Declaring companion object that will hold constants for our table and database name, and other columns
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "workouts.db"
        private const val TABLE = "workouts"
        private const val ERSTELLER = "creator"         //Check if it is an user or automatic creator
        private const val TYP = "type"                  //Select the interval for each excercis in seconds or how many repetitions
        private const val EXERCISES = "excercises"
        private const val TIMES = "times"               //How often should you do teh workout
        private const val FREQUENCY = "frequency"       //Weekly intervall for the workout
        private const val BMIRANGE = "bmi_range"
    }

    // Overriding the `onCreate` method of the `SQLiteOpenHelper` class. This method will create the database table if it doesn't exist.
    override fun onCreate(p0: SQLiteDatabase?) {
        if(!tableExists(p0)){                          // Checking if the table exists
            // Creating the table with SQL query
            val createTable = ("CREATE TABLE " + DatabaseReaderWorkouts.TABLE + "(" + DatabaseReaderWorkouts.ERSTELLER + " TEXT, " + DatabaseReaderWorkouts.TYP + " TEXT, " + DatabaseReaderWorkouts.EXERCISES + " TEXT, " + DatabaseReaderWorkouts.TIMES + " TEXT, "
                    + DatabaseReaderWorkouts.FREQUENCY + " INTEGER, " + DatabaseReaderWorkouts.BMIRANGE + " TEXT)")
            //p0?.execSQL("DROP TABLE " + TABLE)
            // Executing the SQL query to create table
            p0?.execSQL(createTable)

            // Adding some initial data to the table with `ContentValues` object
            val contentValues = ContentValues()
            contentValues.put(DatabaseReaderWorkouts.ERSTELLER, "auto")
            contentValues.put(DatabaseReaderWorkouts.TYP, "time-intervall")
            contentValues.put(DatabaseReaderWorkouts.EXERCISES, "klimmzug, liegestütze, dips")
            contentValues.put(DatabaseReaderWorkouts.TIMES, "12, 20, 16")
            contentValues.put(DatabaseReaderWorkouts.FREQUENCY, "Montag, Mittwoch, Freitag")
            contentValues.put(DatabaseReaderWorkouts.BMIRANGE, "18 - 24")

            // Inserting the `ContentValues` object into the database table
            val success = p0?.insert(DatabaseReaderWorkouts.TABLE, null, contentValues)
        }
    }

    // Overriding the `onUpgrade` method of the `SQLiteOpenHelper` class. This method will upgrade the database version if there is any change in schema.
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS ${DatabaseReaderWorkouts.TABLE}")
        onCreate(p0)                    // Creating the table again
    }

    // Function to check if the table exists
    fun tableExists(p0: SQLiteDatabase?) : Boolean{
        var result = false
        val sql = "select count(*) xcount from sqlite_master where type='table' and name='" + DatabaseReaderWorkouts.TABLE + "'"
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

    // Function to create the workout table if it doesn't already exist in the database
    fun createIfNotExists() {
        val db = this.writableDatabase

        // Check if the table exists
        if (!tableExists(db)) {
            //db.execSQL("DROP TABLE " + TABLE)

            // If the table doesn't exist, create it
            val contentValues = ContentValues()
            contentValues.put(DatabaseReaderWorkouts.ERSTELLER, "auto")
            contentValues.put(DatabaseReaderWorkouts.TYP, "time-intervall")
            contentValues.put(DatabaseReaderWorkouts.EXERCISES, "klimmzug, liegestütze, dips")
            contentValues.put(DatabaseReaderWorkouts.TIMES, "12, 20, 16")
            contentValues.put(DatabaseReaderWorkouts.FREQUENCY, "Montag, Mittwoch, Freitag")
            contentValues.put(DatabaseReaderWorkouts.BMIRANGE, "18 - 24")

            // Insert the workout data into the table
            val success = db.insert(DatabaseReaderWorkouts.TABLE, null, contentValues)

            // Define the SQL statement to create the workout table
            val createTable =
                ("CREATE TABLE " + DatabaseReaderWorkouts.TABLE + "(" + DatabaseReaderWorkouts.ERSTELLER + " TEXT, " + DatabaseReaderWorkouts.TYP + " TEXT, " + DatabaseReaderWorkouts.EXERCISES + " TEXT, " + DatabaseReaderWorkouts.TIMES + " TEXT, "
                        + DatabaseReaderWorkouts.FREQUENCY + " INTEGER, " + DatabaseReaderWorkouts.BMIRANGE + " TEXT)")
            // Execute the SQL statement to create the table
            db.execSQL(createTable)

            // Insert the workout data into the table again
            db.insert(DatabaseReaderWorkouts.TABLE, null, contentValues)

        }
    }

    // Function to register a workout in the database
    fun registerWorkout(workout: Workout) : Long{

        val db = this.writableDatabase

        // Insert the workout data into the workout table
        val contentValues = ContentValues()
        contentValues.put(DatabaseReaderWorkouts.ERSTELLER, workout.ersteller)
        contentValues.put(DatabaseReaderWorkouts.TYP, workout.typ)
        contentValues.put(DatabaseReaderWorkouts.EXERCISES, workout.exercises)
        contentValues.put(DatabaseReaderWorkouts.TIMES, workout.times)
        contentValues.put(DatabaseReaderWorkouts.FREQUENCY, workout.frequency)
        contentValues.put(DatabaseReaderWorkouts.BMIRANGE, workout.bmiRange)

        val success = db.insert(DatabaseReaderWorkouts.TABLE, null, contentValues)

        // Close the database connection
        db.close()

        // Return the ID of the newly inserted row
        return success
    }

    // Function to get workouts from the workout table based on a user's BMI score
    fun getWorkouts(user: User) : MutableList<Workout>{

        var workoutList = ArrayList<Workout>()

        val db = this.readableDatabase

        // Define the SQL query to get workouts from the workout table
        val query = "SELECT * FROM ${DatabaseReaderWorkouts.TABLE} WHERE $ERSTELLER = 'auto' OR $ERSTELLER = '${user.username}'"

        // Execute the SQL query and retrieve the results
        val cursor: Cursor?

        cursor = db.rawQuery(query, null)

        // Move the cursor to the first row of the result set
        cursor.moveToFirst()

        // Iterate over the result set and add each workout to the workout list
        do {
            val ersteller = cursor.getString(0)
            val typ = cursor.getString(1)
            val exercise = cursor.getString(2)
            val times = cursor.getString(3)
            val frequency = cursor.getString(4)
            val bmiRange = cursor.getString(5)
            if (!bmiRange.isEmpty()){
                val ranges = bmiRange.split(" - ")
                if ((user.bmi_score.toDouble() > ranges[0].toDouble() && user.bmi_score.toDouble() < ranges[1].toDouble()) || bmiRange.isEmpty())
                {
                    workoutList.add(Workout(ersteller, typ, exercise, times, frequency, bmiRange))
                }
            }
            else{
                workoutList.add(Workout(ersteller, typ, exercise, times, frequency, bmiRange))
            }


        } while (cursor.moveToNext())

        return workoutList
    }

    fun dropDB(){
        val db = this.writableDatabase
        db.execSQL("DROP TABLE ${TABLE}")
    }

}