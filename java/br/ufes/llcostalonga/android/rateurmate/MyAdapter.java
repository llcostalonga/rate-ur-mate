package br.ufes.llcostalonga.android.rateurmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufes.llcostalonga.android.rateurmate.model.Presentation;
import br.ufes.llcostalonga.android.rateurmate.R;

public class MyAdapter extends ArrayAdapter<Presentation> {

    ArrayList<Presentation> presentations = new ArrayList<>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList<Presentation> objects) {
        super(context, textViewResourceId, objects);
        presentations = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.presentation_listview_item, null);
        TextView textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
        TextView textViewSpeaker = (TextView) v.findViewById(R.id.textViewSpeakers);
        RatingBar ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);

        textViewTitle.setText(presentations.get(position).getTitle());

        ArrayList speakers = presentations.get(position).getPresenters();
        String formatedSpeakers = "";
        for (int i = 0; i < speakers.size(); i++) {
            formatedSpeakers+=speakers.get(i) + " ";
        }

        textViewSpeaker.setText(formatedSpeakers);
        ratingBar.setNumStars(5);

        ratingBar.setRating(presentations.get(position).getEvaluatorRating());


        return v;

    }

}