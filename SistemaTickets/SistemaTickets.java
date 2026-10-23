import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SistemaTickets {

    private final ColaPrioridad<Ticket> pendientes;
    private final List<Ticket> historial;

    public SistemaTickets() {
        this.pendientes = new ColaPrioridad<>(Ticket.ORDEN);
        this.historial = new ArrayList<>();
    }

    /** Crea un ticket y lo agrega a la cola de prioridad. */
    public Ticket crearTicket(String cliente, String asunto, String descripcion, Ticket.Prioridad prioridad) {
        Ticket ticket = new Ticket(cliente, asunto, descripcion, prioridad);
        pendientes.insertar(ticket);
        historial.add(ticket);
        return ticket;
    }

    /** Saca el ticket más urgente de la cola y lo pone en atención. */
    public Ticket atenderSiguiente() {
        Ticket ticket = pendientes.extraer();
        ticket.setEstado(Ticket.Estado.EN_ATENCION);
        return ticket;
    }

    /** Marca un ticket en atención como resuelto. */
    public void resolver(Ticket ticket) {
        if (ticket.getEstado() != Ticket.Estado.EN_ATENCION) {
            throw new IllegalStateException("Solo se pueden resolver tickets que están en atención.");
        }
        ticket.setEstado(Ticket.Estado.RESUELTO);
    }

    public Ticket verSiguiente() {
        return pendientes.verCima();
    }

    public List<Ticket> listarPendientes() {
        return pendientes.obtenerOrdenados();
    }

    public List<Ticket> listarTodos() {
        return new ArrayList<>(historial);
    }

    public Optional<Ticket> buscarPorId(int id) {
        return historial.stream().filter(t -> t.getId() == id).findFirst();
    }

    public boolean hayPendientes() {
        return !pendientes.estaVacia();
    }

    public int contarPorEstado(Ticket.Estado estado) {
        return (int) historial.stream().filter(t -> t.getEstado() == estado).count();
    }

    public int getTotalTickets() {
        return historial.size();
    }
}
