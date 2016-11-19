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
public class Presenter extends AudienceMember {
    
   private String name;
   private Long enrollment;

    public Presenter(String name, Long enrollment) {
        this.name = name;
        this.enrollment = enrollment;
    }

    @Override
    public String toString() {
        return "Presenter{" + "name=" + name + ", enrollment=" + enrollment + '}';
    }
    
    
    public void addPresentation(Assessment assessment, Presentation presentation){
        assessment.addPresentation(presentation);
        
    }
    
    
    
}
