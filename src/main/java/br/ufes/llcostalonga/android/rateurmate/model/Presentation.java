package br.ufes.llcostalonga.android.rateurmate.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Presentation {

   private String title;
   private String date;

   private ArrayList<Presenter> presenters;

  
   private ArrayList<Evaluation> evaluations ;

    public ArrayList<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(ArrayList<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    protected float evaluatorRating = 0;
   protected float audienceRating = 0; // repensar

    public Presentation(String title, Presenter presenter) {
        this.title = title;
        presenters = new ArrayList();
        presenters.add(presenter);
        evaluations = new ArrayList();
    }

    public Presentation(String title, ArrayList<Presenter> presenters) {
        this.title = title;
        this.presenters = presenters;
        evaluations = new ArrayList();
    }

    public Presentation(String title, ArrayList<Presenter> presenters, String date) {
        this.title = title;
        this.presenters = presenters;
        evaluations = new ArrayList();
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public ArrayList getPresenters() {
        return presenters;
    }

    public void addEvaluation(Evaluator evaluator, Evaluation evaluation) {
         
        evaluations.add(evaluation);

        adjustEvaluatorRating();

    }

    private void adjustEvaluatorRating() {
        float rating = 0;

        for (Iterator iterator = evaluations.iterator(); iterator.hasNext();) {
            Object next = iterator.next();

            rating += ((Evaluation) next).getRating();

        }

        rating = rating / evaluations.size();

        evaluatorRating = rating;
    }

    public Evaluation getEvaluation(Evaluator evaluator) {
    
        for (Iterator<Evaluation> iterator = evaluations.iterator(); iterator.hasNext();) {
            Evaluation evaluation = iterator.next();
            
            if (evaluation.getEvaluator() == evaluator){
                return evaluation;
            }
            
        }
        return null;

    }



    public float getEvaluatorRating() {
        adjustEvaluatorRating();
        return evaluatorRating;
    }

    public void setAudienceRating(float rating) {
        audienceRating = rating;
    }

   /* @Override
    public String toString() {

        String returnString
                = "Presentation{" + "title=" + title + ", date=" + date + ", presenters=";

        for (int i = 0; i < presenters.size(); i++) {
            returnString += (Presenter) presenters.get(i);

        }
*
       
        //Avaliações
        Collection values = evaluations.values();
        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            Object next = iterator.next();

           returnString += "\n      "+ ((Evaluation) next);

        }
        
        returnString += "\n";
        
        
        returnString += ", evaluatorRating=" + evaluatorRating + ", audienceRating=" + audienceRating + '}';
        return returnString;
    }*/

}
