package com.example.ebookstore;

public class BooksList {


    public String fileName;
    public String price;
    public String url;

    public BooksList(String fileName, String price, String s) {

        this.fileName = fileName;
        this.price = price;
        this.url = s;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
