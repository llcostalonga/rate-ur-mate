package br.ufes.llcostalonga.android.rateurmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import br.ufes.llcostalonga.android.rateurmate.model.Evaluation;
import br.ufes.llcostalonga.android.rateurmate.model.EvaluationCriteria;

//todo Passo 6: rename from MyAdapter to Presentation Adapter
public class EvaluationAdapter extends ArrayAdapter {

    Evaluation evaluation;

    public EvaluationAdapter(Context context, int textViewResourceId, Evaluation evaluation) {
        super(context, textViewResourceId, evaluation.getCriteria().getCriterionNames());
        this.evaluation = evaluation;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.criteria_listview_item, null);

        TextView textViewCriterion = (TextView) v.findViewById(R.id.textview_evaluation_criteria);
        RatingBar ratingBar = (RatingBar)v.findViewById(R.id.ratingbar_criterion);


        EvaluationCriteria criteria = evaluation.getCriteria();


        textViewCriterion.setText(criteria.getCriterionNames()[position]);


        ratingBar.setRating(evaluation.getRatings()[position]);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                evaluation.updateRating(rating,position);

                Toast toast = Toast.makeText(getContext(), "Overall rating: " + evaluation.getRating(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        return v;

    }

}