package br.ufes.llcostalonga.android.rateurmate.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author LeandroHD
 */
public class EvaluationCriteria extends ArrayList<Object> implements Serializable {
    

    private boolean weighted;

    public EvaluationCriteria(boolean weighted) {
  
        this.weighted = weighted;
    }
    
   
    public void addCriterion(String criterion) {
        addCriterion(criterion,0.0f);
    }
    
    public void addCriterion(String criterion, float weight) {
      add(new Criterion(criterion,weight));
    }

    
    
    public String[] getCriterionNames(){
        String[] criterions = new String[size()];
        for (int i = 0; i < size(); i++) {
            criterions[i] = ((Criterion)get(i)).criterion;
        }
        return criterions; 
    }
    public float[] getCriteriaWeights(){
        float[] weights = new float[size()];
        for (int i = 0; i < size(); i++) {
            weights[i] = ((Criterion)get(i)).weight;
        }
        return weights;
        
    }
    
    
    public boolean isWeighted(){
        return weighted;
    }
    
     public static EvaluationCriteria getDefaultEvaluationCriteria(){
        EvaluationCriteria criteria = new EvaluationCriteria(false);
       
        criteria.addCriterion("Accuracy");
        criteria.addCriterion("Depth");
        criteria.addCriterion("Originality");
        criteria.addCriterion("Relevance");
        criteria.addCriterion("Succinctness");
        
        return criteria;

    }

   /* @Override
    public String toString() {
        String returnString = "EvaluationCriteria{ weighted=" + weighted ;
        
        for (Iterator iterator = iterator(); iterator.hasNext();) {
            Criterion next = (Criterion)iterator.next();
            returnString+=  next.criterion + " (weight = " + next.weight + ")";
        }
        
    
    return returnString + '}';
    }*/
      
    
    class Criterion implements Serializable {
       private String criterion;
       private float weight;

        public Criterion(String criterion, float weight) {
            this.criterion = criterion;
            this.weight = weight;
        }
             
        
    }
     
    
}
