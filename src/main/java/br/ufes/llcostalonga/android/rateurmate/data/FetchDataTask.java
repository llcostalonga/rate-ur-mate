package br.ufes.llcostalonga.android.rateurmate.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.ufes.llcostalonga.android.rateurmate.data.RateUrMateContract;
import br.ufes.llcostalonga.android.rateurmate.model.Assessment;
import br.ufes.llcostalonga.android.rateurmate.model.Evaluation;
import br.ufes.llcostalonga.android.rateurmate.model.EvaluationCriteria;
import br.ufes.llcostalonga.android.rateurmate.model.Evaluator;
import br.ufes.llcostalonga.android.rateurmate.model.Presentation;
import br.ufes.llcostalonga.android.rateurmate.model.Presenter;

/**
 * Created by LeandroHD on 19/11/16.
 */
public class FetchDataTask extends AsyncTask<Void, Void, Void> {

    private final Context mContext;


    public FetchDataTask(Context context) {
        mContext = context;
    }

    protected Void doInBackground(Void... params) {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;

        ArrayList<Presentation> returnPresentations = null;


        try {

            //URL url = new URL("https://raw.githubusercontent.com/llcostalonga/rate-ur-mate/Lesson-2/fakeAssessment.jason");

            //single assessement
            // URL url = new URL("https://raw.githubusercontent.com/llcostalonga/rate-ur-mate/Lesson-2/assessmentJsonFile.json");

            //multiple assessements
            URL url = new URL("https://raw.githubusercontent.com/llcostalonga/rate-ur-mate/master/assessements.json");


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();

            Log.v("Assessment", "json" + jsonStr);


        } catch (IOException e) {
            Log.e("Assessment", "Error ", e);
            // If the code didn't successfully get the  data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Assessment", "Error closing stream", e);
                }
            }
        }

        //FakeAssessmentDataParser parser = new FakeAssessmentDataParser();
        JsonDataParser parser = new JsonDataParser();


        try {

            parser.processAssessementValues(jsonStr);

        } catch (Exception e) {
            Log.e("Assessment", "Error closing stream", e);
        }

        return null;
    }


    public class JsonDataParser {


        public void processAssessementValues(String jsonString) {

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                JSONArray jsonAssessements = jsonObject.getJSONArray("assessements");

                for (int i = 0; i < jsonAssessements.length(); i++) {

                    long assessementId = -1;

                    JSONObject assessementObject = jsonAssessements.getJSONObject(i);


                    String topic = (String) assessementObject.get("topic");

                    // First, check if the location with this city name exists in the db
                    Cursor assessementCursor = mContext.getContentResolver().query(
                            RateUrMateContract.AssessementEntry.CONTENT_URI,
                            new String[]{RateUrMateContract.AssessementEntry._ID},
                            RateUrMateContract.AssessementEntry.COLUMN_TOPIC + " = ?",
                            new String[]{topic},
                            null);

                    if (assessementCursor.moveToFirst()) {
                        int assessementIdIndex = assessementCursor.getColumnIndex(RateUrMateContract.PresentationEntry._ID);
                        assessementId = assessementCursor.getLong(assessementIdIndex);
                    } else {


                        ContentValues assessementValues = new ContentValues();

                        // Then add the data, along with the corresponding name of the data type,
                        // so the content provider knows what kind of value is being inserted.
                        assessementValues.put(RateUrMateContract.AssessementEntry.COLUMN_TOPIC, (String) assessementObject.get("topic"));
                        assessementValues.put(RateUrMateContract.AssessementEntry.COLUMN_SUMMARY, (String) assessementObject.get("summary"));
                        assessementValues.put(RateUrMateContract.AssessementEntry.COLUMN_EVALUATOR_MAX_RATING, assessementObject.getDouble("evaluatorMaxRating"));
                        assessementValues.put(RateUrMateContract.AssessementEntry.COLUMN_AUNDIENCE_MAX_RATING, assessementObject.getDouble("audiencerMaxRating")); //Acertar o nome
                        assessementValues.put(RateUrMateContract.AssessementEntry.COLUMN_RATING_STEP, assessementObject.getDouble("minRatingIncrement"));

                        Uri insertedUri = mContext.getContentResolver().insert(
                                RateUrMateContract.AssessementEntry.CONTENT_URI,
                                assessementValues
                        );

                        assessementId = ContentUris.parseId(insertedUri);

                    }

                    JSONArray jsonPresentations = assessementObject.getJSONArray("presentations");
                    processPresentations(jsonPresentations, assessementId);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private void processPresentations(JSONArray jsonPresentations, long assessmentId) {

            ArrayList<Presentation> presentations = new ArrayList();

            try {


                for (int i = 0; i < jsonPresentations.length(); i++) {

                    JSONObject presentationObject = jsonPresentations.getJSONObject(i);


                    String title = (String) presentationObject.get("title");
                    //todo verificar se existe o dado antes de criar.
                    // String date = (String)presentationObject.get("date");


                    JSONArray jsonPresenters = presentationObject.getJSONArray("presenters");
                    ArrayList presenters = getRebuiltPresenters(jsonPresenters);

                    JSONArray jsonEvaluations = presentationObject.getJSONArray("evaluations");
                    ArrayList evaluations = getRebuiltEvaluations(jsonEvaluations);


                    Presentation rebuiltPresentation = new Presentation((String) presentationObject.get("title"),
                            presenters);

                    //todo verificar se existe o dado antes de criar.
                    // rebuiltPresentation.setDate((String)presentationObject.get("date"));

                    rebuiltPresentation.setEvaluations(evaluations);

                    //todo falta completar com audience settigns


                    presentations.add(rebuiltPresentation);


                    ContentValues presentationValues = new ContentValues();

                    // Then add the data, along with the corresponding name of the data type,
                    // so the content provider knows what kind of value is being inserted.
                    presentationValues.put(RateUrMateContract.PresentationEntry.COLUMN_ASSESSEMENT_KEY, assessmentId);
                    presentationValues.put(RateUrMateContract.PresentationEntry.COLUMN_TITLE, title);
                    //presentationValues.put(RateUrMateContract.PresentationEntry.COLUMN_DATE,date);
                    presentationValues.put(RateUrMateContract.PresentationEntry.COLUMN_DATE, "20/11/2016"); //todo colocar data no jSon


                    Uri insertedUri = mContext.getContentResolver().insert(
                            RateUrMateContract.PresentationEntry.CONTENT_URI,
                            presentationValues
                    );

                    long presentationId = ContentUris.parseId(insertedUri);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        private ArrayList<Presenter> getRebuiltPresenters(JSONArray jsonPresenters) throws JSONException {
            ArrayList<Presenter> presenters = new ArrayList();
            for (int i = 0; i < jsonPresenters.length(); i++) {

                JSONObject presenterObject = jsonPresenters.getJSONObject(i);

                String presenterName = presenterObject.getString("name");
                long enrollement = presenterObject.getLong("enrollment");


                presenters.add(new Presenter(presenterName, enrollement));


            }
            return presenters;
        }


        private ArrayList<Evaluation> getRebuiltEvaluations(JSONArray jsonEvaluations) throws JSONException {
            ArrayList<Evaluation> evaluations = new ArrayList();
            for (int i = 0; i < jsonEvaluations.length(); i++) {

                JSONObject evaluationObject = jsonEvaluations.getJSONObject(i);


                long evaluationTime = evaluationObject.getLong("evaluationTime");

                JSONArray jsonRatings = evaluationObject.getJSONArray("criterionRating");
                float[] criterionRating = getRebuiltEvaluationsRatings(jsonRatings);

                JSONObject jsonCriteria = evaluationObject.getJSONObject("criteria");
                EvaluationCriteria criteria = getRebuiltEvaluationsRatings(jsonCriteria);


                JSONObject jsonEvaluator = evaluationObject.getJSONObject("evaluator");
                Evaluator evaluator = new Evaluator(
                        jsonEvaluator.getString("name")
                );


                Evaluation rebuiltEvaluation = new Evaluation(evaluator, criteria, criterionRating);

                evaluations.add(rebuiltEvaluation);

            }
            return evaluations;
        }

        private EvaluationCriteria getRebuiltEvaluationsRatings(JSONObject jsonCriteria) throws JSONException {
            EvaluationCriteria criteria = new EvaluationCriteria(jsonCriteria.getBoolean("weighted"));

            JSONArray jsonCriterionNames = jsonCriteria.getJSONArray("criterionNames");
            JSONArray jsonCriterionWeights = jsonCriteria.getJSONArray("criteriaWeights");

            for (int i = 0; i < jsonCriterionNames.length(); i++) {
                criteria.addCriterion(
                        jsonCriterionNames.getString(i),
                        (float) jsonCriterionWeights.getDouble(i)
                );
            }

            return criteria;
        }

        private float[] getRebuiltEvaluationsRatings(JSONArray jsonRatings) throws JSONException {
            float ratings[] = new float[jsonRatings.length()];
            for (int i = 0; i < jsonRatings.length(); i++) {
                ratings[i] = (float) jsonRatings.getDouble(i);
            }
            return ratings;
        }

    }


}