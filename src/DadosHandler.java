import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Handler da rota /api/dados
 * Retorna os dados do robô em formato JSON para o frontend atualizar a tela.
 *
 * O JavaScript da página chama essa rota a cada 1 segundo (RNF-03: atualização < 100ms).
 */
public class DadosHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange requisicao) throws IOException {
        // Simula chegada de dados novos do robô
        DadosRobo.simularAtualizacao();

        // Monta o JSON com todos os dados
        String json = montarJson();

        // Define o tipo de resposta e permite chamadas do JavaScript (CORS)
        requisicao.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        requisicao.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        // Envia a resposta com código 200 (OK)
        byte[] bytes = json.getBytes("UTF-8");
        requisicao.sendResponseHeaders(200, bytes.length);

        OutputStream saida = requisicao.getResponseBody();
        saida.write(bytes);
        saida.close();
    }

    // Monta o JSON manualmente (sem biblioteca externa — Java básico)
    private String montarJson() {
        // Monta os logs como array JSON
        StringBuilder logsJson = new StringBuilder("[");
        List<String> logs = DadosRobo.getLogs();
        for (int i = 0; i < Math.min(logs.size(), 15); i++) {
            if (i > 0) logsJson.append(",");
            logsJson.append("\"").append(logs.get(i).replace("\"", "'")).append("\"");
        }
        logsJson.append("]");

        // Monta o objeto JSON completo
        return "{"
            + "\"statusRobo\": \"" + DadosRobo.getStatusRobo() + "\","
            + "\"statusPrograma\": \"" + DadosRobo.getStatusPrograma() + "\","
            + "\"statusComunicacao\": \"" + DadosRobo.getStatusComunicacao() + "\","
            + "\"statusCamera\": \"" + DadosRobo.getStatusCamera() + "\","
            + "\"pecasTotal\": " + DadosRobo.getPecasTotal() + ","
            + "\"pecasVermelhas\": " + DadosRobo.getPecasVermelhas() + ","
            + "\"pecasVerdes\": " + DadosRobo.getPecasVerdes() + ","
            + "\"pecasAzuis\": " + DadosRobo.getPecasAzuis() + ","
            + "\"tempoCiclo\": " + DadosRobo.getTempoCiclo() + ","
            + "\"taxaAcerto\": " + DadosRobo.getTaxaAcerto() + ","
            + "\"oee\": " + DadosRobo.getOee() + ","
            + "\"disponibilidade\": " + DadosRobo.getDisponibilidade() + ","
            + "\"emEmergencia\": " + DadosRobo.isEmEmergencia() + ","
            + "\"logs\": " + logsJson.toString()
            + "}";
    }
}
