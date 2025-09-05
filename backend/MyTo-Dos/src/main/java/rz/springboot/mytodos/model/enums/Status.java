package rz.springboot.mytodos.model.enums;

//enumeração que define os status possíveis de uma tarefa
public enum Status {
    //status inicial quando a tarefa é criada (""a fazer"")
    DOING,
    //status quando a tarefa é concluída
    COMPLETED,
    //status quando a tarefa está atrasada
    LATE
}
