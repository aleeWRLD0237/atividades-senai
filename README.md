# Pedro Alexandre

Sistema de monitoramento industrial — Pedro Alexandre, desenvolvido em Java puro (sem frameworks, sem Maven).

## Tecnologias

- Java (JDK 11+) — `com.sun.net.httpserver.HttpServer`
- HTML + CSS + JavaScript nativos
- Sem dependências externas

## Como rodar

### Linux / Mac
```bash
./compilar_e_rodar.sh
```

### Windows
```bat
compilar_e_rodar.bat
```

Acesse: **http://localhost:8080**

## Estrutura

```
src/
  Servidor.java          — Ponto de entrada, HttpServer na porta 8080
  DadosRobo.java         — Modelo de estado e simulação de telemetria
  DadosHandler.java      — GET /api/dados → JSON com telemetria
  EmergenciaHandler.java — POST /api/emergencia → parada/liberação
  PaginaHandler.java     — GET / → interface HTML completa
```

## Requisitos atendidos

| ID | Descrição |
|----|-----------|
| RF-01 | Parada de Emergência com overlay e tempo de resposta |
| RF-02 | Status do robô em tempo real |
| RF-03 | Status do programa |
| RF-04 | Status da comunicação |
| RF-05 | Contagem de peças processadas |
| RF-06 | Tempo de ciclo |
| RF-07 | Taxa de acerto |
| RF-10 | Status da câmera |
| RF-11 | Logs em tempo real |
| RF-12 | OEE |
| RF-13 | Disponibilidade mecânica |
| RF-14 | Distribuição de peças por cor |
| RF-15 | Navegação entre páginas |
| RF-17 | Relógio em tempo real |
| RNF-01 | Interface dark mode |
| RNF-03 | Atualização via polling (1s) |
| RNF-07 | Funciona em qualquer navegador moderno |
| RNF-09 | Logs com timestamp |
| RNF-10 | Perfis de acesso |
| RNF-11 | Tempo de resposta emergência < 20ms |
