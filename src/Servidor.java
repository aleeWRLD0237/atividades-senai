import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;

/**
 * Ponto de entrada do sistema supervisório Pedro Alexandre.
 * Inicia o servidor HTTP na porta 8080.
 */
public class Servidor {

    public static void main(String[] args) throws IOException {
        // Garante UTF-8 no console para acentuação correta (RNF-01)
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.setErr(new PrintStream(System.err, true, "UTF-8"));
        } catch (Exception ignored) {}

        int porta = 8080;
        HttpServer servidor = HttpServer.create(new InetSocketAddress(porta), 0);

        // Registra as rotas do sistema
        servidor.createContext("/", new PaginaHandler());
        servidor.createContext("/api/dados", new DadosHandler());
        servidor.createContext("/api/emergencia", new EmergenciaHandler());

        servidor.start();

        System.out.println("=========================================");
        System.out.println("  Sistema Supervisorio Pedro Alexandre iniciado!");
        System.out.println("  Acesse: http://localhost:" + porta);
        System.out.println("  Para parar: Ctrl+C");
        System.out.println("=========================================");
    }
}
