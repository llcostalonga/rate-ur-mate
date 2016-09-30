package br.ufes.llcostalonga.android.rateurmate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.ufes.llcostalonga.android.rateurmate.model.*;


public class MainActivity extends AppCompatActivity {

    ArrayList presentations;
    MyAdapter adapter;




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_refresh){
            FetchData fetch = new FetchData();

            fetch.execute(); //city name

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.assessment, menu);
        return true;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPresentations();


     /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.presentation_listview_item,
                R.id.textViewSpeakers,
                presentations
        );*/



       adapter = new  MyAdapter(
                this,
                R.layout.presentation_listview_item,
                presentations
        );


        ListView listView =  (ListView)findViewById(R.id.listviewPresentations);
        listView.setAdapter(adapter);


    }



    private void loadPresentations(){




        presentations = new ArrayList();

        for (int i = 0; i < 100; i++) {


            Presentation p = new Presentation("Apresentação " ,
                    new Presenter(("Aluno " + (int) (Math.random() * 100)),
                            (long)(Math.random()) * 1000));


            //p.addSpeaker("Aluno " + (int) (Math.random() * 100));
            //p.addSpeaker("Aluno " + (int) (Math.random() * 100));

            presentations.add(p);

        }

    }

    public class FetchData extends AsyncTask<Void,Void, ArrayList<Presentation>> {


        protected ArrayList<Presentation> doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;



            try {




                //URL url = new URL("https://raw.githubusercontent.com/llcostalonga/rate-ur-mate/Lesson-2/fakeAssessment.jason");

                URL url = new URL("https://raw.githubusercontent.com/llcostalonga/rate-ur-mate/Lesson-2/assessmentJsonFile.json");

                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=74dee42ab7b3d4b4a8a4e62d691a1d3b");


                // Create the request to OpenWeatherMap, and open the connection
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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v("Assessment", "json" + forecastJsonStr);





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



           // FakeAssessmentDataParser parser = new FakeAssessmentDataParser();
            AssessmentDataParser parser = new AssessmentDataParser();
            try {

                return parser.getPresentations(forecastJsonStr);

            } catch (Exception e) {
                Log.e("Assessment", "Error closing stream", e);
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Presentation> result) {
            super.onPostExecute(result);

            if(result !=null){
                adapter.clear();
                for (Presentation presentation :  result){
                    adapter.add(presentation);
                }
            }
        }
    }


}
