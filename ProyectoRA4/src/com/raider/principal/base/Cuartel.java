package com.raider.principal.base;

import org.bson.types.ObjectId;


/**
 * Created by raider on 5/11/15.
 */
public class Cuartel {

    private ObjectId id;

    private String nCuartel;

    private Double latitud;

    private Double longitud;

    private Boolean actividad;

    private String localidad;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getnCuartel() {
        return nCuartel;
    }

    public void setnCuartel(String nCuartel) {
        this.nCuartel = nCuartel;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getActividad() {
        return actividad;
    }

    public void setActividad(Boolean actividad) {
        this.actividad = actividad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    @Override
    public String toString() {
        return nCuartel + " " + localidad;
    }
}
