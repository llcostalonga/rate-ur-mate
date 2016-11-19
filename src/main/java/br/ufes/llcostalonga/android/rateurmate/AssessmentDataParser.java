package br.ufes.llcostalonga.android.rateurmate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.ufes.llcostalonga.android.rateurmate.model.Assessment;
import br.ufes.llcostalonga.android.rateurmate.model.Evaluation;
import br.ufes.llcostalonga.android.rateurmate.model.EvaluationCriteria;
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

       //todo falta recriar o assessment


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

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return presentations;

    }


    private ArrayList<Presenter> getRebuiltPresenters(JSONArray jsonPresenters) throws JSONException {
        ArrayList<Presenter> presenters = new ArrayList();
        for(int i = 0; i < jsonPresenters.length(); i++) {

            JSONObject presenterObject = jsonPresenters.getJSONObject(i);

            String presenterName = presenterObject.getString("name");
            long enrollement = presenterObject.getLong("enrollment");


            presenters.add(new Presenter(presenterName,enrollement ));

        }
        return presenters;
    }


    private ArrayList<Evaluation> getRebuiltEvaluations(JSONArray jsonEvaluations) throws JSONException {
        ArrayList<Evaluation> evaluations = new ArrayList();
        for(int i = 0; i < jsonEvaluations.length(); i++) {

            JSONObject evaluationObject = jsonEvaluations.getJSONObject(i);


            long evaluationTime = evaluationObject.getLong("evaluationTime");

            JSONArray jsonRatings =  evaluationObject.getJSONArray("criterionRating");
            float[] criterionRating =  getRebuiltEvaluationsRatings(jsonRatings);

            JSONObject jsonCriteria = evaluationObject.getJSONObject("criteria");
            EvaluationCriteria criteria = getRebuiltEvaluationsRatings (jsonCriteria);


            JSONObject jsonEvaluator = evaluationObject.getJSONObject("evaluator");
            Evaluator evaluator = new Evaluator(
                    jsonEvaluator.getString("name")
            );


            Evaluation rebuiltEvaluation = new Evaluation(evaluator,criteria,criterionRating);

            evaluations.add(rebuiltEvaluation);

        }
        return evaluations;
    }

    private EvaluationCriteria getRebuiltEvaluationsRatings (JSONObject jsonCriteria) throws JSONException {
        EvaluationCriteria criteria = new EvaluationCriteria(jsonCriteria.getBoolean("weighted"));

        JSONArray jsonCriterionNames = jsonCriteria.getJSONArray("criterionNames");
        JSONArray jsonCriterionWeights = jsonCriteria.getJSONArray("criteriaWeights");

        for (int i = 0; i < jsonCriterionNames.length() ; i++) {
            criteria.addCriterion(
                    jsonCriterionNames.getString(i),
                    (float)jsonCriterionWeights.getDouble(i)
            );
        }

        return criteria;
    }

    private float[] getRebuiltEvaluationsRatings (JSONArray jsonRatings) throws JSONException {
        float ratings[] = new float[jsonRatings.length()];
        for(int i = 0; i < jsonRatings.length(); i++) {
            ratings[i] = (float)jsonRatings.getDouble(i);
        }
        return ratings;
    }

}
