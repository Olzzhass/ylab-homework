package kaz.olzhas.ylab.entity;

import java.util.ArrayList;
import java.util.List;


//Класс пользователя тут пока что все по простому username and password
public class User {
    private String username;
    private String password;

    private List<Workspace> workspaceList;

    //Конструктор для пользователя
    public User(String username, String password) {
        workspaceList = new ArrayList<>();
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Workspace> getWorkspaceList() {
        return workspaceList;
    }

    public void setWorkspaceList(List<Workspace> workspaceList) {
        this.workspaceList = workspaceList;
    }
}
