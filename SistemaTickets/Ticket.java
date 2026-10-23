import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Ticket {

    public enum Prioridad {
        BAJA(1), MEDIA(2), ALTA(3), CRITICA(4);

        private final int nivel;

        Prioridad(int nivel) {
            this.nivel = nivel;
        }

        public int getNivel() {
            return nivel;
        }
    }

    public enum Estado {
        PENDIENTE, EN_ATENCION, RESUELTO
    }

    private static int contador = 0;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Criterio de orden para la cola: mayor prioridad primero;
     * a igual prioridad, el ticket que llegó antes (menor id).
     */
    public static final Comparator<Ticket> ORDEN = (a, b) -> {
        int porPrioridad = Integer.compare(b.prioridad.getNivel(), a.prioridad.getNivel());
        return porPrioridad != 0 ? porPrioridad : Integer.compare(a.id, b.id);
    };

    private final int id;
    private final String cliente;
    private final String asunto;
    private final String descripcion;
    private final Prioridad prioridad;
    private final LocalDateTime fechaCreacion;
    private Estado estado;

    public Ticket(String cliente, String asunto, String descripcion, Prioridad prioridad) {
        this.id = ++contador;
        this.cliente = cliente;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = Estado.PENDIENTE;
    }

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public Prioridad getPrioridad() { return prioridad; }
    public Estado getEstado() { return estado; }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format("[#%03d] %-9s | %-12s | %-20s | %s | %s",
                id, prioridad, estado, cliente, asunto, fechaCreacion.format(FORMATO));
    }
}
