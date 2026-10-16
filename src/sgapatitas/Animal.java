package sgapatitas;

import java.time.LocalDate;

public abstract class Animal {
    private int idAnimal;
    private final String especie;
    private final int edadAprox;
    private final String sexo;
    private final LocalDate fechaIngreso;
    private final String descripcion;
    private String estado;

    public Animal(String especie, int edadAprox, String sexo, String descripcion) {
        this.especie = especie;
        this.edadAprox = edadAprox;
        this.sexo = sexo;
        this.descripcion = descripcion;
        this.fechaIngreso = LocalDate.now();
        this.estado = "En evaluacion";
    }

    public abstract void mostrarFicha();

    public static class Perro extends Animal {
        public Perro(int edadAprox, String sexo, String descripcion) {
            super("perro", edadAprox, sexo, descripcion);
        }

        public void mostrarFicha() {
            System.out.println("PERRO = ID: " + getIdAnimal() + " -Sexo: " + getSexo() + " -Edad: " + getEdadAprox() + " año/os -Estado: " + getEstado() + " -Descripción: " + getDescripcion());
        }
    }

    public static class Gato extends Animal {
        public Gato(int edadAprox, String sexo, String descripcion) {
            super("gato", edadAprox, sexo, descripcion);
        }

        public void mostrarFicha() {
            System.out.println("GATO = ID: " + getIdAnimal() + " -Sexo: " + getSexo() + " -Edad: " + getEdadAprox() + " año/os -Estado: " + getEstado() + " -Descripción: " + getDescripcion());
        }
    }

    public int getIdAnimal() { return idAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }
    public String getEspecie() { return especie; }
    public int getEdadAprox() { return edadAprox; }
    public String getSexo() { return sexo; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}