package com.example.projudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class myProjects extends AppCompatActivity {
    private RecyclerView projectsRV;
    private TextView myPJs;
    private ArrayList<Project> currentProjects;
    private JSONObject currentProject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects);
        projectsRV = findViewById(R.id.rvProjects);
        myPJs = findViewById(R.id.tvMyPJ);
        currentProjects = new ArrayList<Project>();
        ProjectsRecViewAdapter adapter = new ProjectsRecViewAdapter(this);

        
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://web.socem.plymouth.ac.uk/COMP2000/api/students";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length();i++){
                    try {
                        int ID = response.getJSONObject(i).getInt("studentID");
                        if(ID == 1068){
                            JSONObject currentProject = response.getJSONObject(i);
                            int projectID = currentProject.getInt("projectID");
                            int studentID = currentProject.getInt("studentID");
                            String title = currentProject.getString("title");
                            String desc = currentProject.getString("description");
                            int year = currentProject.getInt("year");
                            String photo = currentProject.getString("photo");
                            currentProjects.add(new Project(projectID,studentID,title,desc,year,photo));

                            projectsRV.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        myPJs.setText("Parsing Error" + e.toString());
                        continue;
                    }

                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myPJs.setText("Error" + error.toString());
            }
        });

        queue.add(jsonArrayRequest);


        adapter.setProjects(currentProjects);
        projectsRV.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_slide_in,R.anim.left_slide_out);
    }

    public void toWelcome(View view) {
        Intent intent = new Intent(this, welcome.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_slide_in,R.anim.left_slide_out);
    }

    public void toAddPJ(View view) {
        Intent intent = new Intent(this, addProject.class);
        startActivity(intent);
    }
}