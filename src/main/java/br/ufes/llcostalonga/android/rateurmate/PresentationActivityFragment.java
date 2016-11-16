package br.ufes.llcostalonga.android.rateurmate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.ufes.llcostalonga.android.rateurmate.model.Evaluation;
import br.ufes.llcostalonga.android.rateurmate.model.Presentation;

/**
 * A placeholder fragment containing a simple view.
 */
public class PresentationActivityFragment extends Fragment {

    public PresentationActivityFragment() {
    }

    ArrayList presentations;
    PresentationAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_presentation, container, false);
        //return inflater.inflate(R.layout.fragment_main, container, false);

        //Não sei se aqui funciona
        loadPresentations();

        adapter = new PresentationAdapter(
                getActivity(),
                R.layout.presentation_listview_item,
                presentations
        );



        ListView listView = (ListView) rootView.findViewById(R.id.listviewPresentations);
        listView.setAdapter(adapter);

        //@todo Passo 2: Reacao ao clique to listview (toast)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Presentation presentation = adapter.getItem(position);

                //@todo Passo 4: criar intent DetailActivity
                Intent intent = new Intent(adapter.getContext(), EvaluationActivity.class);

                //todo acertar esse código para aceitar multiplas avaliacao
                Evaluation evaluation = presentation.getEvaluations().get(0);
                // Evaluation evaluation = presentation.getEvaluation((Evaluator) defaultUser);
                //passingEvaluation = evaluation;

                if (evaluation != null) {
                    // intent.putExtra("ratings", evaluation.getRatings());
                    intent.putExtra("Evaluation",evaluation);

                }

                startActivity(intent);

            }
        });

        return rootView;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.assessment, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {

            loadPresentations();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPresentations();
    }

    private void loadPresentations() {

        if(presentations == null) {
            presentations = new ArrayList();

            FetchData fetch = new FetchData();

            fetch.execute();

        }

    }

    public class FetchData extends AsyncTask<Void, Void, ArrayList<Presentation>> {


        protected ArrayList<Presentation> doInBackground(Void... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            ArrayList<Presentation> returnPresentations = null;


            try {

                //URL url = new URL("https://raw.githubusercontent.com/llcostalonga/rate-ur-mate/Lesson-2/fakeAssessment.jason");

                URL url = new URL("https://raw.githubusercontent.com/llcostalonga/rate-ur-mate/Lesson-2/assessmentJsonFile.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();

                Log.v("Assessment", "json" + jsonStr);


            } catch (IOException e) {
                Log.e("Assessment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Assessment", "Error closing stream", e);
                    }
                }
            }

            //FakeAssessmentDataParser parser = new FakeAssessmentDataParser();
            AssessmentDataParser parser = new AssessmentDataParser();
            try {

                returnPresentations =  parser.getPresentations(jsonStr);

            } catch (Exception e) {
                Log.e("Assessment", "Error closing stream", e);
            }

            return returnPresentations;
        }

        @Override
        protected void onPostExecute(ArrayList<Presentation> result) {
            super.onPostExecute(result);

            if (result != null) {
                adapter.clear();
                for (Presentation presentation : result) {
                    adapter.add(presentation);
                }
            }
        }
    }


}
