package com.example.listgrid;

public class ItemList {
    private String Judul;
    private String Penulis;
    private String imageUrl;
    private String id;

    public ItemList(String judul, String penulis, String imageUrl) {
        Judul = judul;
        Penulis = penulis;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        Judul = judul;
    }

    public String getPenulis() {
        return Penulis;
    }

    public void setPenulis(String penulis) {
        Penulis = penulis;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
