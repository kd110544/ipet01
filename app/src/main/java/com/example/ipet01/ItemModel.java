package com.example.ipet01;

public class ItemModel {
    //image的型態本來是int,為了要傳網址我改String
    private String image,nama, usia, kota;

    public ItemModel() {
    }

    public ItemModel(String image, String nama, String usia, String kota) {
        this.image = image;
        this.nama = nama;
        this.usia = usia;
        this.kota = kota;
    }

    public String getImage() {   //image的型態本來是int,為了要傳網址我改String
        return image;
    }

    public String getNama() {
        return nama;
    }

    public String getUsia() {
        return usia;
    }

    public String getKota() {
        return kota;
    }
}
