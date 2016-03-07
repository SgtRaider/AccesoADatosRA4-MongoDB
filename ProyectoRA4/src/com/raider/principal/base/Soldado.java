package com.raider.principal.base;

import org.bson.types.ObjectId;
import java.util.Date;

/**
 * Created by raider on 5/11/15.
 */
public class Soldado {

    private ObjectId id;

    private String nombre;

    private String apellidos;

    private Date fechaNacimiento;

    private String rango;

    private String lugarNacimiento;

    private String nUnidad;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public String getnUnidad() {
        return nUnidad;
    }

    public void setnUnidad(String nUnidad) {
        this.nUnidad = nUnidad;
    }

    @Override
    public String toString() {
        return rango + " " + apellidos;
    }
}
