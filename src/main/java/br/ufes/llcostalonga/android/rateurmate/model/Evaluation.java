package br.ufes.llcostalonga.android.rateurmate.model;

import java.io.Serializable;

/**
 * Created by LeandroHD on 22/09/16.
 */
public class Evaluation implements Serializable{

    private long evaluationTime;

    private EvaluationCriteria criteria;

    private Evaluator evaluator;

    float[] criterionRating;



    /**
     * Used when an a single rate is enough, in other words, the rating is not
     * composed by different criteria (no evaluation criteria really)
     *
     * @param evaluator
     * @param rating

     */
    public Evaluation(Evaluator evaluator, float rating) {
        this.evaluator = evaluator;

        this.criterionRating = new float[]{rating};

        evaluationTime = System.currentTimeMillis();
    }

    public Evaluation(Evaluator evaluator, EvaluationCriteria criteria, float[] criterionRating) {
        this.evaluator = evaluator;
        this.criteria = criteria;
        this.criterionRating = criterionRating;
        evaluationTime = System.currentTimeMillis();
    }


    public void updateRating(float rating, int criterionIndex){
        //Esse método foi feito nas coxas..inseguro.
        criterionRating[criterionIndex] = rating;
    }

    public float[] getRatings() {
        return criterionRating;
    }


    public float getRating() {

        if (criteria != null) {
            float sum = 0;

            if (criteria.isWeighted()) {
                float[] weights = criteria.getCriteriaWeights();
                for (int i = 0; i < weights.length; i++) {
                    sum += (weights[i] * criterionRating[i]);
                }
            } else {
                for (int i = 0; i < criterionRating.length; i++) {
                    sum += criterionRating[i];
                }
                sum/=criterionRating.length;
            }

            return sum;
        }
        return criterionRating[0];
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public static float[] getSampleRating(EvaluationCriteria evaluationCriteria) {

       
        int qtCriterion = evaluationCriteria!= null?evaluationCriteria.size():1;
        float[] sampleRating = new float[qtCriterion];

        for (int i = 0; i < qtCriterion; i++) {
            sampleRating[i] = (float) ((int) (Math.random() * 10) * 0.5);

        }

        return sampleRating;
      
    }

    public EvaluationCriteria getCriteria() {
        return criteria;
    }
   /* public String toString() {
        String returnString =  "Evaluation{" + "evaluationTime=" + evaluationTime
                + ", evaluator=" + evaluator + ", ";

        if (criteria  !=null){
             String[] criterionNames = criteria.getCriterionNames();
             float[] critetionWeights = criteria.getCriteriaWeights();
             
            for (int i = 0; i < critetionWeights.length; i++) {
           
                returnString+= criterionNames[i] + "(weight " + critetionWeights[i] + "): " 
                        + criterionRating[i] + "\n";           
            }   
            
        }else {
           returnString+=  "rating <no-criteria> : " + criterionRating[0];
        }

       return returnString;
    }*/

}
