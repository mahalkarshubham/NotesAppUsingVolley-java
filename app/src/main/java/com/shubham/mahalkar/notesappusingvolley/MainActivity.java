package com.shubham.mahalkar.notesappusingvolley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvDailyNotes;
    String strTitle, strDescription;
    private RecyclerView.Adapter adapter_notes;
    private List<ModelNotes> NoteItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Notes");
        setSupportActionBar(toolbar);
        rvDailyNotes = findViewById(R.id.rvDailyNotes);

        rvDailyNotes.setHasFixedSize(true);
        rvDailyNotes.setLayoutManager(new LinearLayoutManager(this));

        getAllNotes();
    }

    private void getAllNotes() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting notes, Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final String fetchNotes = Utils.ipAddress + "fetch_notes.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fetchNotes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        NoteItems = new ArrayList();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("checkResponse === ",  response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String resp = jsonObject.getString("status");
                                if (resp.equals("Active")) {
                                    ModelNotes modelNotes = new ModelNotes();

                                    modelNotes.setNotesId(jsonObject.getString("notes_id"));
                                    modelNotes.setNotesTitle(jsonObject.getString("notes_title"));
                                    modelNotes.setNotesDescription(jsonObject.getString("notes_description"));

                                    NoteItems.add(modelNotes);
                                }else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter_notes = new AdapterNotes(NoteItems, MainActivity.this);
                            rvDailyNotes.setAdapter(adapter_notes);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "error  " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.btn_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addNote) {

            final Dialog myDialog = new Dialog(MainActivity.this);
            myDialog.setContentView(R.layout.dialog_add_note);

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(myDialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            myDialog.getWindow().setAttributes(layoutParams);
            myDialog.show();

            final EditText etTitle = myDialog.findViewById(R.id.etTitle);
            final EditText etDescription = myDialog.findViewById(R.id.etDescription);
            Button btnAddNote = myDialog.findViewById(R.id.btnAddNote);

            btnAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strTitle = etTitle.getText().toString();
                    strDescription = etDescription.getText().toString();

                    if (strTitle.trim().length() > 0 && strDescription.trim().length() > 0) {
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
                        if (isConnected) {
                            addNote();
                        } else {
                            Toast.makeText(MainActivity.this, "Please turn on your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "All Fields required", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating note, Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final String add_notes = Utils.ipAddress + "add_notes.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, add_notes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("checkResponse === ", response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String resp = jsonObject.getString("status");
                            if (resp.equals("Active")) {
                                Toast.makeText(MainActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(in);
                                finish();

                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(MainActivity.this, "error" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "error  " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("notes_title", strTitle);
                params.put("notes_description", strDescription);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
