package com.example.fitfluent.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseReaderWorkouts(context: Context) : SQLiteOpenHelper(context,
    DatabaseReaderWorkouts.DATABASE_NAME, null,
    DatabaseReaderWorkouts.DATABASE_VERSION
){

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "workouts.db"
        private const val TABLE = "workouts"
        private const val ERSTELLER = "creator" //ob user oder Automatisch
        private const val TYP = "type" //hier kommt rein ob man die Übung sekunden lang ausführen muss oder wiederholungen
        private const val EXERCISES = "excercises"
        private const val TIMES = "times" //hier kommt rein wie oft das workout ausgeführt werden soll
        private const val FREQUENCY = "frequency" //hier kommt rein wie oft das workout ausgeführt werden soll
        private const val BMIRANGE = "bmi_range"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        if(!tableExists(p0)){
            val createTable = ("CREATE TABLE " + DatabaseReaderWorkouts.TABLE + "(" + DatabaseReaderWorkouts.ERSTELLER + " TEXT, " + DatabaseReaderWorkouts.TYP + " TEXT, " + DatabaseReaderWorkouts.EXERCISES + " TEXT, " + DatabaseReaderWorkouts.TIMES + " TEXT, "
                    + DatabaseReaderWorkouts.FREQUENCY + " INTEGER, " + DatabaseReaderWorkouts.BMIRANGE + " TEXT)")
            //p0?.execSQL("DROP TABLE " + TABLE)
            p0?.execSQL(createTable)

            val contentValues = ContentValues()
            contentValues.put(DatabaseReaderWorkouts.ERSTELLER, "auto")
            contentValues.put(DatabaseReaderWorkouts.TYP, "time-intervall")
            contentValues.put(DatabaseReaderWorkouts.EXERCISES, "klimmzug, liegestütze, dips")
            contentValues.put(DatabaseReaderWorkouts.TIMES, "12, 20, 16")
            contentValues.put(DatabaseReaderWorkouts.FREQUENCY, "Montag, Mittwoch, Freitag")
            contentValues.put(DatabaseReaderWorkouts.BMIRANGE, "18 - 24")

            val success = p0?.insert(DatabaseReaderWorkouts.TABLE, null, contentValues)
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS ${DatabaseReaderWorkouts.TABLE}")
        onCreate(p0)
    }

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

    fun createIfNotExists() {
        val db = this.writableDatabase

        if (!tableExists(db)) {
            val createTable =
                ("CREATE TABLE " + DatabaseReaderWorkouts.TABLE + "(" + DatabaseReaderWorkouts.ERSTELLER + " TEXT, " + DatabaseReaderWorkouts.TYP + " TEXT, " + DatabaseReaderWorkouts.EXERCISES + " TEXT, " + DatabaseReaderWorkouts.TIMES + " TEXT, "
                        + DatabaseReaderWorkouts.FREQUENCY + " INTEGER, " + DatabaseReaderWorkouts.BMIRANGE + " TEXT)")
            db.execSQL(createTable)

        }
    }

    fun registerWorkout(workout: Workout) : Long{

        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseReaderWorkouts.ERSTELLER, workout.ersteller)
        contentValues.put(DatabaseReaderWorkouts.TYP, workout.typ)
        contentValues.put(DatabaseReaderWorkouts.EXERCISES, workout.exercises)
        contentValues.put(DatabaseReaderWorkouts.TIMES, workout.times)
        contentValues.put(DatabaseReaderWorkouts.FREQUENCY, workout.frequency)
        contentValues.put(DatabaseReaderWorkouts.BMIRANGE, workout.bmiRange)

        val success = db.insert(DatabaseReaderWorkouts.TABLE, null, contentValues)
        db.close()
        return success
    }

    fun getWorkouts(user: User) : MutableList<Workout>{

        var workoutList = ArrayList<Workout>()

        val db = this.readableDatabase

        val query = "SELECT * FROM ${DatabaseReaderWorkouts.TABLE} WHERE $ERSTELLER = 'auto' OR $ERSTELLER = '${user.username}'"

        val cursor: Cursor?

        cursor = db.rawQuery(query, null)

        cursor.moveToFirst()

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

}