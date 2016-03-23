package com.example.andrei.weader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected","yes");
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container,false);

        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
        };

        List<String> weekForecast = new ArrayList<String>(
                Arrays.asList(forecastArray)
        );

        ArrayAdapter<String> mForecastAdapter;
        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast
        );

        // Get a reference to the ListView, and attach this adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);



        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<Void, Void, Void>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        protected Void doInBackground(Void... params) {
            // These two need to be declared outside the
            // try/catch so that they can be closed in the finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader           = null;

            // Will contain the raw JSON response as a string
            String forecastJsonStr = null;

            try {
                // Construct the URL dor the OpenWeatherMap query
                // Possible parameters are avaiable at OWM;s forecast API page
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=cfc35592d810d393b1483ff4f4803f2c");
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    //Nothing to do
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing
                    // But it does make debuggin a lot easier if you print outthe completed buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0 ) {
                    //Stream was empty. No point in parsing
                    forecastJsonStr = null;
                }

                forecastJsonStr = buffer.toString();
                //todo: 76
                Log.e(LOG_TAG, "Forecast JSON STRNG " + forecastJsonStr);

            } catch (java.io.IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                //If the code didn;t successfuly get the weather data, there's no point in attemping to parse
                forecastJsonStr = null;
//            e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    try {
                        reader.close();
                    } catch (final java.io.IOException e) {
                        Log.e("PlaceholderFragment", "Error ", e);
                    }
                }
            }

            return null;
        }
    }
}
