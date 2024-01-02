package com.javarush;

import com.javarush.dao.*;
import com.javarush.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Main {
    private final SessionFactory sessionFactory;

    private final ActorDao actorDao;
    private final AddressDao addressDao;
    private final CategoryDao categoryDao;
    private final CityDao cityDao;
    private final CountryDao countryDao;
    private final CustomerDao customerDao;
    private final FilmDao filmDao;
    private final FilmTextDao filmTextDao;
    private final InventoryDao inventorDao;
    private final LanguageDao languageDao;
    private final PaymentDao paymentDao;
    private final RentalDao rentalDao;
    private final StaffDao staffDao;
    private final StoreDao storeDao;

    public Main() {
        Properties properties = getProperties();
        sessionFactory = new Configuration().
                addAnnotatedClass(Actor.class).
                addAnnotatedClass(Address.class).
                addAnnotatedClass(Category.class).
                addAnnotatedClass(City.class).
                addAnnotatedClass(Country.class).
                addAnnotatedClass(Customer.class).
                addAnnotatedClass(Features.class).
                addAnnotatedClass(Film.class).
                addAnnotatedClass(FilmText.class).
                addAnnotatedClass(Inventory.class).
                addAnnotatedClass(Language.class).
                addAnnotatedClass(Payment.class).
                addAnnotatedClass(Rating.class).
                addAnnotatedClass(Rental.class).
                addAnnotatedClass(Staff.class).
                addAnnotatedClass(Store.class).
                addProperties(properties).buildSessionFactory();

        actorDao = new ActorDao(sessionFactory);
        addressDao = new AddressDao(sessionFactory);
        categoryDao = new CategoryDao(sessionFactory);
        cityDao = new CityDao(sessionFactory);
        countryDao = new CountryDao(sessionFactory);
        customerDao = new CustomerDao(sessionFactory);
        filmDao = new FilmDao(sessionFactory);
        filmTextDao = new FilmTextDao(sessionFactory);
        inventorDao = new InventoryDao(sessionFactory);
        languageDao = new LanguageDao(sessionFactory);
        paymentDao = new PaymentDao(sessionFactory);
        rentalDao = new RentalDao(sessionFactory);
        staffDao = new StaffDao(sessionFactory);
        storeDao = new StoreDao(sessionFactory);


    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(AvailableSettings.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(AvailableSettings.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(AvailableSettings.USER, "root");
        properties.put(AvailableSettings.PASS, "root");
        properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(AvailableSettings.HBM2DDL_AUTO, "validate");
//        properties.put(AvailableSettings.SHOW_SQL, "true");
//        properties.put(AvailableSettings.FORMAT_SQL, "true");
//        properties.put(AvailableSettings.HIGHLIGHT_SQL, "true");
        return properties;
    }

    public static void main(String[] args) {
        Main main = new Main();
//        Customer customer = main.createCustomer();
//        main.customerReturnInventoryToStore();
//        main.customerRentInventory(customer);
        main.newFilmWasMade();
    }

    private void newFilmWasMade() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();

            Language language = languageDao.getItems(0, 20).stream().unordered().findAny().get();
            List<Category> categoryList = categoryDao.getItems(0, 5);
            List<Actor> actorList = actorDao.getItems(0, 20);

            Film film = new Film();
            film.setActors(new HashSet<>(actorList));
            film.setRating(Rating.PG13);
            film.setSpecialFeatures(Set.of(Features.DELETED_SCENES, Features.BEHIND_THE_SCENES));
            film.setLength((short) 198);
            film.setReplacementCost(BigDecimal.TEN);
            film.setRentalRate(BigDecimal.ONE);
            film.setLanguage(language);
            film.setDescription("Avatar 3.avi");
            film.setTitle("THE AVATAR 3");
            film.setRentalDuration((byte) 96);
            film.setOriginalLanguage(language);
            film.setCategories(new HashSet<>(categoryList));
            film.setYear(Year.now());
            filmDao.save(film);

            FilmText filmText = new FilmText();
            filmText.setId(film.getId());
            filmText.setFilm(film);
            filmText.setDescription("Avatar 3.avi");
            filmText.setTitle("THE AVATAR 3");
            filmTextDao.save(filmText);

            session.getTransaction().commit();
        }
    }

    private void customerRentInventory(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            Film film = filmDao.getFirstAvailableFilmForRent();
            Store store = storeDao.getItems(0, 1).get(0);

            Inventory inventory = new Inventory();
            inventory.setFilm(film);
            inventory.setStore(store);
            inventorDao.save(inventory);

            Staff staff = store.getStaff();

            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);

            rentalDao.save(rental);

            Payment payment = new Payment();
            payment.setRental(rental);
            payment.setCustomer(customer);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setAmount(BigDecimal.valueOf(1.99));
            payment.setStaff(staff);

            paymentDao.save(payment);

            session.getTransaction().commit();
        }
    }

    private void customerReturnInventoryToStore() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            Rental rental = rentalDao.getAnyUnreturnedRental();
            rental.setReturnDate(LocalDateTime.now());
            rentalDao.save(rental);
            session.getTransaction().commit();
        }
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            Store store = storeDao.getItems(0, 1).get(0);
            City city = cityDao.getByName("Kragujevac");

            Address address = new Address();
            address.setAddress("47 MySakila Drive");
            address.setPhone("14033335568");
            address.setCity(city);
            address.setDistrict("Nagasaki");
            addressDao.save(address);

            Customer customer = new Customer();
            customer.setActive(true);
            customer.setEmail("gluk.666@mail.ru");
            customer.setAddress(address);
            customer.setStore(store);
            customer.setFirstName("Ivan");
            customer.setLastName("Ivanov");
            customerDao.save(customer);

            session.getTransaction().commit();
            return customer;
        }
    }

}
