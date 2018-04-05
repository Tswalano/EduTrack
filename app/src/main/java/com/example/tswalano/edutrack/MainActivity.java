package com.example.tswalano.edutrack;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView txtName, txtGrade;
    private ProgressDialog dialog;
    private ListView listView;

    //    URL
    private static String url = "http://192.168.42.128:8080/api/student/1";
    String name, surname;

    ArrayList<HashMap<String, String>> subjectList;

    List<String> subject_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjectList = new ArrayList<>();


        txtName = findViewById(R.id.names);
        txtGrade = findViewById(R.id.grade);
        listView = findViewById(R.id.listView);

        new getStudent().execute();
    }

    class getStudent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler handler = new HttpHandler();
//            Make a request to url and getting response
            String jsonString = handler.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonString);

            if (jsonString != null) {
                try {
//                    Getting through array node
                    JSONObject jsonObject = new JSONObject(jsonString);
                    name = jsonObject.getString("firstname");
                    surname = jsonObject.getString("surname");
//                    String mark = jsonObject.getString("mark");

//                    Getting JSON array node
                    JSONObject mark = jsonObject.getJSONObject("mark");
                    String subject1 = mark.getJSONObject("subject").getString("subjectOne");
                    String subject2 = mark.getJSONObject("subject").getString("subjectTwo");
                    String subject3 = mark.getJSONObject("subject").getString("subjectThree");
                    String subject4 = mark.getJSONObject("subject").getString("subjectFour");
                    String subject5 = mark.getJSONObject("subject").getString("subjectFive");
                    String subject6 = mark.getJSONObject("subject").getString("subjectSix");

                    // Initialize an array of animals
                    String[] mySubjects = new String[]{
                            subject1,
                            subject2,
                            subject3,
                            subject4,
                            subject5,
                            subject6,
                    };

//                  Create a list from string array elements
                    subject_list = new ArrayList<String>(Arrays.asList(mySubjects));

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            dismiss dialog
            if(dialog.isShowing()){
                dialog.dismiss();
            }

//            ListAdapter adapter = new SimpleAdapter(
//                    MainActivity.this, subjectList,
//                    R.layout.list_items, new String[]{"sub1", "mark"},
//                    new int[]{R.id.sub, R.id.mark}
//            );
//
//            listView.setAdapter(adapter);
//            txtName.setText(name + " " + surname);

            // Initialize an ArrayAdapter object from the list
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    MainActivity.this,android.R.layout.simple_list_item_1,
                    subject_list
            );

            // Populate the ListView widget with ArrayAdapter
            listView.setAdapter(adapter);

            Toast.makeText(MainActivity.this, "Results: " + name, Toast.LENGTH_SHORT).show();
        }

    }
}
