package br.ufes.llcostalonga.android.rateurmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import br.ufes.llcostalonga.android.rateurmate.model.Evaluation;

public class EvaluationActivity extends AppCompatActivity {

    Evaluation evaluation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Meu código
        Intent intent = getIntent();

        evaluation = (Evaluation) intent.getSerializableExtra("Evaluation");

        // evaluation = MainActivity.passingEvaluation;

        EvaluationAdapter criteriaAdapter = new EvaluationAdapter(
                this,
                R.layout.criteria_listview_item,
                evaluation
        );


        ListView listView = (ListView) findViewById(R.id.listviewCriteria);
        listView.setAdapter(criteriaAdapter);



     //Esse código até funciona, mas sobrescreve o comportamento padrão da action bar.

      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"your icon was clicked",Toast.LENGTH_SHORT).show();
            }
        });*/


        //Esse código não funciona porque rating bar não é acessível daqui

       /* RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingbar_criterion);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast toast = Toast.makeText(getApplicationContext(), "Overall rating: " + evaluation.getRating(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(),
                Float.toString(evaluation.getRating()),
                Toast.LENGTH_SHORT).show();


    }

}
