package br.com.fiap.globalsolution.globalsight_api.entity;

public enum SimulationStatus {
    PENDING_USER_INPUT,     // Dados iniciais salvos, aguardando acionamento da IA
    PROCESSING_IA,          // Enviado para a IA, aguardando resposta
    IA_PREDICTION_SUCCESS,  // IA retornou com sucesso a predição
    IA_PREDICTION_FAILED,   // IA falhou ao processar ou houve erro na comunicação
    DRONE_SIMULATION_READY, // Simulação do drone foi gerada com base na predição
    COMPLETED,              // Todo o processo foi finalizado com sucesso
    ERROR                   // Erro genérico no processamento pela API Java
}