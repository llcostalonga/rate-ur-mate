package br.ufes.llcostalonga.android.rateurmate.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by LeandroHD on 15/11/16.
 */
public class RateUrMateContract {

    //TODO 1 - Adicionar URI no Contrato

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "br.ufes.llcostalonga.android.rateurmate.app";


    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_USER = "user";
    public static final String PATH_ASSESSEMENT = "assessement";
    public static final String PATH_PRESENTATION = "presentation";
    public static final String PATH_EVALUATION = "evaluation";
    //TODO Verificar quais mais serão necessários.



    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the location table */
    public static final class AssessementEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ASSESSEMENT).build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ASSESSEMENT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ASSESSEMENT;

        // Table name
        public static final String TABLE_NAME = "assessement";

        // The location setting string is what will be sent to openweathermap
        // as the location query.

        //TODO verificar se isso é preciso..não entendi.
        //public static final String COLUMN_LOCATION_SETTING = "location_setting";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_TOPIC = "topic";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_EVALUATOR_MAX_RATING = "evaluatorMaxRating";
        public static final String COLUMN_AUNDIENCE_MAX_RATING = "audienceMaxRating";
        public static final String COLUMN_RATING_STEP = "ratingStep";

       //TODO Ver como faz com os Ids para evaluationCriteria, evaluators, presentations, e audienceRanking.

        public static Uri buildAssessementUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getAssessementFromUri(Uri uri) {
            return (uri.getPathSegments().get(1));
        }

    }

    /* Inner class that defines the table contents of the weather table */
    public static final class PresentationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRESENTATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRESENTATION;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRESENTATION;

        public static final String TABLE_NAME = "presentation";


        public static final String COLUMN_ASSESSEMENT_KEY = "assessement_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DATE = "date";


        /* TODO ver como fazer para pegar isso!
           private ArrayList<Presenter> presenters;
           private ArrayList<Evaluation> evaluations ;
           public ArrayList<Evaluation> getEvaluations() {
         */

        public static Uri buildPresentationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPresentationAssessement(Long assessement_id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(assessement_id)).build();
        }

        public static String getPresentationFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }

}
