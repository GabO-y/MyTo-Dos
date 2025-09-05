package rz.springboot.mytodos.model.request;

//record é uma classe imutável que encapsula dados
//essa é usada para transferir dados de criação ou atualização de um card
public record CardRequest(String name, String description, Long ownerId) {
}
