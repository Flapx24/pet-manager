package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.enums.AnimalType;

public class MascotaDTO {
    private Long id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private LocalDate fechaRegistro;
    private Double peso;
    private String color;
    private String sexo;
    private AnimalType tipoAnimal;
    private String notas;
    private String dieta;
    private boolean esterilizado;
    private LocalDate ultimaDesparasitacion;
    
    private String raza;
    private String tamano;
    private String tipoPelaje;
    private boolean pedigree;
    private boolean indoor;
    private String especie;
    private boolean alasCortadas;
    private boolean habla;
    private String tipoHabitat;
    private String requisitosTemperatura;
    private boolean venenoso;
    private String tipoAgua;
    private Double temperaturaAgua;
    private Double nivelPH;
    private String comportamientoSocial;
    private boolean entrenadoJaula;
    private Integer esperanzaVida;
    private String condicionDientes;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    public Double getPeso() {
        return peso;
    }
    public void setPeso(Double peso) {
        this.peso = peso;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public AnimalType getTipoAnimal() {
        return tipoAnimal;
    }
    public void setTipoAnimal(AnimalType tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }
    public String getNotas() {
        return notas;
    }
    public void setNotas(String notas) {
        this.notas = notas;
    }
    public String getDieta() {
        return dieta;
    }
    public void setDieta(String dieta) {
        this.dieta = dieta;
    }
    public boolean isEsterilizado() {
        return esterilizado;
    }
    public void setEsterilizado(boolean esterilizado) {
        this.esterilizado = esterilizado;
    }
    public LocalDate getUltimaDesparasitacion() {
        return ultimaDesparasitacion;
    }
    public void setUltimaDesparasitacion(LocalDate ultimaDesparasitacion) {
        this.ultimaDesparasitacion = ultimaDesparasitacion;
    }
    public String getRaza() {
        return raza;
    }
    public void setRaza(String raza) {
        this.raza = raza;
    }
    public String getTamano() {
        return tamano;
    }
    public void setTamano(String tamano) {
        this.tamano = tamano;
    }
    public String getTipoPelaje() {
        return tipoPelaje;
    }
    public void setTipoPelaje(String tipoPelaje) {
        this.tipoPelaje = tipoPelaje;
    }
    public boolean isPedigree() {
        return pedigree;
    }
    public void setPedigree(boolean pedigree) {
        this.pedigree = pedigree;
    }
    public boolean isIndoor() {
        return indoor;
    }
    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }
    public String getEspecie() {
        return especie;
    }
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    public boolean isAlasCortadas() {
        return alasCortadas;
    }
    public void setAlasCortadas(boolean alasCortadas) {
        this.alasCortadas = alasCortadas;
    }
    public boolean isHabla() {
        return habla;
    }
    public void setHabla(boolean habla) {
        this.habla = habla;
    }
    public String getTipoHabitat() {
        return tipoHabitat;
    }
    public void setTipoHabitat(String tipoHabitat) {
        this.tipoHabitat = tipoHabitat;
    }
    public String getRequisitosTemperatura() {
        return requisitosTemperatura;
    }
    public void setRequisitosTemperatura(String requisitosTemperatura) {
        this.requisitosTemperatura = requisitosTemperatura;
    }
    public boolean isVenenoso() {
        return venenoso;
    }
    public void setVenenoso(boolean venenoso) {
        this.venenoso = venenoso;
    }
    public String getTipoAgua() {
        return tipoAgua;
    }
    public void setTipoAgua(String tipoAgua) {
        this.tipoAgua = tipoAgua;
    }
    public Double getTemperaturaAgua() {
        return temperaturaAgua;
    }
    public void setTemperaturaAgua(Double temperaturaAgua) {
        this.temperaturaAgua = temperaturaAgua;
    }
    public Double getNivelPH() {
        return nivelPH;
    }
    public void setNivelPH(Double nivelPH) {
        this.nivelPH = nivelPH;
    }
    public String getComportamientoSocial() {
        return comportamientoSocial;
    }
    public void setComportamientoSocial(String comportamientoSocial) {
        this.comportamientoSocial = comportamientoSocial;
    }
    public boolean isEntrenadoJaula() {
        return entrenadoJaula;
    }
    public void setEntrenadoJaula(boolean entrenadoJaula) {
        this.entrenadoJaula = entrenadoJaula;
    }
    public Integer getEsperanzaVida() {
        return esperanzaVida;
    }
    public void setEsperanzaVida(Integer esperanzaVida) {
        this.esperanzaVida = esperanzaVida;
    }
    public String getCondicionDientes() {
        return condicionDientes;
    }
    public void setCondicionDientes(String condicionDientes) {
        this.condicionDientes = condicionDientes;
    }

    
}
