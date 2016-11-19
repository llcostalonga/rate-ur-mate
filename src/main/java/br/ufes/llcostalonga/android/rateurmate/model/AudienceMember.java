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
public abstract class AudienceMember {
    
  public void registerPreferedRanking(Assessment assessment, int[] ordering){
      assessment.registerAudienceRanking(this, ordering);
  } 
    
    
}
