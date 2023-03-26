package com.example.fitfluent.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.math.E

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
    fun registerExercise(exercise: Exercise) : Long{

        val db = this.writableDatabase

        // Create a new ContentValues object and set the values
        val contentValues = ContentValues()
        contentValues.put(DatabaseReaderExercises.NAME, exercise.name)
        contentValues.put(DatabaseReaderExercises.DESCRIPTION, exercise.description)
        contentValues.put(DatabaseReaderExercises.MATERIAL, exercise.material)
        contentValues.put(DatabaseReaderExercises.IMAGE, exercise.image)
        contentValues.put(DatabaseReaderExercises.VIDEO, exercise.video)

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

    fun createTable(){
        val db = this.writableDatabase
        if (!tableExists(db))
        {
            db.execSQL("CREATE TABLE $TABLE ($NAME TEXT, $DESCRIPTION TEXT, $MATERIAL TEXT, $IMAGE TEXT, $VIDEO TEXT)")

            registerExercise(Exercise("Squats","Beine schulterbreit aufstellen und mit geradem Rücken nach unten gehen, bis die Knie im 90 Grad Winkel sind. Arme dabei vor dem Körper halten. Kurz verweilen und langsam wieder aufstehen.","Kein Zusatzmaterial","Squats","test"))
            registerExercise(Exercise("Planke","Ausgehende Liegestützposition, Arme bleiben gestreckt. Abwechselnd wird ein Bein gehoben. Spannung kurz halten. Das Bein wieder abstellen und das andere Heben.","Kein Zusatzmaterial","Planke","test"))
            registerExercise(Exercise("Bergsteiger","Liegestützposition einnehmen. Abwechslend ein Bein in Richtung des Kinns bewegen. Zurückführen und das andere Bein vorführen. Die Körperline bleibt dabei erhalten. Bauch dabei anspannen. Zur steigerung kann die Geschwindigkeit der Übung gesteigert werden.","Kein Zusatzmaterial","Bergsteiger","test"))
            registerExercise(Exercise("Wadenheber","Hüftbreit aufstellen und die Fersen paralell vom Boden heben, danach wieder in die Ausgangsposition bringen. Dabei die Fersen nicht absetzen. Aufwärtsbewegung wiedeholen. Zur Steigerung kann ein Bein angehoben werden und die Übung auf einem Bein absolviert werdern. Dementsprechend nach angegebener Zeit das Bein wechseln. Am Anfang kann sich auch an einem Tisch oder Regal festgehalten werden.","Kein Zusatzmaterial","Wadenheben","test"))
            registerExercise(Exercise("Crunches","Auf den Rücken legen, Beine leicht angewinket abstellen. Hände hinter den Kopf und den Oberkörper in Richtung der Knie bewegen. Kopf dabei in einer Linie mit dem Oberkörper halten. Wichtig, den Rücken nicht krümmen. ","Yogamatte","Crunches","test"))
            registerExercise(Exercise("Dips an Stuhlkannte","Händflächen hinter dem Rücken auf die Sitzfläche legen, die Ellenbogen zeigen dabei nach außen. Beine ausstrecken und dabei auf die Fersen stellen. Nun langsam die Ellenbogen anwinkeln und mit geradem Rücken Richtung Boden bewegen, nicht abstetzen. Danach eine Aufwärtsbewegung ausführen. Spannung im Bauch und Oberkörper halten.","Stuhl","DipsStuhlkannte","test"))
            registerExercise(Exercise("Bizeps mit Fitnessband oder Wasserflasche","Hüftbreit mit geradem Oberkörper auftellen, die Knie dabei leicht gebeugt halten. Abwechselnd den Unterarm beugen und die Hand Richtung Schulter bewegen. Dabei den Ellenbogen an der Hüfte bzw Taile halten. Diese Übung kann entweder mit Wasserflasen oder einem Fittness- / Terraband  durchgefürhrt werden. Beim Fitnessband: Mit geschlossenen Beinen auf das Band stellen, gerader Oberkörper und abwechselnd die Arme anwinkeln.","Wasserflasche oder Fitnessband","BizepsFitnessband","test"))
            registerExercise(Exercise("Schulterdrücken am Türrahmen","Hüftbreit in die geöffnete Tür stellen und die kurze Seite des Türrahmens zwischen die Handflächen bringen. Dabei die Ellenbogen vor den Körper bringen. Die angegebene Zeit so kräftig wie möglich Kraft aufbringen um den Rahmen \"zusammenzudrücken\"","Kein Zusatzmaterial","Kein Bild","test"))
            registerExercise(Exercise("Superman","Flach auf den Boden legen und Arme sowie Beine vom Boden abheben und ausstrecken. Spannung halten. Die angegeben Zeit in dieser Position veweilen.","Yogamatte","Superman","test"))
            registerExercise(Exercise("Kreuzheben mit Flaschen","Schulterbreit aufstellen und mit gerdem Rücken nach unten bewegen bis die Knie 90 Grad gebeugt sind. Diese Übung ist ähnlich wie bei Squats. Dabei werden die Arme mit Gewichten in den Händen nah am Körper gefüht.","Wasserflaschen","KreuzhebenMitFlaschen","test"))
            registerExercise(Exercise("Latziehen mit Fitnessband","Mit geschlossenen, gestreckten Beinen auf den Boden setzten.Oberkörper dabei im 90 Gradwinkel. Fitnessband um die Füße legen und so greifen, dass die Arme dabei ausgestreckt sind. Nun das Band an den Körper heranziehen. Dabei die Hände nah an der Taile fühen. Die Schulterblätter dabei zueinander ziehen. Position kurz halten und Arme wieder nach vorne ausstrecken.","Fitnessband","LatziehenMitFitnessband","test"))
            registerExercise(Exercise("Klimmzüge","Greife die Stange im breiten Obergriff: Dabei liegen die Daumen unter der Stange, weil du sie von oben umgreifst. Der Abstand deiner Hände sollte weiter als schulterbreit sein. Schulterblätter runter und zusammenziehen, Core anspannen. Der Rücken beibt dabei gerade. Knie leicht anwinkeln.","Klimmzugstange","Klimmzug","test"))
            registerExercise(Exercise("Liegestütze","Handflächen parallel zwischen Schulter und Brusthöheweiter Griff, Ellenbogen zeigen nach außen, Core anspannen. Beine leicht auseinander. Langsam nach unten bewegen. Die Ellenbogen zeigen dabei nach außen. Oberkörper in der unteren Position dabei nicht ablegen. Die Körperline bleibt erhalten.","Kein Zusatzmaterial","Liegestuetz","test"))
            registerExercise(Exercise("Ausfallschritte mit Rotation","Großer Ausfallschritt nach vorne. Herunterbeigen, bis das vordere Beim im 90 Grad Winkel ist. Der Rücken bleibt gerade und das hintere Knie berührt nicht den Boden. Anschließend die Hände vor der Brust zusammenführen und eine Rotation des Oberkörpers in eine Richtung ausführen. Nach angegebener Zeit die Seite wechseln. Zur Steigerung können Gewichte in den Händen gehalten werden.","Yogamatte","AusfallschrittMitRotation","test"))
            registerExercise(Exercise("Schulterbrücke","Kopf und Schultern auf dem Boden ablegen, Füße aufstellen und die Hüfte Richtung Decke drücken, dabei bilden Oberschenkl und Oberkörper eine Line. Spannung kurz halten. Danach wird eine Abwärtsbewegung ausgeführt. Po dabei nicht ablegen. Hände ausgestreckt und vor der Brust zusammenführen. Alternativ kann ein Bein nach oben gesteckt werden. Seite wechseln","Yogamatte","Schulterbruecke","test"))
            registerExercise(Exercise("Wandsitzen","Beine schulterbreit aufstellen, mit dem Rücken gegen die Wand lehnen und die Knie in 90 Grad Winkel bringen. Der Oberkörper sollte vollflächig an der Wand anligen. Spannung halten. Zur Steigerung können die Fersen vom Boden gelößt werrden und leiche Wippbewegungen durchgeführt werden.","Kein Zusatzmaterial","Wandsitz","test"))
            registerExercise(Exercise("Seitstütz","Seitlicher Liegestütz mit einer hand unter der Schulter. Hand und Ellenbogen gerühren den Boden. Die obere Hand liegt seitlich auf der Hüfte. Füße sind übereinander. Der Körper befindet sich nun in einer Linie. Nun wird eine Aufwärtbewegung der Hüfte ausgeführt. Spannung kurz halten. danach wieder in die Ausgangsposition bewegen. Nach der angegeben Zeit die Seite wechseln.","Kein Zusatzmaterial","Seitstuetz","test"))
            registerExercise(Exercise("Arm- und Beinstrecken im Vierfüßlerstand","Vierfüßlerstand einnehmen. Knie unterhalb der Hüfte. Arme unterhalb der Schulter. Nun diagonal Arm und Bein austrecken und Position für die angegeben Zeit halten, danach Seite wechseln. Der Kopf bleibt dabei gerade.","Yogamatte","ArmUndBeinVierfuesslerstand","test"))

        }
    }

    fun dropTable(){
        val db = this.writableDatabase
        db.execSQL("DROP TABLE $TABLE")
    }


}

