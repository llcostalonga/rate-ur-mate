package br.ufes.llcostalonga.android.rateurmate;

import android.text.format.Time;
import android.util.Log;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import br.ufes.llcostalonga.android.rateurmate.model.Assessment;
import br.ufes.llcostalonga.android.rateurmate.model.Evaluation;
import br.ufes.llcostalonga.android.rateurmate.model.Evaluator;
import br.ufes.llcostalonga.android.rateurmate.model.Presentation;
import br.ufes.llcostalonga.android.rateurmate.model.Presenter;

/**
 * Created by LeandroHD on 30/09/16.
 */
public class AssessmentDataParser {


    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public Assessment getAssessmentDataFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        JSONArray presenters = forecastJson.getJSONArray("presenters");




        return null;

    }


    public ArrayList<Presentation> getPresentations(String jsonString){

        ArrayList<Presentation> presentations = new ArrayList();

        try {


            JSONObject jsonAssessment = new JSONObject(jsonString);

            JSONArray jsonPresentations = jsonAssessment.getJSONArray("presentations");

            for(int i = 0; i < jsonPresentations.length(); i++) {

               JSONObject presentationObject = jsonPresentations.getJSONObject(i);


                String title = (String)presentationObject.get("title");
                JSONArray jsonPresenters = presentationObject.getJSONArray("presenters");

                ArrayList<Presenter> presenters = new ArrayList();

                for(int j = 0; j < jsonPresenters.length(); j++) {

                    JSONObject presenterObject = jsonPresenters.getJSONObject(i);

                    String presenterName = presenterObject.getString("name");
                    long enrollement = presenterObject.getLong("enrollment");


                    presenters.add(new Presenter(presenterName,enrollement ));

                }


                presentations.add(new Presentation(
                        (String) presentationObject.get("title"),
                         presenters));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return presentations;

    }
}
