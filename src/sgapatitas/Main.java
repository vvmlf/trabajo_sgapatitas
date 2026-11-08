package sgapatitas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner entrada = new Scanner(System.in);

    private final GestorAnimal gestorAnimal = new GestorAnimal();
    private final GestorAdoptante gestorAdoptante = new GestorAdoptante();
    private final GestorSolicitudAdopcion gestorSolicitud = new GestorSolicitudAdopcion();

    private int idUsuario;
    private String rolUsuario;

    public static void main(String[] args) {
        Main programa = new Main();
        if (programa.iniciarSesion()) {
            programa.mostrarMenu();
        }
        System.out.println("Programa finalizado.");
    }

    private boolean iniciarSesion() {
        System.out.println("       SISTEMA DE GESTION DE ADOPCION");
        System.out.println("           INICIAR SESION");

        for (int intento = 1; intento <= 3; intento++) {
            System.out.print("Usuario: ");
            String usuario = entrada.nextLine().trim();
            System.out.print("Contraseña: ");
            String clave = entrada.nextLine();

            String sql = "SELECT id_usuario, nombre, rol FROM usuario WHERE usuario = ? AND clave = ?";
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, usuario);
                ps.setString(2, clave);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idUsuario = rs.getInt("id_usuario");
                        String nombreUsuario = rs.getString("nombre");
                        rolUsuario = rs.getString("rol");
                        System.out.println("\nBienvenido/a, " + nombreUsuario + " (" + rolUsuario + ")");
                        return true;
                    }
                }
                System.out.println("Usuario o contrasena incorrectos. Intento " + intento + " de 3.\n");
            } catch (SQLException e) {
                System.out.println("No se pudo iniciar sesion. Revise MySQL y la conexion.");
                System.out.println("Detalle: " + e.getMessage());
                return false;
            }
        }
        System.out.println("Se alcanzó el limite de intentos.");
        return false;
    }

    private void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n---------- SISTEMA DE GESTION PATITAS----------");
            System.out.println("1. Registrar animal");
            System.out.println("2. Registrar nueva solicitud de adopcion");
            System.out.println("3. Mostrar todos los animales y/o cambiar estado");
            System.out.println("4. Cerrar sesion y salir");
            System.out.println("------------------------------------------------");
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    registrarAnimal();
                    break;
                case 2:
                    registrarSolicitud();
                    break;
                case 3:
                    consultarAnimales();
                    break;
                case 4:
                    System.out.println("Sesion cerrada.");
                    break;
                default:
                    System.out.println("Opcion incorrecta. Intente nuevamente.");
            }
        } while (opcion != 4);
    }

    private void registrarAnimal() {
        String[] especies = {"perro", "gato"};
        String[] sexos = {"macho", "hembra"};

        System.out.println("\n--- REGISTRAR ANIMAL ---");
        System.out.println("1. Perro");
        System.out.println("2. Gato");
        int opcionEspecie = leerEntero("Seleccione la especie: ");

        int edad = leerEntero("Edad (aprox): ");
        if (edad < 0) {
            System.out.println("La edad ingresada no corresponde");
            return;
        }

        System.out.println("1. Macho");
        System.out.println("2. Hembra");
        int opcionSexo = leerEntero("Seleccione el sexo: ");
        if (opcionSexo < 1 || opcionSexo > sexos.length) {
            System.out.println("Sexo no valido");
            return;
        }

        System.out.print("Descripcion o raza: ");
        String descripcion = entrada.nextLine().trim();
        if (descripcion.isEmpty()) {
            System.out.println("Porfavor añada una descripcion");
            return;
        }

        Animal animal;
        if ("gato".equals(especies[opcionEspecie - 1])) {
            animal = new Animal.Gato(edad, sexos[opcionSexo - 1], descripcion);
        } else {
            animal = new Animal.Perro(edad, sexos[opcionSexo - 1], descripcion);
        }

        try {
            gestorAnimal.guardar(animal);
            System.out.println("Animal registrado correctamente");
        } catch (SQLException e) {
            System.out.println("No se pudo registrar el animal: " + e.getMessage());
        }
    }

    private void consultarAnimales() {
        try {
            List<Animal> animales = gestorAnimal.listarTodos();
            if (animales.isEmpty()) {
                System.out.println("No hay animales registrados.");
                return;
            }

            System.out.println("\n--- ANIMALES REGISTRADOS ---");
            for (Animal animal : animales) {
                animal.mostrarFicha();
            }

            System.out.print("\nDesea cambiar el estado de un animal? (s/n): ");
            String respuesta = entrada.nextLine().trim();
            if (!respuesta.equalsIgnoreCase("s")) return;

            int id = leerEntero("Ingrese el ID del animal: ");
            String[] estados = {"En evaluacion", "Disponible", "En proceso", "Adoptado"};
            System.out.println("Estados posibles:");
            for (int i = 0; i < estados.length; i++) {
                System.out.println((i + 1) + ". " + estados[i]);
            }
            int opcionEstado = leerEntero("Seleccione el nuevo estado: ");

            String nuevoEstado = estados[opcionEstado - 1];
            if (nuevoEstado.equals("disponible") && !"veterinario".equalsIgnoreCase(rolUsuario)) {
                System.out.println("Solo un usuario con rol veterinario puede marcar un animal como disponible");
                return;
            }

            gestorAnimal.cambiarEstado(id, nuevoEstado);
            System.out.println("Estado actualizado correctamente.");
        } catch (SQLException e) {
            System.out.println("No se pudo consultar o actualizar el animal: " + e.getMessage());
        }
    }

    private void registrarSolicitud() {
        try {
            List<Animal> disponibles = gestorAnimal.listarDisponibles();
            if (disponibles.isEmpty()) {
                System.out.println("No hay animales disponibles para adopcion.");
                return;
            }

            System.out.println("\n--- ANIMALES DISPONIBLES ---");
            for (Animal animal : disponibles) {
                animal.mostrarFicha();
            }

            int idAnimal = leerEntero("Ingrese el ID del animal elegido: ");
            boolean encontrado = false;
            for (Animal animal : disponibles) {
                if (animal.getIdAnimal() == idAnimal) {
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("El ID no corresponde a un animal disponible.");
                return;
            }

            System.out.println("\n--- DATOS DEL ADOPTANTE ---");
            System.out.print("Nombre: ");
            String nombre = entrada.nextLine().trim();
            System.out.print("Apellido: ");
            String apellido = entrada.nextLine().trim();
            System.out.print("DNI: ");
            String dni = entrada.nextLine().trim();
            System.out.print("Telefono: ");
            String telefono = entrada.nextLine().trim();
            System.out.print("Observaciones: ");
            String observaciones = entrada.nextLine().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
                System.out.println("Nombre, apellido y DNI son requeridos");
                return;
            }

            int idAdoptante = gestorAdoptante.registrarAdoptante(nombre, apellido, dni, telefono, observaciones);
            int idSolicitud = gestorSolicitud.registrarSolicitud(idAnimal, idAdoptante, idUsuario);
            System.out.println("Solicitud registrada correctamente con Numero: " + idSolicitud);
        } catch (SQLException e) {
            System.out.println("No se pudo registrar la solicitud: " + e.getMessage());
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = entrada.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero");
            }
        }
    }
}
