package com.example.androiddasar6offlinedatabase;

import android.content.Intent;
import android.os.Bundle;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<CatatanModel> dataCatatan = new ArrayList<>();
    RecyclerView recycler;
    RealmHelper realm;
    FloatingSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TambahCatatanActivity.class));
            }
        });

        realm = new RealmHelper(MainActivity.this);

        //1 Membuat layout per item
        //2 Membuat data model
//        CatatanModel catatan1 = new CatatanModel();
//        catatan1.setId("1");
//        catatan1.setJudul("Hutang ke A");
//        catatan1.setJumlahhutang("20000");
//        catatan1.setTanggal("12-12-2012");
//
//        for (int i = 0; i < 20; i++) {
//            dataCatatan.add(catatan1);
//        }

        //get data dari realm
        dataCatatan = realm.showData();

        //3 Adapter
        recycler = findViewById(R.id.recyclerView);
        recycler.setAdapter(new CatatanAdapter(MainActivity.this, dataCatatan));

        //4 Layout manager
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recycler.setHasFixedSize(true);
        recycler.addItemDecoration(new DividerItemDecoration(MainActivity.this, 1));

        searchView = findViewById(R.id.floating_search_view);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
//                Toast.makeText(MainActivity.this, "" + newQuery, Toast.LENGTH_SHORT).show();
                //filter searchView
                List<CatatanModel> filterCatatan = filterData(dataCatatan, newQuery);
                recycler.setAdapter(new CatatanAdapter(MainActivity.this, filterCatatan));
            }
        });
    }

    private List<CatatanModel> filterData(List<CatatanModel> dataCatatan, String newQuery) {
        String lowercasequery = newQuery.toLowerCase();
        List<CatatanModel> filterData = new ArrayList<>();
        for (int i = 0; i < dataCatatan.size(); i++) {
            String text = dataCatatan.get(i).getJudul().toLowerCase();
            if (text.contains(lowercasequery)) {
                filterData.add(dataCatatan.get(i));
            }
        }
        return filterData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataCatatan = realm.showData();
        recycler.setAdapter(new CatatanAdapter(MainActivity.this, dataCatatan));
    }
}