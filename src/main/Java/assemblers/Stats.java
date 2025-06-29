package assemblers;

import utils.Formatter;

public class Stats {

    public static float getMonthSpend(String username, String carName, boolean filtered, int month){
        if (filtered){
            return Setup.getCarMonthSpend(username, carName, month);
        } else {
            return Setup.getCarMonthSpend(username, null, month);
        }
    }

    public static float getYearSpend(String username, String carName, boolean filtered){
        float output = 0.f;
        for (int i = 0; i < 12; i++){
            output += getMonthSpend(username, carName, filtered, Formatter.getCurrentMonth() - i);
        }
        return output*100/100;
    }

    public static float getMonthMinPrice(String username, String carName, boolean filtered, int month){
        if (filtered){
            return Setup.getMonthMinPrice(username, carName, month);
        } else {
            return Setup.getMonthMinPrice(username, null, month);
        }
    }

    public static float getMonthMaxPrice(String username, String carName, boolean filtered, int month){
        if (filtered){
            return Setup.getMonthMaxPrice(username, carName, month);
        } else {
            return Setup.getMonthMaxPrice(username, null, month);
        }
    }

    public static float getYearMaxPrice(String username, String carName, boolean filtered){
        float output = 0.0f;
        float max;
        for (int i = 0; i < 12; i++){
            max = getMonthMaxPrice(username, carName, filtered, Formatter.getCurrentMonth() - i);
            if ( max > output) {
                output = max;
            }
        }
        return output;
    }

    public static float getYearMinPrice(String username, String carName, boolean filtered){
        float output = 100.0f;
        float min = 0.0f;
        for (int i = 0; i < 12; i++){
            min = getMonthMinPrice(username, carName, filtered, Formatter.getCurrentMonth() - i);
            if ( min < output && min != 0.0f) {
                output = min;
            }
        }
        return Math.min(output, 100.0f);
    }

    public static int getMonthNumberFuels(String username, String carName, boolean filtered, int month){
        if (filtered){
            return Setup.getMonthNumberFuels(username, carName, month);
        } else {
            return Setup.getMonthNumberFuels(username, null, month);
        }
    }

    public static int getYearNumberFuels(String username, String carName, boolean filtered){
        int output = 0;
        for (int i = 0; i < 12; i++){
            output += getMonthNumberFuels(username, carName, filtered, Formatter.getCurrentMonth() - i);
        }
        return output;
    }

}
