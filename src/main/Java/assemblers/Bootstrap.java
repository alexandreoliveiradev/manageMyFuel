package assemblers;


import javax.swing.*;

public class Bootstrap {

    public Bootstrap(JFrame frame){
        loadUsers(frame);
    }

    public void loadUsers(JFrame frame){
        Setup.createUsersFromJson(frame);
    }
}
