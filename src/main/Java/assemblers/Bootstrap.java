package assemblers;

import domains.Car;
import domains.Fuel;
import domains.User;
import utils.Formatter;

import javax.swing.*;
import java.text.ParseException;
import java.util.Date;

public class Bootstrap {

    public Bootstrap(JFrame frame){
        loadUsers(frame);
    }

    public void loadUsers(JFrame frame){
        Setup.createUsersFromJson(frame);
    }
}
