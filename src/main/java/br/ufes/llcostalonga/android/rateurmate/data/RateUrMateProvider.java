package br.ufes.llcostalonga.android.rateurmate.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by LeandroHD on 17/11/16.
 */
public class RateUrMateProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RateUrMateDbHelper mOpenHelper;

    static final int ASSESSEMENT = 100;
   // static final int WEATHER_WITH_LOCATION = 101;
   // static final int WEATHER_WITH_LOCATION_AND_DATE = 102;

    static final int PRESENTATION = 200;
    static final int PRESENTATION_WITH_ASSESSMENT = 201;

    private static final SQLiteQueryBuilder sPresentationByAssessementQueryBuilder;

    static{
        sPresentationByAssessementQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sPresentationByAssessementQueryBuilder.setTables(
                RateUrMateContract.PresentationEntry.TABLE_NAME + " INNER JOIN " +
                        RateUrMateContract.AssessementEntry.TABLE_NAME +
                        " ON " + RateUrMateContract.PresentationEntry.TABLE_NAME +
                        "." + RateUrMateContract.PresentationEntry.COLUMN_ASSESSEMENT_KEY +
                        " = " + RateUrMateContract.AssessementEntry.TABLE_NAME +
                        "." + RateUrMateContract.AssessementEntry._ID);
    }

    //location.location_setting = ?
    private static final String sAssessementTopicSelection =
            RateUrMateContract.AssessementEntry.TABLE_NAME+
                    "." + RateUrMateContract.AssessementEntry.COLUMN_TOPIC +  " = ? ";


    private static final String sAssessementIdSelection =
            RateUrMateContract.AssessementEntry.TABLE_NAME+
                    "." + RateUrMateContract.AssessementEntry._ID +  " = ? ";



    //location.location_setting = ? AND date >= ?
    /*private static final String sLocationSettingWithStartDateSelection =
            RateUrMateContract.AssessementEntry.TABLE_NAME+
                    "." + RateUrMateContract.AssessementEntry..COLUMN_LOCATION_SETTING + " = ? AND " +
                    WeatherContract.WeatherEntry.COLUMN_DATE + " >= ? ";
                    */

    //location.location_setting = ? AND date = ?
  /*  private static final String sLocationSettingAndDaySelection =
            WeatherContract.LocationEntry.TABLE_NAME +
                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ";*/

    private Cursor getPresentationByAssessement(Uri uri, String[] projection, String sortOrder) {
        String assessement_id = RateUrMateContract.AssessementEntry.getAssessementFromUri(uri);

        String selection = sAssessementIdSelection;
        String[] selectionArgs = new String[]{assessement_id};

        return sPresentationByAssessementQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sAssessementIdSelection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }




    //TODO 2.1: Construir o buildUriMatcher
    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the ASSESSEMENT, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and PRESENTATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RateUrMateContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, RateUrMateContract.PATH_ASSESSEMENT, ASSESSEMENT);
       // matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        //matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);

        matcher.addURI(authority, RateUrMateContract.PATH_PRESENTATION, PRESENTATION);
        matcher.addURI(authority, RateUrMateContract.PATH_PRESENTATION + "/#", PRESENTATION_WITH_ASSESSMENT);
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new RateUrMateDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) { //não estou bem certo pra que serve.

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case ASSESSEMENT:
                return RateUrMateContract.AssessementEntry.CONTENT_TYPE;
            case PRESENTATION:
                return RateUrMateContract.PresentationEntry.CONTENT_TYPE;
            case PRESENTATION_WITH_ASSESSMENT:
                return RateUrMateContract.PresentationEntry.CONTENT_TYPE;
        }
                throw new UnsupportedOperationException("Unknown uri: " + uri);

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case PRESENTATION_WITH_ASSESSMENT:
            {
                retCursor = getPresentationByAssessement(uri, projection, sortOrder);
                break;
            }
            // "weather"
            case ASSESSEMENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RateUrMateContract.AssessementEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"
            case PRESENTATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RateUrMateContract.PresentationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ASSESSEMENT: {
                //normalizeDate(values);
                long _id = db.insert(RateUrMateContract.AssessementEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = (RateUrMateContract.AssessementEntry.buildAssessementUri(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PRESENTATION: {
                long _id = db.insert(RateUrMateContract.PresentationEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = RateUrMateContract.PresentationEntry.buildPresentationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case ASSESSEMENT:
                rowsDeleted = db.delete(
                        RateUrMateContract.AssessementEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRESENTATION:
                rowsDeleted = db.delete(
                        RateUrMateContract.PresentationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

 /*   private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(WeatherContract.WeatherEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
            values.put(WeatherContract.WeatherEntry.COLUMN_DATE, WeatherContract.normalizeDate(dateValue));
        }
    }*/

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ASSESSEMENT:
              //  normalizeDate(values);
                rowsUpdated = db.update(RateUrMateContract.AssessementEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PRESENTATION:
                rowsUpdated = db.update(RateUrMateContract.PresentationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRESENTATION: //e presentation bulk insert?
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                       // normalizeDate(value);
                        long _id = db.insert(RateUrMateContract.PresentationEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
