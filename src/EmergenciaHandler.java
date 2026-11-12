import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handler da rota /api/emergencia
 * Recebe comandos de parada/liberação de emergência.
 *
 * RF-01: Botão de Parada de Emergência
 * RNF-11: Tempo de resposta < 20ms
 */
public class EmergenciaHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange requisicao) throws IOException {
        // Registra o tempo antes de processar (para medir RNF-11)
        long inicio = System.currentTimeMillis();

        // Lê o corpo da requisição (ação: "acionar" ou "liberar")
        InputStream corpo = requisicao.getRequestBody();
        String acao = new String(corpo.readAllBytes(), "UTF-8").trim();

        // Processa a ação
        if (acao.equals("acionar")) {
            DadosRobo.acionarEmergencia();
        } else if (acao.equals("liberar")) {
            DadosRobo.liberarEmergencia();
        }

        // Calcula tempo de resposta (RNF-11: deve ser < 20ms)
        long tempoResposta = System.currentTimeMillis() - inicio;
        System.out.println("[EMERGÊNCIA] Ação: " + acao + " | Tempo de resposta: " + tempoResposta + "ms");

        // Responde com JSON
        String json = "{\"ok\": true, \"tempoMs\": " + tempoResposta + ", \"estado\": \""
            + (DadosRobo.isEmEmergencia() ? "emergencia" : "normal") + "\"}";

        requisicao.getResponseHeaders().add("Content-Type", "application/json");
        requisicao.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        requisicao.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");

        // Trata preflight CORS (OPTIONS)
        if (requisicao.getRequestMethod().equals("OPTIONS")) {
            requisicao.sendResponseHeaders(204, -1);
            return;
        }

        byte[] bytes = json.getBytes("UTF-8");
        requisicao.sendResponseHeaders(200, bytes.length);
        OutputStream saida = requisicao.getResponseBody();
        saida.write(bytes);
        saida.close();
    }
}
