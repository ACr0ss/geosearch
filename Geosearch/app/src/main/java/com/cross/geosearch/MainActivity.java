package com.cross.geosearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cross.geosearch.pojo.Page;
import com.cross.geosearch.utils.PageAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Page> pages;
    private String lat = "";
    private String lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        pages = (List<Page>) extras.getSerializable("data");
        lat = (String) extras.getSerializable("lat");
        lon = (String) extras.getSerializable("lon");

        setView();
    }

    private void setView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ticket_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PageAdapter adapter = new PageAdapter(pages, lat, lon);
        recyclerView.setAdapter(adapter);
    }
}