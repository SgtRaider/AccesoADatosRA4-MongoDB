package com.raider.principal.base;

import org.bson.types.ObjectId;
import java.util.Date;

/**
 * Created by raider on 5/11/15.
 */
public class Unidad {

    private ObjectId id;

    private String nUnidad;

    private String tipo;

    private int noTropas;

    private Date fechaCreacion;

    private String nCuartel;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getnUnidad() {
        return nUnidad;
    }

    public void setnUnidad(String nUnidad) {
        this.nUnidad = nUnidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNoTropas() {
        return noTropas;
    }

    public void setNoTropas(int noTropas) {
        this.noTropas = noTropas;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getnCuartel() {
        return nCuartel;
    }

    public void setnCuartel(String nCuartel) {
        this.nCuartel = nCuartel;
    }

    @Override
    public String toString() {
        return tipo + " " + nUnidad;
    }
}
