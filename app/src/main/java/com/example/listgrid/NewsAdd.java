package com.example.listgrid;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.Nullable;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;


public class NewsAdd extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText title, writer;
    String id="", judul, penulis, image;
    private ImageView img;
    private Button saveNews, chooseImage;
    private Uri imageUri;
    private FirebaseFirestore dbNews;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_add);
        dbNews = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        title = findViewById(R.id.title);
        writer = findViewById(R.id.writer);
        img = findViewById(R.id.img);
        saveNews = findViewById(R.id.btnAdd);
        chooseImage = findViewById(R.id.btnChooseImage);

        progressDialog = new ProgressDialog(NewsAdd.this);
        progressDialog.setTitle("Loading...");

        Intent updateOption = getIntent();
        if (updateOption != null) {
            id = updateOption.getStringExtra("id");
            judul = updateOption.getStringExtra("title");
            penulis = updateOption.getStringExtra("writer");
            image = updateOption.getStringExtra("img");

            title.setText(judul);
            writer.setText(penulis);
            Glide.with(this).load(image).into(img);
        }

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        saveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newsTitle = title.getText().toString().trim();
                String newsWriter = writer.getText().toString().trim();

                if (newsTitle.isEmpty() || newsWriter.isEmpty()) {
                    Toast.makeText(NewsAdd.this, "Judul dan penulis wajib diisi", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                if (imageUri != null){
                    uploadImageToStorage(newsTitle, newsWriter);
                }
                else {
                    saveData(newsTitle, newsWriter, image);
                }
            }
        });

    }
    private  void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }
    private void uploadImageToStorage(String newsTitle, String newsWriter){
        if (imageUri != null) {
            StorageReference storageRef = storage.getReference().child("news_image/" + System.currentTimeMillis() + ".jpg");
            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveData(newsTitle, newsWriter, imageUrl);
            })).addOnFailureListener(e -> {progressDialog.dismiss();
            Toast.makeText(NewsAdd.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();});
        }
    }
    private void saveData(String newsTitle, String newsWriter, String imageUrl){
        Map<String, Object> news_data = new HashMap<>();
        news_data.put("title", newsTitle);
        news_data.put("writer", newsWriter);
        news_data.put("img", imageUrl);

        if (id != null) {
            dbNews.collection("news_data").document(id)
                    .update(news_data)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                    finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "Gagal mengubah data" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w("NewsAdd", "gagal mengubah dokumen", e);
                    });
        } else {
            dbNews.collection("news_data").add(news_data).addOnSuccessListener(documentReference -> {progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "berhasil menambahkan data", Toast.LENGTH_SHORT).show();
                        title.setText("");
                        writer.setText("");
                        img.setImageResource(0);})
                    .addOnFailureListener(e -> {progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "Gagal menambahkan data" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w("NewsAdd", "Gagal", e);});
        }


    }
}