import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Cola de prioridad implementada con un montículo binario (heap).
 * El elemento "menor" según el comparador es el que sale primero.
 */
public class ColaPrioridad<T> {

    private static final int CAPACIDAD_INICIAL = 16;

    private Object[] heap;
    private int tamanio;
    private final Comparator<? super T> comparador;

    public ColaPrioridad(Comparator<? super T> comparador) {
        this.comparador = comparador;
        this.heap = new Object[CAPACIDAD_INICIAL];
        this.tamanio = 0;
    }

    /** Inserta un elemento y lo sube a su posición según la prioridad. */
    public void insertar(T elemento) {
        if (tamanio == heap.length) {
            redimensionar();
        }
        heap[tamanio] = elemento;
        subir(tamanio);
        tamanio++;
    }

    /** Elimina y devuelve el elemento de mayor prioridad. */
    public T extraer() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola de prioridad está vacía.");
        }
        T raiz = elementoEn(0);
        tamanio--;
        heap[0] = heap[tamanio];   // el último pasa a la raíz
        heap[tamanio] = null;
        if (tamanio > 0) {
            bajar(0);
        }
        return raiz;
    }

    /** Devuelve el elemento de mayor prioridad sin eliminarlo. */
    public T verCima() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola de prioridad está vacía.");
        }
        return elementoEn(0);
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int getTamanio() {
        return tamanio;
    }

    /** Devuelve copia de los elementos ordenados por prioridad (sin modificar la cola). */
    public List<T> obtenerOrdenados() {
        List<T> lista = new ArrayList<>();
        for (int i = 0; i < tamanio; i++) {
            lista.add(elementoEn(i));
        }
        lista.sort(comparador);
        return lista;
    }

    // ---------- Métodos internos del heap ----------

    /** Sube el elemento en la posición i mientras sea más prioritario que su padre. */
    private void subir(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (comparar(i, padre) >= 0) {
                break;
            }
            intercambiar(i, padre);
            i = padre;
        }
    }

    /** Baja el elemento en la posición i hasta que cumpla la propiedad del heap. */
    private void bajar(int i) {
        while (true) {
            int izquierdo = 2 * i + 1;
            int derecho = 2 * i + 2;
            int mejor = i;

            if (izquierdo < tamanio && comparar(izquierdo, mejor) < 0) {
                mejor = izquierdo;
            }
            if (derecho < tamanio && comparar(derecho, mejor) < 0) {
                mejor = derecho;
            }
            if (mejor == i) {
                break;
            }
            intercambiar(i, mejor);
            i = mejor;
        }
    }

    private int comparar(int a, int b) {
        return comparador.compare(elementoEn(a), elementoEn(b));
    }

    private void intercambiar(int a, int b) {
        Object temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    @SuppressWarnings("unchecked")
    private T elementoEn(int i) {
        return (T) heap[i];
    }

    private void redimensionar() {
        heap = Arrays.copyOf(heap, heap.length * 2);
    }
}
