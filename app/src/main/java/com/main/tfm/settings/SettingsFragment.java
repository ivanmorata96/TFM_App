package com.main.tfm.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.main.tfm.R;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchTheme;
    private Spinner spinnerLanguage;
    private AppCompatButton btnPurgeDatabase;
    private TextView tvAppInfo, tvAppInfoContent;

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchTheme = view.findViewById(R.id.switch_theme);
        spinnerLanguage = view.findViewById(R.id.spinner_language);
        btnPurgeDatabase = view.findViewById(R.id.btn_purge_database);
        tvAppInfo = view.findViewById(R.id.tv_app_info);
        tvAppInfoContent = view.findViewById(R.id.tv_app_info_content);

        // Inicializar SharedPreferences para guardar las preferencias del usuario
        sharedPreferences = getActivity().getSharedPreferences("app_preferences", getContext().MODE_PRIVATE);

        // Configurar el switch de tema
        boolean isDarkTheme = sharedPreferences.getBoolean("dark_theme", false);
        switchTheme.setChecked(isDarkTheme);
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_theme", isChecked);
            editor.apply();
            Toast.makeText(getContext(), "Tema cambiado. Reinicia la app para aplicar los cambios.", Toast.LENGTH_SHORT).show();
        });
        /*
        // Configurar el spinner de selección de idioma
        spinnerLanguage.setSelection(sharedPreferences.getInt("selected_language", 0));
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("selected_language", position);
                editor.apply();
                Toast.makeText(getContext(), "Idioma seleccionado: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona ningún elemento
            }
        });
        */
        btnPurgeDatabase.setOnClickListener(v -> {
            purgeDatabase();
            Toast.makeText(getContext(), "Base de datos purgada", Toast.LENGTH_SHORT).show();
        });

        tvAppInfo.setOnClickListener(v -> {
            if (tvAppInfoContent.getVisibility() == View.GONE) {
                tvAppInfoContent.setVisibility(View.VISIBLE);
            } else {
                tvAppInfoContent.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void purgeDatabase() {
        // Lógica para purgar la base de datos
        // Esta es una lógica simulada. Debes adaptar este método para interactuar con tu base de datos.
        // Por ejemplo, podrías usar un objeto DAO para borrar todas las entradas de tus tablas.
    }
}
