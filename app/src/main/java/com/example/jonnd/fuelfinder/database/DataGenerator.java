package com.example.jonnd.fuelfinder.database;

import com.example.jonnd.fuelfinder.entities.FillUp;
import com.example.jonnd.fuelfinder.entities.Station;
import com.example.jonnd.fuelfinder.entities.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final String [] FUEL_TYPE = new String []{
            "Regular (87)", "Super (89)","Premium (91)"
    };

    private static final String [] FIRST_NAMES = new String[] {
            "Mitch", "Rick", "Paul", "James", "Larry", "Clay",
            "Peter", "Brian", "Randy", "Trevor", "Cory", "Jacob",
            "Laura", "Clair", "Bridget", "Brittney", "Gillian", "Jenny",
            "Jane", "Lisa", "Joan", "Hillary", "Gillian", "Emily"
    };

    private static final String [] LAST_NAMES = new String[] {
            "Kramer", "Davis", "Davisson", "Jackson", "Boone", "Blair",
            "Petersson", "Brians", "Valentine", "MacDonalds", "Gramhe", "Kripke",
            "Laura", "Clair", "Bridget", "Brittney", "Gillian", "Jenny",
            "Douglas", "Bonet", "Gadot", "Toole", "Bluth", "Thompson"
    };

    private static final String [] EMAIL_DOMAINS = {
            "aol.com","gmail.com","hotmail.com", "grovemail.com", "yahoo.com"
    };

    private static final String [] STATIONS = new String [] {
            "BP","Chevron", "Citgo", "Exxon" , "Flying J",
            "Marathon", "QuikStop", "Shell", "Sunoco", "Speedway",
            "Swifty", "Tesco", "Valero", "Wawa", "Kroger"
    };

    public List<User> generateUsers() {
        List<User> users = new ArrayList<>(FIRST_NAMES.length);
        Random rnd = new Random();
        for (int i = 0; i < FIRST_NAMES.length; i++) {
            User user = new User();
            String first = FIRST_NAMES[rnd.nextInt(FIRST_NAMES.length)];
            String last = LAST_NAMES[rnd.nextInt(FIRST_NAMES.length)];
            user.setFirstName(first);
            user.setLastName(last);
            user.setUserName(first.toLowerCase() + last);
            user.setDateCreated(new Date());
            users.add(user);
        }
        return users;
    }

    public List<Station> generateStations() {
        List<Station> stations = new ArrayList<>(STATIONS.length);
        Random rnd = new Random();
        for (int i = 0; i < STATIONS.length; i++) {
            Station station = new Station();
            station.setStationName(STATIONS[rnd.nextInt(STATIONS.length)]);
            int zipCodeBase = 32000;
            station.setAddress(rnd.nextInt(1200) + " " + FIRST_NAMES[rnd.nextInt(FIRST_NAMES.length)] + " Street "+ (zipCodeBase + rnd.nextInt(10000))
                    +  " Steubenville ohio");
            stations.add(station);
        }
        return stations;
    }

    public List<FillUp> generateFillUps() {
        List<FillUp> fillUps = new ArrayList<>(5);
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            FillUp newFillUp = new FillUp();
            newFillUp.setUserId(1);
            newFillUp.setStationId(1);
            newFillUp.setFuelType(FUEL_TYPE[rnd.nextInt(FUEL_TYPE.length)]);

            Calendar cal = Calendar.getInstance();
            // Force the year to be 2018, but generate pseudorandom values for the
            // month, day of m, h, m & sec. Use the appropriate bounds for the
            // relevant unit. etc, the bound for second should be '60'.
            int year = 2018;
            int month = rnd.nextInt(12) + 1;
            int day = month != 2 ? rnd.nextInt(30) : rnd.nextInt(28);
            int h = rnd.nextInt(24);
            int m = rnd.nextInt(60);
            int s = rnd.nextInt(60);
            cal.set(year, month, day, h, m, s);
            newFillUp.setDate(cal.getTime());
            newFillUp.setNumberOfGallons(rnd.nextInt(15));
            double price = (rnd.nextInt(3) + 1) +  rnd.nextDouble();
            newFillUp.setPricePerGallon(price);
            fillUps.add(newFillUp);
        }
        return fillUps;
    }
}
