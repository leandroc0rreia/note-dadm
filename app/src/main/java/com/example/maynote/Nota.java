package com.example.maynote;

public class Nota {

    private String titulo, texto, data;

    public Nota(){}

    public Nota(String titulo, String texto, String data) {
        this.titulo = titulo;
        this.texto = texto;
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "titulo='" + titulo + '\'' +
                ", texto='" + texto + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
