/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufes.llcostalonga.android.rateurmate.model;

/**
 *
 * @author LeandroHD
 */
public class AssessementFake {
    public  String[]  presentationTitles;
    public String[] presentationPresenters;
    public float[] presentationRatings;
    
    
    public  AssessementFake(){
         presentationTitles = new String[] { 
          "Apresentacão 1" ,
          "Apresentacão 2", 
          "Apresentacão 3"
        
          };
         presentationPresenters = new String[]{
             "Fulano",
             "Ciclano",
             "Beltrano"
         };
         
         presentationRatings = new float[] {
           5f,
           4.5f,
           2.5f
         };
    }
    
    
    
}
