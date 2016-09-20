package br.ufes.llcostalonga.android.rateurmate.parte1_listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList presentations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPresentations();


      ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.presentation_listview_item,
                R.id.textViewSpeakers,
                presentations
        );



      /*MyAdapter adapter = new  MyAdapter(
                this,
                R.layout.presentation_listview_item,
                presentations
        );*/


        ListView listView =  (ListView)findViewById(R.id.listviewPresentations);
        listView.setAdapter(adapter);


    }



    private void loadPresentations(){
        presentations = new ArrayList();

        for (int i = 0; i < 100; i++) {
            Presentation p = new Presentation("Apresentação " + i, "01/01/2016");
            p.addSpeaker("Aluno " + (int) (Math.random() * 100));
            p.addSpeaker("Aluno " + (int) (Math.random() * 100));
            p.addSpeaker("Aluno " + (int) (Math.random() * 100));

            presentations.add(p);

        }

    }




}
