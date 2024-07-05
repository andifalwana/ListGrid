package com.example.listgrid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewsDetail extends AppCompatActivity {
    TextView newsTitle, newsWriter;

    ImageView newsImage;
    Button edit, hapus;
    private FirebaseFirestore db;

//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        newsTitle = findViewById(R.id.newsTitle);
        newsWriter = findViewById(R.id.newsWriter);
        newsImage = findViewById(R.id.newsImage);
        edit = findViewById(R.id.editButton);
        hapus = findViewById(R.id.deleteButton);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String writer = intent.getStringExtra("writer");
        String imgUrl = intent.getStringExtra("img");

        newsTitle.setText(title);
        newsWriter.setText(writer);
        Glide.with(this).load(imgUrl).into(newsImage);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetail.this, NewsAdd.class);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("writer", writer);
                intent.putExtra("img", imgUrl);
                startActivity(intent);
            }
        });
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("news_data").document(id)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(NewsDetail.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewsDetail.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(NewsDetail.this, "Gagal mengahpus data" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.w("NewsDetail", "gagal menghapus document", e);
                        });
            }
        });

    }
}