package br.ufes.llcostalonga.android.rateurmate.model;

import java.io.Serializable;

/**
 * Created by LeandroHD on 22/09/16.
 */
public class Evaluator extends AudienceMember implements Serializable {

   private String name;
   private String organization;

    public Evaluator(String name) {
        this.name = name;

    }

    public Evaluator(String name, String organization) {
        this.name = name;
        this.organization = organization;
    }

    public void evaluate(Presentation presentation, float rating) {

        Evaluation evaluation = new Evaluation(this, rating);
        presentation.addEvaluation(this, evaluation);

    }

    public void evaluate(Presentation presentation, EvaluationCriteria criteria, float[] ratings) {

        Evaluation evaluation = new Evaluation(this, criteria, ratings);
        presentation.addEvaluation(this, evaluation);

    }

}
