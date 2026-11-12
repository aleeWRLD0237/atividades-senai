import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Armazena o estado atual do sistema do robô.
 * RF-02: statusRobo  RF-03: statusPrograma  RF-04: statusComunicacao
 */
public class DadosRobo {

    // RF-02: Estado de operação do robô
    private static String statusRobo = "Rampa 3 em execução";

    // RF-03: Programa habilitado ou não
    private static String statusPrograma = "Habilitado";

    // RF-04: Comunicação com a célula
    private static String statusComunicacao = "Online";

    // RF-10: Status do sistema de visão
    private static String statusCamera = "Processando";

    // RF-05: contador de peças
    private static int pecasTotal     = 1257;
    private static int pecasVermelhas = 528;
    private static int pecasVerdes    = 440;
    private static int pecasAzuis     = 289;

    // RF-06: tempo de ciclo em segundos
    private static double tempoCiclo = 4.2;

    // RF-07: taxa de acerto do processo de separação por cor
    private static double taxaAcerto = 98.5;

    // RF-12: OEE — Eficiência Global do Equipamento
    private static double oee = 85.0;

    // RF-13: disponibilidade mecânica do robô
    private static double disponibilidade = 92.3;

    // RF-11: lista de logs do robô com timestamp
    private static List<String> logs = new ArrayList<>();
    private static final int MAX_LOGS = 50;

    // RF-01: flag de emergência
    private static boolean emEmergencia = false;

    // Inicializa com logs de boot
    static {
        adicionarLog("Sistema iniciado com sucesso");
        adicionarLog("Camera CAM-01 conectada");
        adicionarLog("Comunicacao com CLP estabelecida");
        adicionarLog("Robo em modo automatico");
        adicionarLog("Contagem de pecas iniciada");
    }

    // RF-11: adiciona log com timestamp
    public static void adicionarLog(String mensagem) {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logs.add(0, hora + " — " + mensagem);
        if (logs.size() > MAX_LOGS) logs.remove(logs.size() - 1);
    }

    public static List<String> getLogs() { return logs; }

    // Simula chegada de dados do CLP/MQTT (em produção, substituir por WebSocket real)
    public static void simularAtualizacao() {
        if (emEmergencia) return;
        java.util.Random r = new java.util.Random();

        // RF-05: novas peças
        int novas = r.nextInt(3);
        if (novas > 0) {
            pecasTotal += novas;
            int tipo = r.nextInt(3);
            if (tipo == 0) pecasVermelhas += novas;
            else if (tipo == 1) pecasVerdes += novas;
            else pecasAzuis += novas;
        }

        // RF-06: variação no tempo de ciclo
        tempoCiclo = Math.round((3.8 + r.nextDouble()) * 10.0) / 10.0;

        // RF-07: variação na taxa de acerto
        taxaAcerto = Math.round((96.0 + r.nextDouble() * 4.0) * 10.0) / 10.0;

        // RF-12: variação no OEE
        oee = Math.round((82.0 + r.nextDouble() * 8.0) * 10.0) / 10.0;

        // RF-13: variação na disponibilidade
        disponibilidade = Math.round((90.0 + r.nextDouble() * 5.0) * 10.0) / 10.0;

        // RF-02: troca de status do robô ocasionalmente
        String[] rampas = {"Rampa 1 em execucao","Rampa 2 em execucao","Rampa 3 em execucao","Aguardando peca"};
        if (r.nextInt(20) == 0) {
            statusRobo = rampas[r.nextInt(rampas.length)];
            adicionarLog("Status alterado: " + statusRobo);
        }

        // RF-11: log de peça processada
        if (novas > 0 && r.nextInt(5) == 0) {
            adicionarLog("Peca processada — ciclo: " + tempoCiclo + "s");
        }
    }

    public static int    getPecasTotal()       { return pecasTotal; }
    public static int    getPecasVermelhas()   { return pecasVermelhas; }
    public static int    getPecasVerdes()      { return pecasVerdes; }
    public static int    getPecasAzuis()       { return pecasAzuis; }
    public static double getTempoCiclo()       { return tempoCiclo; }
    public static double getTaxaAcerto()       { return taxaAcerto; }
    public static double getOee()              { return oee; }
    public static double getDisponibilidade()  { return disponibilidade; }

    protected static void setPecasTotal(int v)      { pecasTotal = v; }
    protected static void setPecasVermelhas(int v)  { pecasVermelhas = v; }
    protected static void setPecasVerdes(int v)     { pecasVerdes = v; }
    protected static void setPecasAzuis(int v)      { pecasAzuis = v; }
    protected static void setTempoCiclo(double v)   { tempoCiclo = v; }
    protected static void setTaxaAcerto(double v)   { taxaAcerto = v; }
    protected static void setOee(double v)          { oee = v; }
    protected static void setDisponibilidade(double v){ disponibilidade = v; }

    public static String getStatusRobo()       { return statusRobo; }
    public static String getStatusPrograma()   { return statusPrograma; }
    public static String getStatusComunicacao(){ return statusComunicacao; }
    public static String getStatusCamera()     { return statusCamera; }
    public static boolean isEmEmergencia()     { return emEmergencia; }

    // RF-01: aciona parada de emergência
    public static void acionarEmergencia() {
        emEmergencia = true;
        statusRobo = "PARADA DE EMERGÊNCIA";
        statusPrograma = "Desabilitado";
        adicionarLog("EMERGENCIA ACIONADA — sistema parado");
    }

    // RF-01: libera o sistema após emergência
    public static void liberarEmergencia() {
        emEmergencia = false;
        statusRobo = "Aguardando reinicio";
        statusPrograma = "Habilitado";
        adicionarLog("Emergencia liberada — aguardando operador");
    }

    protected static void setStatusRobo(String s)     { statusRobo = s; }
    protected static void setStatusPrograma(String s) { statusPrograma = s; }
    protected static void setEmEmergencia(boolean v)  { emEmergencia = v; }
}
