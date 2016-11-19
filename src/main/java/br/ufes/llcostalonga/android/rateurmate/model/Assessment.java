package br.ufes.llcostalonga.android.rateurmate.model;

import java.util.ArrayList;

/**
 * Created by LeandroHD on 22/09/16.
 */
public class Assessment {

    private String topic;
    private String summary;

    private float evaluatorMaxRating;
    private float audiencerMaxRating;
    private float minRatingIncrement;

    private EvaluationCriteria evaluationCriteria;

    private ArrayList<Evaluator> evaluators;
    
    private ArrayList<Presentation> presentations;

    private ArrayList audienceRanking;

 
    public Assessment(String topic, Evaluator evaluator) {
        initDefaultValues(); //must be first line

        this.topic = topic;

        evaluators.add(evaluator);
    }

    public Assessment(String topic, Evaluator evaluator, EvaluationCriteria evaluationCriteria) {
        initDefaultValues(); //must be first line

        this.topic = topic;
        
        evaluators.add(evaluator);
        
        this.evaluationCriteria = evaluationCriteria;

    }

   
    private void initDefaultValues() {
        evaluatorMaxRating = 5.0f;
        audiencerMaxRating = 5.0f;
        minRatingIncrement = 0.5f;

        evaluators = new ArrayList();
        presentations = new ArrayList();
        audienceRanking = new ArrayList();
    }

    
    public boolean isEvaluator(Evaluator evaluator){
        return evaluators.contains(evaluator);
    }
    public EvaluationCriteria getEvaluationCriteria() {
        return evaluationCriteria;
    }

    public void setEvaluationCriteria(EvaluationCriteria stablishedEvaluationCriteria) {
        this.evaluationCriteria = stablishedEvaluationCriteria;
    }

    public String getTopic() {
        return topic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void addPresentation(Presentation presentation) {
        presentations.add(presentation);

    }

    public Presentation getPresentation(int index) {

        return (Presentation) (getPresentations().get(index));
    }

    public ArrayList<Presentation> getPresentations() {

        calculateAveragedAudienceRating();

        return presentations;
    }

    private void calculateAveragedAudienceRating() {
        int numAudienceEntries = audienceRanking.size() == 0 ? 1 : audienceRanking.size();
        for (int i = 0; i < presentations.size(); i++) {
            float averagedAudienceRating
                    = ((Presentation) presentations.get(i)).audienceRating / numAudienceEntries;

            ((Presentation) presentations.get(i)).setAudienceRating(averagedAudienceRating);

        }
    }

    public void registerAudienceRanking(AudienceMember audienceMember, int[] ordering) {
        audienceRanking.add(ordering);

        float stepFactor = 1.0f / (presentations.size() - 1);

        for (int i = 0; i < ordering.length; i++) {

            ((Presentation) presentations.get(i)).audienceRating += (ordering[i] * stepFactor);

        }

    }

}
