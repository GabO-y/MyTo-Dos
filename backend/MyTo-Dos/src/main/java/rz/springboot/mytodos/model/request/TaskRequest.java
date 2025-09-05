package rz.springboot.mytodos.model.request;

import java.time.LocalDateTime;

//record é uma classe imutável que encapsula dados
//essa é usada para transferir dados de criação ou atualização de uma tarefa
public record TaskRequest(String name, String description, String cardName, LocalDateTime expectedEnd) {
}
