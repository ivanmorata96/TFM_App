package com.main.tfm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class searchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el Intent que inició esta actividad
        Intent intent = getIntent();

        // Recuperar el valor de "query" del Intent
        String searchQuery = intent.getStringExtra("item");

        //Log.i("Test", searchQuery);

        // Usar el valor de 'searchQuery' como desees
        TextView searchResultsTextView = findViewById(R.id.textTest);
        searchResultsTextView.setText("Resultados de la búsqueda: " + searchQuery);
    }
}