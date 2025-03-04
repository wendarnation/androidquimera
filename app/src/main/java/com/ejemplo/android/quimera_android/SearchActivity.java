package com.ejemplo.android.quimera_android;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private TextInputEditText inputSearch;
    private TextInputLayout searchInputLayout;
    private Map<View, String> itemsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inputSearch = findViewById(R.id.inputSearch);
        searchInputLayout = findViewById(R.id.searchInputLayout);

        // Abrir la cámara directamente al hacer clic en el icono
        searchInputLayout.setEndIconOnClickListener(v -> openCamera());

        // Mapeo de elementos con su texto correspondiente
        mapElements();

        // Filtrar elementos en función del texto ingresado
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Vincular BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_search) {
                    return true;
                } else if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No se pudo abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private void mapElements() {
        addElementToMap(R.id.imageJordanV, R.id.titleJordanV, R.id.subtitleJordanV, R.id.lowestPriceLabel, R.id.priceJordanV);
        addElementToMap(R.id.imageJordanI, R.id.titleJordanI, R.id.subtitleJordanI, R.id.lowestPriceLabelJordanI, R.id.priceJordanI);
        addElementToMap(R.id.imageNewBalance, R.id.titleNewBalance, R.id.subtitleNewBalance, R.id.lowestNewBalance, R.id.priceNewBalance);
        addElementToMap(R.id.imageadidas, R.id.titleadidas, R.id.subtitleadidas, R.id.lowestadidas, R.id.priceadidas);
    }

    private void addElementToMap(int imageViewId, int titleId, int subtitleId, int priceLabelId, int priceId) {
        ImageView image = findViewById(imageViewId);
        TextView title = findViewById(titleId);
        TextView subtitle = findViewById(subtitleId);
        TextView priceLabel = findViewById(priceLabelId);
        TextView price = findViewById(priceId);
        String searchText = (title.getText().toString() + " " + subtitle.getText().toString()).toLowerCase();

        itemsMap.put(image, searchText);
        itemsMap.put(title, searchText);
        itemsMap.put(subtitle, searchText);
        itemsMap.put(priceLabel, searchText);
        itemsMap.put(price, searchText);
    }

    private void filterItems(String query) {
        String searchQuery = query.toLowerCase();
        boolean found = false;

        for (Map.Entry<View, String> entry : itemsMap.entrySet()) {
            View element = entry.getKey();
            String text = entry.getValue();

            if (text.contains(searchQuery)) {
                element.setVisibility(View.VISIBLE);
                found = true;
            } else {
                element.setVisibility(View.GONE);
            }
        }

        if (!found) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
    }
}
