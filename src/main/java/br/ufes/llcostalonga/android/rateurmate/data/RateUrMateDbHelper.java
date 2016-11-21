package br.ufes.llcostalonga.android.rateurmate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LeandroHD on 15/11/16.
 */
public class RateUrMateDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "rateURmate.db";

    public RateUrMateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_ASSESSEMENT_TABLE = "CREATE TABLE " + RateUrMateContract.AssessementEntry.TABLE_NAME + " (" +
                RateUrMateContract.AssessementEntry._ID + " INTEGER PRIMARY KEY," +
                RateUrMateContract.AssessementEntry.COLUMN_TOPIC + " TEXT NOT NULL, " +
                RateUrMateContract.AssessementEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                RateUrMateContract.AssessementEntry.COLUMN_EVALUATOR_MAX_RATING + " REAL NOT NULL, " +
                RateUrMateContract.AssessementEntry.COLUMN_AUNDIENCE_MAX_RATING + " REAL NOT NULL, " +
                RateUrMateContract.AssessementEntry.COLUMN_RATING_STEP + " REAL NOT NULL " +
                " );";

        final String SQL_CREATE_PRESENTATION_TABLE = "CREATE TABLE " + RateUrMateContract.PresentationEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                RateUrMateContract.PresentationEntry._ID + " INTEGER PRIMARY KEY, " +

                // the ID of the location entry associated with this weather data
                RateUrMateContract.PresentationEntry.COLUMN_ASSESSEMENT_KEY + " INTEGER NOT NULL, " +
                RateUrMateContract.PresentationEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                RateUrMateContract.PresentationEntry.COLUMN_TITLE + " TEXT NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + RateUrMateContract.PresentationEntry.COLUMN_ASSESSEMENT_KEY + ") REFERENCES " +
                RateUrMateContract.AssessementEntry.TABLE_NAME + " (" + RateUrMateContract.AssessementEntry._ID + ")); " ;

        sqLiteDatabase.execSQL(SQL_CREATE_ASSESSEMENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PRESENTATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RateUrMateContract.PresentationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RateUrMateContract.AssessementEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
