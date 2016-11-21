package br.ufes.llcostalonga.android.rateurmate;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


//TODO Passo 1: Copiar o código do GitHub
public class PresentationAdapter extends CursorAdapter {

    public PresentationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //TODO: ver como fica a amarracao o itemXML a listView  e Cursor. Explicar em aula.
        View view = LayoutInflater.from(context).inflate(R.layout.presentation_listview_item, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView tv = (TextView)view;
        String title = cursor.getString(PresentationActivityFragment.COLUMN_TITLE);
        tv.setText(title);



/*        TextView textViewTopic = (TextView) view.findViewById(R.id.textViewTopic);
        TextView textSummary = (TextView) view.findViewById(R.id.textViewSummary);



        String topic = cursor.getString(AssessmentActivityFragment.COLUMN_TOPIC);
        textViewTopic.setText(topic); //  cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),

        String summary = cursor.getString(AssessmentActivityFragment.COLUMN_SUMMARY);
        textSummary.setText(topic);*/


    }
}