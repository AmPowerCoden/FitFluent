package com.example.fitfluent.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileInputStream
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class DatabaseReaderExercises (context: Context) : SQLiteOpenHelper(context, DatabaseReaderExercises.DATABASE_NAME, null, DatabaseReaderExercises.DATABASE_VERSION)
{
    // Define constants for database name, version, and table columns
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "exercises.db"
        private const val TABLE = "exercises"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val MATERIAL = "material"
        private const val IMAGE = "image"
        private const val VIDEO = "video"
    }

    // Create the database table if it does not exist
    override fun onCreate(p0: SQLiteDatabase?) {
        if(!tableExists(p0)){
            val createTable = ("CREATE TABLE " + DatabaseReaderExercises.TABLE + "(" + DatabaseReaderExercises.NAME + " TEXT, " + DatabaseReaderExercises.DESCRIPTION + " TEXT, " + DatabaseReaderExercises.MATERIAL + " TEXT, " + DatabaseReaderExercises.IMAGE + " TEXT, " + DatabaseReaderExercises.VIDEO + " TEXT)")
            //p0?.execSQL("DROP TABLE " + TABLE)
            p0?.execSQL(createTable)

            // Insert an example exercise into the database
            val contentValues = ContentValues()
            contentValues.put(DatabaseReaderExercises.NAME, "Klimmzug")
            contentValues.put(DatabaseReaderExercises.DESCRIPTION, "Beschreibung der Ausführung")
            contentValues.put(DatabaseReaderExercises.MATERIAL, "Klimmzugstange")
            contentValues.put(DatabaseReaderExercises.IMAGE, "Bildtext")
            contentValues.put(DatabaseReaderExercises.VIDEO, "video.de")

            registerExercise("Squats","Beine schulterbreit aufstellen und mit geradem Rücken nach unten gehen, bis die Knie im 90 Grad Winkel sind. Arme dabei vor dem Körper halten. Kurz verweilen und langsam wieder aufstehen.","Kein Zusatzmaterial","Squats","test")


            val success = p0?.insert(DatabaseReaderExercises.TABLE, null, contentValues)
        }

    }

    // Upgrade the database table if the version has changed
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS ${DatabaseReaderExercises.TABLE}")
        onCreate(p0)
    }

    // Check if the database table exists
    fun tableExists(p0: SQLiteDatabase?) : Boolean{
        var result = false
        val sql = "select count(*) xcount from sqlite_master where type='table' and name='" + DatabaseReaderExercises.TABLE + "'"
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

    // Insert a new exercise into the database
    fun registerExercise(name: String, description: String, material: String, image: String, video: String ) : Long{

        val db = this.writableDatabase

        // Create a new ContentValues object and set the values
        val contentValues = ContentValues()
        contentValues.put(DatabaseReaderExercises.NAME, name)
        contentValues.put(DatabaseReaderExercises.DESCRIPTION, description)
        contentValues.put(DatabaseReaderExercises.MATERIAL, material)
        contentValues.put(DatabaseReaderExercises.IMAGE, image)
        contentValues.put(DatabaseReaderExercises.VIDEO, video)

        // Insert the exercise into the database and return the success indicator
        val success = db.insert(DatabaseReaderExercises.TABLE, null, contentValues)
        db.close()
        return success
    }

    // Get all exercises from the database
    fun getExercises() : MutableList<Exercise>{
        var exerciseList = ArrayList<Exercise>()

        val db = this.readableDatabase

        // Define the query to get all exercises
        val query = "SELECT * FROM ${DatabaseReaderExercises.TABLE}"

        // Execute the query and get the results as a cursor
        val cursor: Cursor?

        // Iterate over the cursor and create an Exercise object for each row
        cursor = db.rawQuery(query, null)

        cursor.moveToFirst()

        do {
            val exercise = cursor.getString(0)
            val description = cursor.getString(1)
            val material = cursor.getString(2)
            val image = cursor.getString(3)
            val video = cursor.getString(4)
            exerciseList.add(Exercise(exercise, description, material, image, video))
        } while (cursor.moveToNext())

        return exerciseList
    }

    fun readFromExcelFile(filepath: String){
        val inputStream = FileInputStream(filepath)
        //Instantiate Excel workbook using existing file:
        var xlWb = WorkbookFactory.create(inputStream)

        //Row index specifies the row in the worksheet (starting at 0):
        val rowNumber = 0
        //Cell index specifies the column within the chosen row (starting at 0):
        val columnNumber = 0

        //Get reference to first sheet:
        val xlWs = xlWb.getSheetAt(0)
        println(xlWs.getRow(rowNumber).getCell(columnNumber))
    }

    fun fromExcelToDb (){
        val inputStream = FileInputStream("./exercises.xlsx")
        var xlWb = WorkbookFactory.create(inputStream)
        val xlWs = xlWb.getSheetAt(0)

        for (r in 2..19){
            var exercise = Exercise()
            for (c in 0..4){
                val value = xlWs.getRow(r).getCell(c)

                if (c == 0)
                {
                    exercise.name = (value.toString())
                }
                if (c == 1)
                {
                    exercise.description = (value.toString())
                }
                if (c == 2)
                {
                    exercise.material = (value.toString())
                }
                if (c == 3)
                {
                    exercise.image = (value.toString())
                }
                if (c == 4)
                {
                    exercise.video = (value.toString())
                }
            }
            //registerExercise(exercise)

        }
        xlWb.close()
    }


}

