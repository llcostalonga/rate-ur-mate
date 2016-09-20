package br.ufes.llcostalonga.android.rateurmate.parte1_listview;


import java.util.ArrayList;

public class Presentation {

    String title;
    String date;
    ArrayList<String> speakers;



    public Presentation(String title, String date) {
        this.title = title;
        this.date = date;
        speakers = new ArrayList();
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setSpeakers(ArrayList speakers) {
        this.speakers = speakers;
    }



    public void addSpeaker (String speakerName){
        speakers.add(speakerName);
    }

    public String getTitle() {

        return title;
    }

    public String getDate() {
        return date;
    }

    public ArrayList getSpeakers() {
        return speakers;
    }
}
