package dev.mozaffari.mtranslator;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;

import dev.mozaffari.mtranslator.utils.SettingsManager;
import dev.mozaffari.mtranslator.adapters.HistoryAdapter;
import dev.mozaffari.mtranslator.adapters.LanguageSpinnerAdapter;
import dev.mozaffari.mtranslator.db.HistoryDatabaseHelper;
import dev.mozaffari.mtranslator.models.LanguageItem;
import dev.mozaffari.mtranslator.models.Translation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<LanguageItem> languageItems;
    private LanguageSpinnerAdapter fromAdapter,toAdapter;

    Spinner spinnerTo,spinnerFrom;

    private ImageButton ivSwapButton;
    private EditText etTapTo;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HistoryAdapter historyAdapter;

    List<Translation> historyTranslations;

    private int fromIndex = 0,toIndex =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initList();

        initHistory();

        initFromSpinner();

        initToSpinner();

        ivSwapButton =findViewById(R.id.iv_swap_button);
        etTapTo = findViewById(R.id.et_tap_to);

        ivSwapButton.setOnClickListener(this);
        etTapTo.setOnClickListener(this);


        historyAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HistoryDatabaseHelper.getInstance(getApplicationContext()).removeHistory(historyTranslations.get(position).getId());
                historyTranslations.remove(position);
                historyAdapter.notifyDataSetChanged();
            }
        });


    }

    private void initHistory() {
        recyclerView = findViewById(R.id.rv_history);
        HistoryDatabaseHelper historyDatabaseHelper =  HistoryDatabaseHelper.getInstance(this);

        historyTranslations = historyDatabaseHelper.getAllHistory();
        historyDatabaseHelper.close();
        historyAdapter = new HistoryAdapter(historyTranslations,this);

        layoutManager = new LinearLayoutManager(this);

        //to get last added history first
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(historyAdapter);



    }

    private void initToSpinner() {

        spinnerTo = findViewById(R.id.spinner_lang_to);
        toAdapter = new LanguageSpinnerAdapter(this, languageItems);
        spinnerTo.setAdapter(toAdapter);

        toIndex = SettingsManager.getTranslateToIndex(this);
        spinnerTo.setSelection(toIndex);

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LanguageItem clickedItem = (LanguageItem) parent.getItemAtPosition(position);
                SettingsManager.setTranslateToIndex(getApplicationContext(),position);
                toIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initFromSpinner() {
        spinnerFrom = findViewById(R.id.spinner_lang_from);

        fromAdapter = new LanguageSpinnerAdapter(this, languageItems);
        spinnerFrom.setAdapter(fromAdapter);
        fromIndex = SettingsManager.getTranslateFromIndex(this);
        spinnerFrom.setSelection(fromIndex);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LanguageItem clickedItem = (LanguageItem) parent.getItemAtPosition(position);
                SettingsManager.setTranslateFromIndex(getApplicationContext(),position);
                fromIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void initList() {
        languageItems = new ArrayList<>();
        languageItems.add(new LanguageItem(getResources().getString(R.string.english),"en", R.drawable.flag_us));
        languageItems.add(new LanguageItem(getResources().getString(R.string.farsi),"fa", R.drawable.flag_ir));
        languageItems.add(new LanguageItem(getResources().getString(R.string.pashto),"ps", R.drawable.flag_af));
        languageItems.add(new LanguageItem(getResources().getString(R.string.arabic),"ar", R.drawable.flag_sa));
        languageItems.add(new LanguageItem(getResources().getString(R.string.urdu),"ur", R.drawable.flag_pk));
        languageItems.add(new LanguageItem(getResources().getString(R.string.chines),"zh", R.drawable.flag_cn));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HistoryDatabaseHelper helper = HistoryDatabaseHelper.getInstance(this);
        List<Translation> historyTranslations2 = helper.getAllHistory();
        historyTranslations.clear();
        for (Translation t:
             historyTranslations2) {

            historyTranslations.add(t);
        }
        helper.close();
        historyAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_swap_button:
            {
                spinnerFrom.setSelection(toIndex);
                spinnerTo.setSelection(fromIndex);

                //time to swap index now
                int tmp = toIndex;
                toIndex = fromIndex;
                fromIndex = tmp;
                break;
            }
            case R.id.et_tap_to:
            {
                Intent intent = new Intent(getApplicationContext(),TranslateActivity.class);
                intent.putExtra("translate_from_code",languageItems.get(spinnerFrom.getSelectedItemPosition()).getLanguageCode());
                intent.putExtra("translate_to_code",languageItems.get(spinnerTo.getSelectedItemPosition()).getLanguageCode());
                intent.putExtra("translate_to",languageItems.get(spinnerTo.getSelectedItemPosition()).getLanguageName());
                intent.putExtra("translate_from",languageItems.get(spinnerFrom.getSelectedItemPosition()).getLanguageName());
                //intent.putExtra("language_code",languageItems.get(spinnerFrom.getSelectedItemPosition()).getLanguageCode());
                startActivityForResult(intent,0);
                break;
            }

        }

    }
}
