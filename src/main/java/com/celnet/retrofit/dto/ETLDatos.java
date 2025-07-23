package com.celnet.retrofit.dto;

public class ETLDatos {
    private int registrosLeidos;
    private int registrosValidos;
    private int registrosInsertados;

    // Constructor
    public ETLDatos(int registrosLeidos, int registrosValidos, int registrosInsertados) {
        this.registrosLeidos = registrosLeidos;
        this.registrosValidos = registrosValidos;
        this.registrosInsertados = registrosInsertados;
    }

    public ETLDatos() {
    }

    // Getters y setters
    public int getRegistrosLeidos() {
        return registrosLeidos;
    }

    public void setRegistrosLeidos(int registrosLeidos) {
        this.registrosLeidos = registrosLeidos;
    }

    public int getRegistrosValidos() {
        return registrosValidos;
    }

    public void setRegistrosValidos(int registrosValidos) {
        this.registrosValidos = registrosValidos;
    }

    public int getRegistrosInsertados() {
        return registrosInsertados;
    }

    public void setRegistrosInsertados(int registrosInsertados) {
        this.registrosInsertados = registrosInsertados;
    }
}