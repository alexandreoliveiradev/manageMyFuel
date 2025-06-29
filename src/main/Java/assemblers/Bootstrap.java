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

    public static void testBootstrap() throws ParseException { //remover function no deploy - guardar local
        Date date1 = Formatter.getDateFromString("15012023");
        Date date2 = Formatter.getDateFromString("15072023");
        Date date3 = Formatter.getDateFromString("15122023");

        Fuel fuel = new Fuel(date1, 31.1f, 1.75f, 100000, 31, "Repsol", 1);
        Fuel fuel2 = new Fuel(date2, 41.1f, 1.55f, 120000, 40, "Galp", 2);
        Fuel fuel3 = new Fuel(date3, 26.1f, 1.35f, 130000, 37, "Rede Energias", 3);

        Car car = new Car("carro", 'Y', "obs", "Gasolina 95", "alex", new Date());
        Car car2 = new Car("carro2", 'N', "obs", "Gasolina 95", "alex", new Date());

        car.fuels.add(fuel);
        car.fuels.add(fuel2);
        car2.fuels.add(fuel3);

        User user = new User("alex", "1234");
        user.cars.add(car);
        user.cars.add(car2);

        Setup.users.add(user);
    }

    public void loadUsers(JFrame frame){
        Setup.createUsersFromJson(frame);
    }
}
