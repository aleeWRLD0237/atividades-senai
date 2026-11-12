import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Serve o HTML completo da interface supervisória via rota /.
 * RNF-01: Dark Mode  |  RNF-07: Funciona em qualquer navegador moderno
 */
public class PaginaHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange req) throws IOException {
        String html = gerarHTML();
        req.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = html.getBytes("UTF-8");
        req.sendResponseHeaders(200, bytes.length);
        OutputStream out = req.getResponseBody();
        out.write(bytes);
        out.close();
    }

    private String gerarHTML() {
        return cabecalho() + corpo() + scripts() + rodape();
    }

    // ── Cabeçalho com <head> e abertura do <body> ──
    private String cabecalho() {
        return """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Pedro Alexandre</title>
<style>
*{margin:0;padding:0;box-sizing:border-box;}
body{font-family:'Segoe UI',system-ui,sans-serif;background:#0f0f17;color:#dde1f0;display:flex;flex-direction:column;height:100vh;overflow:hidden;}
/* ── Topbar ── */
.topbar{display:flex;align-items:center;justify-content:space-between;padding:0 20px;height:58px;background:#16162a;border-bottom:1px solid #2a2a4a;flex-shrink:0;gap:12px;z-index:100;}
.topbar-left,.topbar-right{display:flex;align-items:center;gap:14px;}
.btn-menu{background:none;border:none;color:#dde1f0;font-size:20px;cursor:pointer;padding:6px 8px;border-radius:8px;transition:background .15s;}
.btn-menu:hover{background:#2a2a4a;}
.brand{display:flex;align-items:center;gap:10px;font-size:22px;}
.brand-title{font-size:14px;font-weight:700;color:#c5b4fa;}
.brand-sub{font-size:11px;color:#6e7191;}
.btn-emergencia{background:#7c1fa8;color:#fff;border:2px solid #a855f7;padding:8px 18px;border-radius:20px;font-size:13px;font-weight:700;cursor:pointer;letter-spacing:.5px;transition:all .2s;}
.btn-emergencia:hover{background:#6b1b92;box-shadow:0 0 14px #a855f755;}
.btn-emergencia.ativo{background:#4a0070;animation:pulso 1s infinite;}
@keyframes pulso{0%,100%{opacity:1;}50%{opacity:.55;}}
.relogio{font-family:'Courier New',monospace;font-size:14px;color:#a78bfa;min-width:76px;text-align:center;background:#1e1e38;padding:4px 10px;border-radius:8px;}
.notif-wrapper{position:relative;cursor:pointer;font-size:20px;padding:4px 8px;border-radius:8px;transition:background .15s;}
.notif-wrapper:hover{background:#2a2a4a;}
.notif-badge{position:absolute;top:-2px;right:-2px;background:#e040fb;color:#fff;font-size:10px;border-radius:50%;width:16px;height:16px;display:flex;align-items:center;justify-content:center;font-weight:700;}
.notif-dropdown{display:none;position:absolute;right:0;top:38px;background:#1e1e38;border:1px solid #2a2a4a;border-radius:12px;width:270px;z-index:200;box-shadow:0 8px 28px #00000066;}
.notif-dropdown.visivel{display:block;}
.notif-header{padding:10px 14px;font-weight:600;border-bottom:1px solid #2a2a4a;font-size:13px;color:#a78bfa;}
.notif-item{padding:10px 14px;font-size:13px;border-bottom:1px solid #1a1a30;}
.notif-item.aviso{color:#fbbf24;}
.notif-item.info{color:#67e8f9;}
.notif-item.ok{color:#34d399;}
.usuario{display:flex;align-items:center;gap:8px;cursor:pointer;position:relative;padding:4px 10px;border-radius:20px;border:1px solid #2a2a4a;transition:background .15s;}
.usuario:hover{background:#2a2a4a;}
.avatar{width:28px;height:28px;background:linear-gradient(135deg,#7c3aed,#a855f7);border-radius:50%;display:flex;align-items:center;justify-content:center;font-weight:700;font-size:12px;}
.user-menu{display:none;position:absolute;right:0;top:46px;background:#1e1e38;border:1px solid #2a2a4a;border-radius:12px;width:170px;z-index:200;box-shadow:0 8px 28px #00000066;}
.user-menu.visivel{display:block;}
.user-menu-item{padding:10px 14px;font-size:13px;cursor:pointer;border-radius:8px;margin:4px;}
.user-menu-item:hover{background:#2a2a4a;}
.user-menu-item.sair{color:#f87171;}
/* ── Layout ── */
.layout{display:flex;flex:1;overflow:hidden;}
/* ── Sidebar ── */
.sidebar{width:230px;background:#16162a;border-right:1px solid #2a2a4a;display:flex;flex-direction:column;padding:14px 0;transition:width .2s;flex-shrink:0;}
.sidebar.recolhida{width:60px;}
.sidebar.recolhida .sidebar-label{display:none;}
.sidebar-nav{flex:1;}
.sidebar-item{display:flex;align-items:center;gap:12px;padding:11px 18px;cursor:pointer;border-radius:12px;margin:3px 10px;font-size:14px;transition:background .15s;}
.sidebar-item:hover{background:#1e1e38;}
.sidebar-item.ativo{background:linear-gradient(90deg,#3b1fa820,#7c3aed22);border-left:3px solid #a855f7;color:#c4b5fd;}
.sidebar-versao{padding:14px 18px;font-size:11px;color:#3e3e6a;}
/* ── Conteúdo ── */
.conteudo{flex:1;overflow-y:auto;padding:24px;}
.pagina{display:none;}
.pagina.ativa{display:block;}
.pag-titulo{font-size:20px;font-weight:700;margin-bottom:20px;color:#c4b5fd;}
/* ── Barra de Status ── */
.barra-status{display:flex;gap:14px;margin-bottom:18px;flex-wrap:wrap;}
.status-card{background:#16162a;border:1px solid #2a2a4a;border-radius:16px;padding:14px 20px;display:flex;align-items:center;gap:14px;flex:1;min-width:200px;}
.indicador{width:11px;height:11px;border-radius:50%;flex-shrink:0;}
.indicador.verde{background:#34d399;}
.indicador.roxo{background:#a855f7;box-shadow:0 0 8px #a855f788;}
.indicador.vermelho{background:#f87171;animation:pulso 1s infinite;}
.status-label{font-size:11px;color:#6e7191;margin-bottom:3px;}
.status-valor{font-size:13px;font-weight:600;}
.badge{padding:3px 10px;border-radius:20px;font-size:12px;font-weight:600;}
.badge.verde{background:#0d2e22;color:#34d399;}
.badge.amarelo{background:#2e2000;color:#fbbf24;}
.badge.vermelho{background:#2e0a0a;color:#f87171;}
/* ── Métricas ── */
.metricas-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(160px,1fr));gap:14px;margin-bottom:18px;}
.metrica-card{background:#16162a;border:1px solid #2a2a4a;border-radius:16px;padding:18px;text-align:center;}
.metrica-label{font-size:11px;color:#6e7191;margin-bottom:8px;}
.metrica-valor{font-size:30px;font-weight:700;}
.metrica-valor.verde{color:#34d399;}
.metrica-valor.roxo{color:#a855f7;}
.metrica-valor.laranja{color:#fbbf24;}
.metrica-un{font-size:11px;color:#6e7191;margin-top:4px;}
/* ── Grid inferior ── */
.grid-inferior{display:grid;grid-template-columns:1fr 1fr 1fr;gap:14px;}
@media(max-width:900px){.grid-inferior{grid-template-columns:1fr;}}
.painel{background:#16162a;border:1px solid #2a2a4a;border-radius:16px;overflow:hidden;}
.painel-header{padding:13px 18px;font-size:13px;font-weight:600;border-bottom:1px solid #2a2a4a;display:flex;justify-content:space-between;align-items:center;color:#c4b5fd;}
.cam-ok{font-size:12px;font-weight:400;color:#34d399;}
/* ── Câmera ── */
.camera-view{position:relative;height:160px;background:#0a0a14;display:flex;align-items:center;justify-content:center;}
.cam-placeholder{display:flex;flex-direction:column;align-items:center;color:#3e3e6a;}
.cam-spin{width:36px;height:36px;border:3px solid #2a2a4a;border-top-color:#a855f7;border-radius:50%;animation:spin 1s linear infinite;}
@keyframes spin{to{transform:rotate(360deg);}}
.rec-badge{position:absolute;top:8px;right:8px;background:#e040fb;color:#fff;font-size:11px;font-weight:700;padding:2px 8px;border-radius:6px;display:flex;align-items:center;gap:4px;}
.rec-ponto{width:7px;height:7px;background:#fff;border-radius:50%;animation:pulso 1s infinite;}
/* ── Peças por cor ── */
.peca-linha{display:flex;align-items:center;gap:8px;margin-bottom:12px;}
.peca-cor{width:14px;height:14px;border-radius:4px;flex-shrink:0;}
.peca-nm{width:70px;font-size:13px;}
.barra-wrap{flex:1;height:8px;background:#1e1e38;border-radius:4px;overflow:hidden;}
.barra{height:100%;border-radius:4px;transition:width .5s;}
.peca-qt{width:40px;text-align:right;font-size:13px;font-weight:600;}
/* ── Logs ── */
.logs-lista{padding:12px;max-height:160px;overflow-y:auto;}
.log-item{font-size:12px;font-family:'Courier New',monospace;color:#6e7191;padding:4px 0;border-bottom:1px solid #1e1e38;}
.logs-completo{padding:16px;max-height:600px;overflow-y:auto;}
.logs-completo .log-item{font-size:13px;padding:6px 0;}
/* ── Formulário ── */
.form-card{background:#16162a;border:1px solid #2a2a4a;border-radius:16px;padding:28px;max-width:520px;}
.form-linha{display:flex;flex-direction:column;gap:5px;margin-bottom:18px;}
.form-linha label{font-size:12px;color:#6e7191;}
.form-input{background:#0f0f17;border:1px solid #2a2a4a;border-radius:10px;color:#dde1f0;padding:9px 14px;font-size:14px;width:100%;}
.form-input:focus{outline:none;border-color:#a855f7;}
.btn-salvar{background:linear-gradient(90deg,#7c3aed,#a855f7);color:#fff;border:none;padding:11px 26px;border-radius:20px;font-size:14px;font-weight:600;cursor:pointer;}
.btn-salvar:hover{opacity:.88;}
/* ── Overlay Emergência ── */
.overlay-em{display:none;position:fixed;inset:0;background:#00000099;z-index:999;align-items:center;justify-content:center;}
.overlay-em.visivel{display:flex;}
.modal-em{background:#1a0a2e;border:2px solid #a855f7;border-radius:20px;padding:44px;text-align:center;max-width:420px;animation:aparecer .3s ease;}
@keyframes aparecer{from{transform:scale(.8);opacity:0;}to{transform:scale(1);opacity:1;}}
.modal-em h2{color:#e040fb;margin:16px 0 8px;font-size:22px;}
.modal-em p{color:#6e7191;margin-bottom:26px;}
.btn-liberar{background:linear-gradient(90deg,#7c3aed,#a855f7);color:#fff;border:none;padding:12px 34px;border-radius:20px;font-size:16px;font-weight:700;cursor:pointer;}
.btn-liberar:hover{opacity:.88;}
</style>
</head>
<body>
""";
    }

    // ── Corpo HTML com topbar, sidebar e páginas ──
    private String corpo() {
        return topbar() + "<div class=\"layout\">" + sidebar() + conteudo() + "</div>";
    }

    private String topbar() {
        return """
<header class="topbar">
  <div class="topbar-left">
    <button onclick="toggleSidebar()" class="btn-menu">&#9776;</button>
    <div class="brand">
      <span>🤖</span>
      <div>
        <div class="brand-title">Pedro Alexandre</div>
        <span class="brand-sub">Sistema de Monitoramento Industrial</span>
      </div>
    </div>
  </div>
  <button class="btn-emergencia" id="btnEmergencia" onclick="acionarEmergencia()">⬛ PARADA EMERGÊNCIA</button>
  <div class="topbar-right">
    <div class="relogio" id="relogio">00:00:00</div>
    <div class="notif-wrapper" onclick="toggleNotif()">
      🔔<span class="notif-badge" id="badgeNotif">3</span>
      <div class="notif-dropdown" id="dropNotif">
        <div class="notif-header">Notificações</div>
        <div class="notif-item aviso">⚠ Tempo de ciclo acima do limite</div>
        <div class="notif-item info">ℹ Câmera CAM-01 reconectada</div>
        <div class="notif-item ok">✓ Backup de logs realizado</div>
      </div>
    </div>
    <div class="usuario" onclick="toggleUserMenu()">
      <div class="avatar">U</div><span>Usuário</span>
      <div class="user-menu" id="userMenu">
        <div class="user-menu-item" onclick="ir('perfil')">👤 Perfil</div>
        <div class="user-menu-item sair" onclick="sair()">🚪 Sair</div>
      </div>
    </div>
  </div>
</header>
""";
    }

    private String sidebar() {
        return """
<aside class="sidebar" id="sidebar">
  <nav class="sidebar-nav">
    <div class="sidebar-item ativo" onclick="ir('dashboard')" data-pag="dashboard">
      <span>📊</span><span class="sidebar-label">Painel Geral</span>
    </div>
    <div class="sidebar-item" onclick="ir('perfil')" data-pag="perfil">
      <span>👤</span><span class="sidebar-label">Perfil e Cadastro</span>
    </div>
    <div class="sidebar-item" onclick="ir('configuracoes')" data-pag="configuracoes">
      <span>⚙</span><span class="sidebar-label">Configurações</span>
    </div>
    <div class="sidebar-item" onclick="ir('logs')" data-pag="logs">
      <span>📋</span><span class="sidebar-label">Logs Completos</span>
    </div>
  </nav>
  <div class="sidebar-versao">v1.0.0</div>
</aside>
""";
    }

    private String conteudo() {
        return "<main class=\"conteudo\" id=\"conteudo\">"
            + pagDashboard() + pagPerfil() + pagConfig() + pagLogs()
            + "</main>"
            + overlayEmergencia();
    }

    private String pagDashboard() {
        return """
<div class="pagina ativa" id="pag-dashboard">
  <div class="barra-status">
    <div class="status-card"><div class="indicador roxo" id="indRobo"></div>
      <div><div class="status-label">Status do Robô (RF-02)</div>
      <div class="status-valor" id="statusRobo">Rampa 3 em execução</div></div></div>
    <div class="status-card"><div>
      <div class="status-label">Programa (RF-03)</div>
      <div><span class="badge verde" id="badgePrograma">Habilitado</span></div></div></div>
    <div class="status-card"><div>
      <div class="status-label">Comunicação (RF-04)</div>
      <div><span class="badge verde" id="badgeComm">Online</span></div></div></div>
  </div>
  <div class="metricas-grid">
    <div class="metrica-card"><div class="metrica-label">Peças Processadas (RF-05)</div>
      <div class="metrica-valor" id="pecasTotal">1.257</div><div class="metrica-un">peças</div></div>
    <div class="metrica-card"><div class="metrica-label">Tempo de Ciclo (RF-06)</div>
      <div class="metrica-valor laranja" id="tempoCiclo">4.2</div><div class="metrica-un">seg/peça</div></div>
    <div class="metrica-card"><div class="metrica-label">Taxa de Acerto (RF-07)</div>
      <div class="metrica-valor verde" id="taxaAcerto">98.5</div><div class="metrica-un">%</div></div>
    <div class="metrica-card"><div class="metrica-label">OEE (RF-12)</div>
      <div class="metrica-valor roxo" id="oeeVal">85.0</div><div class="metrica-un">%</div></div>
    <div class="metrica-card"><div class="metrica-label">Disponib. Mecânica (RF-13)</div>
      <div class="metrica-valor roxo" id="dispMec">92.3</div><div class="metrica-un">%</div></div>
  </div>
  <div class="grid-inferior">
    <div class="painel">
      <div class="painel-header">📹 CAM-01 — Célula Robótica
        <span class="cam-ok" id="statusCam">🟢 Processando</span></div>
      <div class="camera-view">
        <div class="cam-placeholder"><div class="cam-spin"></div>
          <span style="font-size:12px;margin-top:8px;">Feed ao vivo — CAM-01</span></div>
        <div class="rec-badge"><div class="rec-ponto"></div> REC</div>
      </div>
    </div>
    <div class="painel">
      <div class="painel-header">🎨 Peças por Cor (RF-14)</div>
      <div style="padding:16px;">
        <div class="peca-linha"><div class="peca-cor" style="background:#f87171;"></div>
          <div class="peca-nm">Vermelhas</div>
          <div class="barra-wrap"><div class="barra" id="bVerm" style="width:42%;background:#f87171;"></div></div>
          <div class="peca-qt" id="qVerm">528</div></div>
        <div class="peca-linha"><div class="peca-cor" style="background:#34d399;"></div>
          <div class="peca-nm">Verdes</div>
          <div class="barra-wrap"><div class="barra" id="bVerd" style="width:35%;background:#34d399;"></div></div>
          <div class="peca-qt" id="qVerd">440</div></div>
        <div class="peca-linha"><div class="peca-cor" style="background:#818cf8;"></div>
          <div class="peca-nm">Azuis</div>
          <div class="barra-wrap"><div class="barra" id="bAzul" style="width:23%;background:#818cf8;"></div></div>
          <div class="peca-qt" id="qAzul">289</div></div>
        <div style="padding-top:10px;font-size:13px;color:#6e7191;">Total: <strong id="totPecas">1.257</strong></div>
      </div>
    </div>
    <div class="painel">
      <div class="painel-header">📋 Logs do Robô (RF-11)</div>
      <div class="logs-lista" id="logsLista"></div>
    </div>
  </div>
</div>
""";
    }

    private String pagPerfil() {
        return """
<div class="pagina" id="pag-perfil">
  <div class="pag-titulo">👤 Perfil e Cadastro</div>
  <div class="form-card">
    <div class="form-linha"><label>Nome</label>
      <input class="form-input" type="text" placeholder="Seu nome completo"></div>
    <div class="form-linha"><label>E-mail</label>
      <input class="form-input" type="email" placeholder="seu@email.com"></div>
    <div class="form-linha"><label>Perfil (RNF-10)</label>
      <select class="form-input"><option>Operador</option><option>Técnico</option><option>Administrador</option></select></div>
    <button class="btn-salvar">Salvar Alterações</button>
  </div>
</div>
""";
    }

    private String pagConfig() {
        return """
<div class="pagina" id="pag-configuracoes">
  <div class="pag-titulo">⚙ Configurações</div>
  <div class="form-card">
    <div class="form-linha"><label>Endpoint WebSocket (RNF-03)</label>
      <input class="form-input" type="text" placeholder="ws://192.168.1.100:8765"></div>
    <div class="form-linha"><label>MQTT Broker</label>
      <input class="form-input" type="text" placeholder="mqtt://192.168.1.100:1883"></div>
    <div class="form-linha"><label>Latência máx. vídeo ms (RNF-02)</label>
      <input class="form-input" type="number" value="200"></div>
    <div class="form-linha"><label>Intervalo atualização ms (RNF-03)</label>
      <input class="form-input" type="number" value="100"></div>
    <button class="btn-salvar">Salvar</button>
  </div>
</div>
""";
    }

    private String pagLogs() {
        return """
<div class="pagina" id="pag-logs">
  <div class="pag-titulo">📋 Logs Completos (RNF-09)</div>
  <div class="logs-completo" id="logsCompleto"></div>
</div>
""";
    }

    private String overlayEmergencia() {
        return """
<div class="overlay-em" id="overlayEm">
  <div class="modal-em">
    <div style="font-size:60px;">🛑</div>
    <h2>PARADA DE EMERGÊNCIA ACIONADA</h2>
    <p>Resposta ao CLP em <strong id="tempoMs">— ms</strong></p>
    <button class="btn-liberar" onclick="liberarEmergencia()">Liberar Sistema</button>
  </div>
</div>
""";
    }

    // ── Scripts JavaScript ──
    private String scripts() {
        return """
<script>
// ── RF-17: Relógio em tempo real ──
function atualizarRelogio() {
  const agora = new Date();
  const h = String(agora.getHours()).padStart(2, '0');
  const m = String(agora.getMinutes()).padStart(2, '0');
  const s = String(agora.getSeconds()).padStart(2, '0');
  document.getElementById('relogio').textContent = h + ':' + m + ':' + s;
}
setInterval(atualizarRelogio, 1000);
atualizarRelogio();

// ── RF-15: Navegação entre páginas ──
function ir(pag) {
  document.querySelectorAll('.pagina').forEach(p => p.classList.remove('ativa'));
  document.querySelectorAll('.sidebar-item').forEach(i => i.classList.remove('ativo'));
  const p = document.getElementById('pag-' + pag);
  if (p) p.classList.add('ativa');
  const item = document.querySelector('[data-pag="' + pag + '"]');
  if (item) item.classList.add('ativo');
  fecharMenus();
}

function toggleSidebar() {
  document.getElementById('sidebar').classList.toggle('recolhida');
}

function fecharMenus() {
  document.getElementById('dropNotif').classList.remove('visivel');
  document.getElementById('userMenu').classList.remove('visivel');
}

function toggleNotif() {
  document.getElementById('dropNotif').classList.toggle('visivel');
  document.getElementById('userMenu').classList.remove('visivel');
}

function toggleUserMenu() {
  document.getElementById('userMenu').classList.toggle('visivel');
  document.getElementById('dropNotif').classList.remove('visivel');
}

function sair() { if (confirm('Deseja realmente sair?')) location.reload(); }

document.addEventListener('click', e => {
  if (!e.target.closest('.notif-wrapper') && !e.target.closest('.usuario')) fecharMenus();
});

// ── RF-01: Parada de Emergência ──
let emEmergencia = false;

function acionarEmergencia() {
  if (emEmergencia) return;
  fetch('/api/emergencia', { method:'POST', body:'acionar' })
    .then(r => r.json())
    .then(d => {
      emEmergencia = true;
      document.getElementById('overlayEm').classList.add('visivel');
      document.getElementById('tempoMs').textContent = d.tempoMs + ' ms';
      document.getElementById('btnEmergencia').classList.add('ativo');
    });
}

function liberarEmergencia() {
  fetch('/api/emergencia', { method:'POST', body:'liberar' })
    .then(r => r.json())
    .then(() => {
      emEmergencia = false;
      document.getElementById('overlayEm').classList.remove('visivel');
      document.getElementById('btnEmergencia').classList.remove('ativo');
    });
}

// ── RNF-03: Polling de telemetria a cada 1 segundo ──
function atualizarDados() {
  fetch('/api/dados')
    .then(r => r.json())
    .then(d => {
      document.getElementById('statusRobo').textContent = d.statusRobo || '—';
      atualizarBadge('badgePrograma', d.statusPrograma);
      atualizarBadge('badgeComm', d.statusComunicacao);
      atualizarIndicador('indRobo', d.statusRobo);

      document.getElementById('pecasTotal').textContent = fmt(d.pecasTotal);
      document.getElementById('tempoCiclo').textContent = (d.tempoCiclo || 0).toFixed(1);
      document.getElementById('taxaAcerto').textContent = (d.taxaAcerto || 0).toFixed(1);
      document.getElementById('oeeVal').textContent = (d.oee || 0).toFixed(1);
      document.getElementById('dispMec').textContent = (d.disponibilidade || 0).toFixed(1);

      const tot = d.pecasTotal || 1;
      atualizarBarra('bVerm', 'qVerm', d.pecasVermelhas, tot);
      atualizarBarra('bVerd', 'qVerd', d.pecasVerdes, tot);
      atualizarBarra('bAzul', 'qAzul', d.pecasAzuis, tot);
      document.getElementById('totPecas').textContent = fmt(d.pecasTotal);

      const cam = document.getElementById('statusCam');
      if (cam) cam.textContent = d.statusCamera === 'online' ? '🟢 Processando' : '🔴 Offline';

      if (d.logs && d.logs.length) {
        const lista = document.getElementById('logsLista');
        const completo = document.getElementById('logsCompleto');
        lista.innerHTML = d.logs.slice(-6).reverse()
          .map(l => '<div class="log-item">' + escHTML(l) + '</div>').join('');
        completo.innerHTML = d.logs.slice().reverse()
          .map(l => '<div class="log-item">' + escHTML(l) + '</div>').join('');
      }

      if (d.emEmergencia && !emEmergencia) {
        emEmergencia = true;
        document.getElementById('overlayEm').classList.add('visivel');
        document.getElementById('btnEmergencia').classList.add('ativo');
      }
    })
    .catch(() => {});
}

function fmt(n) { return (n || 0).toLocaleString('pt-BR'); }
function escHTML(s) {
  return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;')
    .replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}

function atualizarBadge(id, val) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = val || '—';
  el.className = 'badge';
  if (!val) return;
  const v = val.toLowerCase();
  if (v.includes('hab') || v.includes('online') || v.includes('exec')) el.classList.add('verde');
  else if (v.includes('stand') || v.includes('espera')) el.classList.add('amarelo');
  else el.classList.add('vermelho');
}

function atualizarIndicador(id, val) {
  const el = document.getElementById(id);
  if (!el) return;
  el.className = 'indicador';
  if (!val) return;
  const v = val.toLowerCase();
  if (v.includes('exec') || v.includes('prod')) el.classList.add('roxo');
  else if (v.includes('stand') || v.includes('ocioso')) el.classList.add('verde');
  else el.classList.add('vermelho');
}

function atualizarBarra(idBarra, idQt, qtd, total) {
  const pct = total > 0 ? Math.round((qtd / total) * 100) : 0;
  const b = document.getElementById(idBarra);
  const q = document.getElementById(idQt);
  if (b) b.style.width = pct + '%';
  if (q) q.textContent = fmt(qtd);
}

setInterval(atualizarDados, 1000);
atualizarDados();
</script>
""";
    }

    private String rodape() { return "</body></html>"; }
}
