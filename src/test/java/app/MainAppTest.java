package app;

import config.JpaConfig;
import model.Brewer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.junit.jupiter.api.*;
import service.BeerService;
import service.BrewerService;
import service.CategoryService;

import static org.junit.jupiter.api.Assertions.*;


class MainAppTest {

    static BrewerService brewerService;
    static CategoryService categoryService;
    static BeerService beerService;
    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeEach
    void setUp() {
        brewerService = new BrewerService();
        categoryService = new CategoryService();
        beerService = new BeerService();

        JpaConfig.getEntityManagerFactory()
                .unwrap(SessionFactoryImplementor.class)
                .getSchemaManager()
                .truncateMappedObjects();
    }

    @AfterEach
    void tearDown() {
        brewerService = null;
        categoryService = null;
        beerService = null;
    }

    @AfterAll
    static void tearDownAll() {
        // https://stackoverflow.com/questions/9903341/cleanup-after-all-junit-tests
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Running Shutdown Hook");
            JpaConfig.shutdown();
        }));
    }
    //TODO: mock scanner...
    // https://stackoverflow.com/questions/31635698/junit-testing-for-user-input-using-scanner/31635953#31635953

//    @Test
//    void manageBreweries() {
//    }
//
//    @Test
//    void addBrewery() {
//    }
//
//    @Test
//    void findBrewery() {
//    }
//
//    @Test
//    void findAllBreweries() {
//    }
//
//    @Test
//    void updateBrewery() {
//    }
//
//    @Test
//    void deleteBrewery() {
//    }
//
//    @Test
//    void manageBeers() {
//    }
//
//    @Test
//    void addBeer() {
//    }
//
//    @Test
//    void findBeer() {
//    }
//
//    @Test
//    void findAllBeers() {
//    }
//
//    @Test
//    void updateBeer() {
//    }
//
//    @Test
//    void deleteBeer() {
//    }
//
//    @Test
//    void manageCategories() {
//    }
//
//    @Test
//    void addCategory() {
//    }
//
//    @Test
//    void findCategory() {
//    }
//
//    @Test
//    void findAllCategories() {
//    }
//
//    @Test
//    void updateCategory() {
//    }
//
//    @Test
//    void deleteCategory() {
//    }
}