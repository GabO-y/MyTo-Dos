package rz.springboot.mytodos.model.dto;

//record é uma classe imutável que encapsula dados
//essa é usada para transferir dados de login
public record LoginRequest(String login, String password) {
}
