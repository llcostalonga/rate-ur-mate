package br.ufes.llcostalonga.android.rateurmate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import br.ufes.llcostalonga.android.rateurmate.data.RateUrMateContract.PresentationEntry;


/**
 * A placeholder fragment containing a simple view.
 */
public class PresentationActivityFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int PRESENTATION_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] PRESENTATION_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            PresentationEntry.TABLE_NAME + "." + PresentationEntry._ID,
            PresentationEntry.COLUMN_ASSESSEMENT_KEY,
            PresentationEntry.COLUMN_TITLE,
            PresentationEntry.COLUMN_DATE
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_PRESENTATION_ID = 0;
    static final int COLUMN_ASSESSEMENT_KEY = 1;
    static final int COLUMN_TITLE = 2;
    static final int COLUMN_DATE = 3;


    private PresentationAdapter mPresentationAdapter;



    public PresentationActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_presentation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            getLoaderManager().restartLoader(PRESENTATION_LOADER, null, this);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPresentationAdapter = new PresentationAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_presentation, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listviewPresentations);
        listView.setAdapter(mPresentationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                   // String locationSetting = Utility.getPreferredLocation(getActivity());
                    //
                    Intent intent = new Intent(getActivity(), EvaluationActivity.class);

                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PRESENTATION_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(), //TODO Passo X: Os dados (URI) passados da main
                PRESENTATION_COLUMNS,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
       mPresentationAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPresentationAdapter.swapCursor(null);
    }



}
