package com.example.listgrid;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import  java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private  AdapterList myAdapter;
    private List<ItemList> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        itemList = new ArrayList<>();
        itemList.add(new ItemList("Harry Potter","J.K. Rowling",
                "https://upload.wikimedia.org/wikipedia/id/5/56/Harry_potter_deathly_hallows_US.jpg"));
        itemList.add(new ItemList("To Kill A Mockingbird","Harper Lee",
                "https://upload.wikimedia.org/wikipedia/commons/4/4f/To_Kill_a_Mockingbird_%28first_edition_cover%29.jpg"));
        itemList.add(new ItemList("The Gulag Archipelago","Edward E. Ericson Jr.",
                "https://upload.wikimedia.org/wikipedia/id/c/c7/Gulag_Archipelago.jpg"));
        itemList.add(new ItemList("Laskar Pelangi","Andrea Hirata",
                "https://upload.wikimedia.org/wikipedia/id/8/8e/Laskar_pelangi_sampul.jpg"));

        myAdapter = new AdapterList(itemList);
        recyclerView.setAdapter((RecyclerView.Adapter) myAdapter);
    }
}