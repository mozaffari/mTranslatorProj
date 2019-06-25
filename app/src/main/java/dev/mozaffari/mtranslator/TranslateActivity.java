package dev.mozaffari.mtranslator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import dev.mozaffari.mtranslator.db.HistoryDatabaseHelper;
import dev.mozaffari.mtranslator.models.Translation;

public class TranslateActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String API_URL = "https://mozaffari.dev/api/gtranslate/";
    private RequestQueue requestQueue;

    EditText etFrom,etTo;

    Translation  translation;

    ImageButton ivClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        etFrom = findViewById(R.id.et_orignal_text);
        etTo = findViewById(R.id.et_translated);

        ivClear = findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(this);


        etTo.setOnClickListener(this);
        ivClear.setOnClickListener(this);

        translation = new Translation();

        translation.setTranslateFromCode(getIntent().getStringExtra("translate_from_code"));
        translation.setTranslateToCode(getIntent().getStringExtra("translate_to_code"));
        translation.setTranslateFrom(getIntent().getStringExtra("translate_from"));
        translation.setTranslateTo(getIntent().getStringExtra("translate_to"));

        etFrom.setHint(getResources().getString(R.string.tap_to_enter_text)+ " ("+ translation.getTranslateFrom()+")");
        etTo.setHint(getResources().getString(R.string.Translation)+ " (" +translation.getTranslateTo()+")");

        etFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                translate(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        requestQueue = getRequestQueue();



    }

    private void translate(final String text) {
        if(getRequestQueue() != null)
        {
            getRequestQueue().cancelAll(this);

        }

        etTo.setText(translation.getTranslatedText()+" ...");
        JsonObjectRequest
                jsonObjectRequest
                = new JsonObjectRequest(
                Request.Method.GET,
                API_URL+"?from="+translation.getTranslateFromCode()+"&to="+translation.getTranslateToCode()+"&text="+text,
                null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            //Toast.makeText(TranslateActivity.this, "Done! "+ jsonObject.getString("translated_text"), Toast.LENGTH_SHORT).show();
                            translation.setTranslatedText(jsonObject.getString("translated_text"));
                            translation.setOrignalText(text);
                            etTo.setText(translation.getTranslatedText());

                            if(!text.equals(""))
                                addHistory(translation);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TranslateActivity.this, getApplicationContext().getResources().getString(R.string.some_thing_bad_happend) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(TranslateActivity.this, getApplicationContext().getResources().getString(R.string.error)+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        translation.setTranslatedText("");
                        etTo.setText("");
                    }
                });
        jsonObjectRequest.setTag(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void addHistory(Translation translation) {
        HistoryDatabaseHelper databaseHelper = HistoryDatabaseHelper.getInstance(this);
        databaseHelper.addHistory(translation);
        databaseHelper.close();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.et_translated:
            {
                getRequestQueue().cancelAll(this);
                finish();
                break;
            }
            case R.id.iv_clear:
            {

                if(etFrom.getText().toString().equals(""))
                    finish();
                else
                    etFrom.setText("");

                break;
            }
        }
    }
}
