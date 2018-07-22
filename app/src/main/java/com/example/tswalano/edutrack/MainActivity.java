package com.example.tswalano.edutrack;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tswalano.edutrack.model.DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //    List
    ArrayList<DataModel> dataModels;
    HashMap<String, String> studentInfo;
    private static StudentAdapter adapter;
    private ProgressDialog pDialog;

    ListView listView;
    TextView txtName, txtSchool, txtGrade;
    ImageView btnNext, btnPrev;

    int count = 0;

    //    JSON Var
    private String TAG = MainActivity.class.getSimpleName();
    String url = "http://192.168.42.140:8080/api/v1/students/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.txtStudentName);
        txtGrade = findViewById(R.id.txtStudentGrade);
        txtSchool = findViewById(R.id.txtStudentSchool);
        listView = findViewById(R.id.listView);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        dataModels = new ArrayList<>();
        studentInfo = new HashMap<>();

        new GetStudents().execute();
    }

    private class GetStudents extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
//            Make a request to the url and get a response
            String jsonStr = handler.makeServiceCall(url);
            Log.e(TAG, "Response from URL: " + jsonStr);

//            System.out.println(dataModels);
            dataModels.clear();
//            System.out.println(dataModels);

            if (jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    String name = jsonObject.getString("name");
                    String surname = jsonObject.getString("surname");

//                    Getting the json Array node
                    JSONArray school = jsonObject.getJSONArray("school");
                    JSONObject schoolObject = school.getJSONObject(count);

                    String schoolName = schoolObject.getString("schoolName");
                    int year = schoolObject.getInt("year");
                    String grade = schoolObject.getString("grade");

                    JSONArray subjects = schoolObject.getJSONArray("subjects");

                    for (int i = 0; i < subjects.length(); i++){
                        JSONObject subjectObject = subjects.getJSONObject(i);

                        String subject = subjectObject.getString("subject");
                        String term = subjectObject.getString("term");
                        Integer mark = subjectObject.getInt("mark");

                        dataModels.add(new DataModel(subject, term, mark));
                    }

                    studentInfo.put("name", name);
                    studentInfo.put("surname", surname);
                    studentInfo.put("schoolName", schoolName);
                    studentInfo.put("grade", grade);
                    studentInfo.put("year", Integer.toString(year));

                }catch (final JSONException e){
                    Log.e(TAG, "JSON parse error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            count = count - 1;
                            System.err.println("JSON parse error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong with the server",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }else{
                Log.e(TAG, "Couldn't get JSON from server ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Connection error, Please check your internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void results) {
            super.onPostExecute(results);

            final GetStudents getStudents = new GetStudents();

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check if json object is not last
                    // Go to the next object
                    count = count + 1;
                    getStudents.execute();
                }
            });

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check if json object is not first
                    // Go to the previous object
                    if(count != 0){
                        count = count - 1;
                    } else if(count == 0) {
                        count = 0;
                    } else {
                    }
                    getStudents.execute();
                }
            });

            txtName.setText(studentInfo.get("name") + " " + studentInfo.get("surname"));
            txtSchool.setText(studentInfo.get("schoolName"));
            txtGrade.setText(studentInfo.get("grade") + " - " + studentInfo.get("year"));

            adapter= new StudentAdapter(MainActivity.this, R.layout.list_items,  dataModels);
            listView.setAdapter(adapter);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

}
