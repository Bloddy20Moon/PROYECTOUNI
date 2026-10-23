import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final SistemaTickets sistema = new SistemaTickets();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1 -> crearTicket();
                    case 2 -> atenderTicket();
                    case 3 -> verSiguiente();
                    case 4 -> mostrarLista("TICKETS PENDIENTES (por prioridad)", sistema.listarPendientes());
                    case 5 -> mostrarLista("HISTORIAL COMPLETO", sistema.listarTodos());
                    case 6 -> buscarTicket();
                    case 7 -> mostrarEstadisticas();
                    case 0 -> System.out.println("Cerrando sistema de tickets...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (IllegalStateException e) {
                System.out.println("Aviso: " + e.getMessage());
            }

        } while (opcion != 0);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n============ SISTEMA DE ATENCIÓN DE TICKETS ============");
        System.out.println("1. Crear ticket");
        System.out.println("2. Atender siguiente ticket");
        System.out.println("3. Ver siguiente ticket");
        System.out.println("4. Listar tickets pendientes");
        System.out.println("5. Listar historial completo");
        System.out.println("6. Buscar ticket por ID");
        System.out.println("7. Estadísticas");
        System.out.println("0. Salir");
        System.out.println("-------------------------------------------------------");
    }

    private static void crearTicket() {
        System.out.print("Nombre del cliente: ");
        String cliente = sc.nextLine().trim();
        System.out.print("Asunto: ");
        String asunto = sc.nextLine().trim();
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine().trim();

        Ticket.Prioridad prioridad = seleccionarPrioridad();
        Ticket ticket = sistema.crearTicket(cliente, asunto, descripcion, prioridad);
        System.out.println("Ticket creado correctamente: #" + String.format("%03d", ticket.getId()));
    }

    private static Ticket.Prioridad seleccionarPrioridad() {
        Ticket.Prioridad[] valores = Ticket.Prioridad.values();
        System.out.println("Prioridad:");
        for (int i = 0; i < valores.length; i++) {
            System.out.println("  " + (i + 1) + ". " + valores[i]);
        }
        int opcion;
        do {
            opcion = leerEntero("Seleccione prioridad (1-" + valores.length + "): ");
        } while (opcion < 1 || opcion > valores.length);
        return valores[opcion - 1];
    }

    private static void atenderTicket() {
        if (!sistema.hayPendientes()) {
            System.out.println("No hay tickets pendientes.");
            return;
        }
        Ticket ticket = sistema.atenderSiguiente();
        System.out.println("Atendiendo ticket:");
        System.out.println(ticket);
        System.out.print("¿Resolver ahora? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            sistema.resolver(ticket);
            System.out.println("Ticket #" + String.format("%03d", ticket.getId()) + " resuelto.");
        } else {
            System.out.println("El ticket queda EN_ATENCION.");
        }
    }

    private static void verSiguiente() {
        if (!sistema.hayPendientes()) {
            System.out.println("No hay tickets pendientes.");
            return;
        }
        System.out.println("Siguiente ticket a atender:");
        System.out.println(sistema.verSiguiente());
    }

    private static void mostrarLista(String titulo, List<Ticket> tickets) {
        System.out.println("\n--- " + titulo + " ---");
        if (tickets.isEmpty()) {
            System.out.println("(vacío)");
            return;
        }
        tickets.forEach(System.out::println);
    }

    private static void buscarTicket() {
        int id = leerEntero("ID del ticket: ");
        Optional<Ticket> ticket = sistema.buscarPorId(id);
        if (ticket.isPresent()) {
            System.out.println(ticket.get());
        } else {
            System.out.println("No existe un ticket con ID " + id + ".");
        }
    }

    private static void mostrarEstadisticas() {
        System.out.println("\n--- ESTADÍSTICAS ---");
        System.out.println("Total de tickets:  " + sistema.getTotalTickets());
        System.out.println("Pendientes:        " + sistema.contarPorEstado(Ticket.Estado.PENDIENTE));
        System.out.println("En atención:       " + sistema.contarPorEstado(Ticket.Estado.EN_ATENCION));
        System.out.println("Resueltos:         " + sistema.contarPorEstado(Ticket.Estado.RESUELTO));
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida, ingrese un número.");
            }
        }
    }
}
