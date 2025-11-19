package service;

import config.JpaConfig;
import model.Beer;
import model.Brewer;
import model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class BeerServiceTest {

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

    @Test
    void testCreate() {
        // Arrange
        insertTestData();
        Beer beer = new Beer("Roman Pils", 1.5F, 3.2F,
                brewerService.findById(1).get(), categoryService.findById(1).get());
        long idBefore = beer.getId();

        // Act
        beerService.create(beer);

        // Assert
        Assertions.assertTrue(idBefore == 0);
        Assertions.assertEquals(4, beer.getId());
    }

    @Test
    void testFindById() {
        insertTestData();

        Optional<Beer> beer = beerService.findById(1);

        Assertions.assertEquals("bier1", beer.get().getName());
        Assertions.assertEquals("brewer1", beer.get().getBrewer().getName());
        Assertions.assertEquals("category1", beer.get().getCategory().getName());
    }

    @Test
    void testFindAll() {
        insertTestData();

        List<Beer> beers = beerService.findAll();

        Assertions.assertEquals(3, beers.size());
        Assertions.assertEquals("bier1", beers.get(0).getName());
        Assertions.assertEquals(2.0F, beers.get(0).getPrice());
        Assertions.assertEquals("bier2", beers.get(1).getName());
        Assertions.assertEquals("bier3", beers.get(2).getName());
    }

    @Test
    void testUpdate() {
        insertTestData();
        Beer beer = beerService.findById(3).get();
        beer.setName("NAME");
        beer.setAlcoholPercentage(9.5F);

        beerService.update(beer);

        Beer updatedBeer = beerService.findById(3).get();
        Assertions.assertEquals("NAME", updatedBeer.getName());
        Assertions.assertEquals(9.5F, updatedBeer.getAlcoholPercentage());
    }

    @Test
    void testDeleteById_expectSuccess() {
        // Arrange
        insertTestData();
        assertTrue(beerService.findById(2).isPresent());

        // Act
        beerService.deleteById(2);

        // Assert
        assertFalse(beerService.findById(2).isPresent());
    }

    @Test
    void testDeleteById_entityNotFound(){
        insertTestData();

        Exception thrown = assertThrows(app.FeedbackToUserException.class,
                () -> {  beerService.deleteById(555);  }
        );
        assertEquals("Element niet gevonden. Bier id 555", thrown.getMessage());
    }

    private static void insertTestData() {
        Brewer brewer1 = new Brewer("brewer1", "Bx");
        Brewer brewer2 = new Brewer("brewer2", "Antwerpen");
        Brewer brewer3 = new Brewer("brewer3", "Gent");
        Category category1 = new Category("category1", "descr1");
        Category category2 = new Category("category2", "descr2");
        Category category3 = new Category("category3", "descr3");
        brewerService.create(brewer1);
        brewerService.create(brewer2);
        brewerService.create(brewer3);
        categoryService.create(category1);
        categoryService.create(category2);
        categoryService.create(category3);
        Beer beer1 = new Beer("bier1", 1, 2, brewer1,  category1);
        Beer beer2 = new Beer("bier2", 1, 2, brewer1,  category3);
        Beer beer3 = new Beer("bier3", 1, 2, brewer3,  category3);
        beerService.create(beer1);
        beerService.create(beer2);
        beerService.create(beer3);
    }

}