package br.ufes.llcostalonga.android.rateurmate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import br.ufes.llcostalonga.android.rateurmate.data.FetchDataTask;
import br.ufes.llcostalonga.android.rateurmate.data.RateUrMateContract;
import br.ufes.llcostalonga.android.rateurmate.data.RateUrMateContract.AssessementEntry;


/**
 * A placeholder fragment containing a simple view.
 */
public class AssessmentActivityFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int ASSESSEMENT_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] ASSESSEMENT_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            AssessementEntry.TABLE_NAME + "." + AssessementEntry._ID,
            AssessementEntry.COLUMN_TOPIC,
            AssessementEntry.COLUMN_SUMMARY,
            AssessementEntry.COLUMN_EVALUATOR_MAX_RATING,
            AssessementEntry.COLUMN_AUNDIENCE_MAX_RATING,
            AssessementEntry.COLUMN_RATING_STEP
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_ASSESSEMENT_ID = 0;
    static final int COLUMN_TOPIC = 1;
    static final int COLUMN_SUMMARY = 2;
    static final int COLUMN_EVALUATOR_MAX_RATING = 3;
    static final int COLUMN_AUNDIENCE_MAX_RATING = 4;
    static final int COLUMN_RATING_STEP = 5;


    private AssessementAdapter mAssessementAdapter;


    public AssessmentActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_assessment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAssessementAdapter = new AssessementAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_assessment, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listviewAssessements);
        listView.setAdapter(mAssessementAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                   // String locationSetting = Utility.getPreferredLocation(getActivity());
                    //
                    Intent intent = new Intent(getActivity(), PresentationActivity.class)
                            .setData(RateUrMateContract.PresentationEntry.buildPresentationAssessement(
                                    cursor.getLong(COL_ASSESSEMENT_ID)
                            ));
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ASSESSEMENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri assessementUri = AssessementEntry.buildAssessementUri();


        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                assessementUri,
                ASSESSEMENT_COLUMNS,
                null,
                null,
                null);

        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
       mAssessementAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAssessementAdapter.swapCursor(null);
    }

    // since we read the location when we create the loader, all we need to do is restart things
    void onDataChanged( ) { //TODO User change talvez? Não sei se vai servir..
        updateData();
        getLoaderManager().restartLoader(ASSESSEMENT_LOADER, null, this);
    }

     void updateData() {

         FetchDataTask fetchDataTask = new FetchDataTask(getActivity());
         fetchDataTask.execute();

        getLoaderManager().restartLoader(ASSESSEMENT_LOADER, null, this);
    }


}
