package br.ufes.llcostalonga.android.rateurmate;



import java.util.ArrayList;

import org.boon.json.*;

import br.ufes.llcostalonga.android.rateurmate.model.*;

public class FakeAssessmentDataParser {

   public  ArrayList<Presentation> getPresentations(String jsonString){

       ObjectMapper mapper = JsonFactory.create();

      // Assessment assessment = mapper.fromJson(jsonString, Assessment.class);


      /* AssessementFake assessmentFake =  mapper.fromJson(jsonString, AssessementFake.class);



       ArrayList<Presentation> presentations = new ArrayList();
       for (int i = 0; i < assessmentFake.presentationTitles.length; i++) {
           Presentation p = new Presentation(assessmentFake.presentationTitles[i],
                  new Presenter(assessmentFake.presentationPresenters[i],123L));

          Evaluation evaluation = new Evaluation(new Evaluator("Leandro"),
                  assessmentFake.presentationRatings[i]);

           p.addEvaluation(new Evaluator("Leandro"),evaluation );

           presentations.add(p);

       }*/


       ArrayList<Presentation> presentations = new ArrayList();
       for (int i = 0; i < 10; i++) {
           Presentation p = new Presentation("JSon Title " + i,
                   new Presenter("Json Presenter " + 1,123L));

           Evaluation evaluation = new Evaluation(new Evaluator("Leandro"),
                   (float)(Math.random() * 5));

           p.addEvaluation(new Evaluator("Leandro"),evaluation );

           presentations.add(p);

       }

       return presentations;


   }

}
