package sgapatitas;

import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorAnimal gestorAnimal = new GestorAnimal();
        GestorAdoptante gestorAdoptante = new GestorAdoptante();
        GestorSolicitudAdopcion gestorSolicitud = new GestorSolicitudAdopcion();

        int opcion = 0;

        do {
            System.out.println("\n---- SISTEMA DE GESTION PATITAS ----");
            System.out.println("1. Registrar un animal");
            System.out.println("2. Registrar nueva solicitud de adopcion");
            System.out.println("3. Mostrar todos los animales y/o cambiar disponibilidad");
            System.out.println("4. Salir");
            System.out.print("Elija una opcion: ");

            try {
                if (scanner.hasNextInt()) {
                    opcion = scanner.nextInt();
                } else {
                    System.out.println("Error: Debe ingresar un numero valido.");
                    scanner.next();
                    opcion = 0;
                    continue;
                }
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Especie (perro/gato): ");
                        String especie = scanner.nextLine().trim().toLowerCase();

                        System.out.print("Edad aproximada: ");
                        int edad = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Sexo: ");
                        String sexo = scanner.nextLine();

                        System.out.print("Descripción y/o raza: ");
                        String descripcion = scanner.nextLine();

                        Animal nuevoAnimal;
                        if (especie.equals("gato")) {
                            nuevoAnimal = new Animal.Gato(edad, sexo, descripcion);
                        } else {
                            nuevoAnimal = new Animal.Perro(edad, sexo, descripcion);
                        }

                        gestorAnimal.guardar(nuevoAnimal);
                        System.out.println("Animal registrado con éxito en la base de datos");
                        break;

                    case 2:
                        System.out.println("\n--- ANIMALES DISPONIBLES PARA ADOPCION ---");
                        List<Animal> disponibles = gestorAnimal.listarDisponibles();
                        if (disponibles.isEmpty()) {
                            System.out.println("No hay animales disponibles en este momento.");
                            break;
                        }
                        for (Animal a : disponibles) {
                            a.mostrarFicha();
                        }

                        System.out.print("\nIngrese el ID del animal elegido por el adoptante: ");
                        int idAnimalElegido = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("\n--- DATOS DEL ADOPTANTE ---");
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Apellido: ");
                        String apellido = scanner.nextLine();
                        System.out.print("DNI: ");
                        String dni = scanner.nextLine();
                        System.out.print("Telefono: ");
                        String telefono = scanner.nextLine();
                        System.out.print("Formulario de idoneidad (observaciones): ");
                        String formularioIdoneidad = scanner.nextLine();

                        int idAdoptante = gestorAdoptante.registrarAdoptante(nombre, apellido, dni, telefono, formularioIdoneidad);
                        if (idAdoptante != -1) {
                            int idUsuarioSistemaFijo = 1;
                            int idSolicitud = gestorSolicitud.registrarSolicitud(idAnimalElegido, idAdoptante, idUsuarioSistemaFijo);
                            if (idSolicitud != -1) {
                                System.out.println("Solicitud registrada con éxito. ID de solicitud: " + idSolicitud);
                            }
                        }
                        break;

                    case 3:
                        System.out.println("\n--- LISTA DE TODOS LOS ANIMALES ---");
                        List<Animal> todos = gestorAnimal.listarTodos();
                        if (todos.isEmpty()) {
                            System.out.println("No hay animales registrados en la base de datos.");
                            break;
                        }
                        for (Animal a : todos) {
                            a.mostrarFicha();
                        }

                        System.out.print("\nCambiar la disponibilidad de algun animal? (s/n): ");
                        String respuesta = scanner.nextLine();
                        if (respuesta.equalsIgnoreCase("s")) {
                            System.out.print("Ingrese el ID del animal: ");
                            int idMod = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Ingrese el nuevo estado (ej: disponible, en evaluacion, adoptado): ");
                            String nuevoEstado = scanner.nextLine();
                            gestorAnimal.cambiarEstado(idMod, nuevoEstado);
                        }
                        break;

                    case 4:
                        System.out.println("Saliendo...");
                        break;

                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: Tipo de dato ingresado no válido. Intente nuevamente.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }

        } while (opcion != 4);

        scanner.close();
    }
}