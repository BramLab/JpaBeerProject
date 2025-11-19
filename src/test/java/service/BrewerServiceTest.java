package service;

import config.JpaConfig;
import jakarta.persistence.RollbackException;
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

class BrewerServiceTest {

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
    void create() {
        // Arrange
        Brewer brewer = new Brewer("Roman", "Oudenaarde");
        long idBefore = brewer.getId();

        // Act
        brewerService.create(brewer);

        // Assert
        Assertions.assertTrue(idBefore == 0);
        Assertions.assertEquals(1, brewer.getId());
    }

    @Test
    void findById() {
        insertTestData();

        Optional<Brewer> brewer = brewerService.findById(1);

        Assertions.assertTrue(brewer.get().getName().equals("brewer1"));
        Assertions.assertEquals(2, brewer.get().getBeers().size());
    }

    @Test
    void findAll() {
        insertTestData();

        List<Brewer> brewers = brewerService.findAll();

        Assertions.assertEquals(3, brewers.size());
        Assertions.assertEquals("brewer1", brewers.get(0).getName());
        Assertions.assertEquals("Bx", brewers.get(0).getLocation());
        Assertions.assertEquals("brewer2", brewers.get(1).getName());
        Assertions.assertEquals("brewer3", brewers.get(2).getName());
    }

    @Test
    void update() {
        Brewer brewer = brewerService.findById(3).get();
        brewer.setName("NAME");
        brewer.setLocation("LOCATION");

        brewerService.update(brewer);

        Brewer updatedBrewer = brewerService.findById(3).get();
        Assertions.assertEquals("NAME", updatedBrewer.getName());
        Assertions.assertEquals("LOCATION", updatedBrewer.getLocation());
    }

    @Test
    void update__fresh_element__ExpectNoInsert_OnlyException() {
        insertTestData();
        List<Brewer> brewersBefore = brewerService.findAll();
        int countBefore = brewersBefore.size();
        Brewer brewer = new Brewer("Roman", "Oudenaarde");
        brewer.setId(666);

        brewerService.update(brewer);
//        Exception thrown = assertThrows(app.FeedbackToUserException.class,
//                () -> {  brewerService.update(brewer);  }
//        );
        List<Brewer> brewersAfter = brewerService.findAll();
        int countAfter = brewersAfter.size();

//        assertEquals("Element niet gevonden. Brouwer id 666", thrown.getMessage());
        assertEquals(countBefore, countAfter);
    }

    @Test
    void update_element_changed_inbetween_expectException() {
        insertTestData();
        Brewer brewerV1 = brewerService.findById(1).get();
        brewerV1.setName("NAME CHANGED v1");

                // but meanwhile somebody else changes it too ...
                Brewer brewerV2 = brewerService.findById(1).get();
                brewerV2.setName("NAME CHANGED v2");
                brewerService.update(brewerV2);

        brewerService.update(brewerV1);

        Brewer updatedBrewer = brewerService.findById(1).get();
        Assertions.assertEquals("EXPECT EXCEPTION", updatedBrewer.getName());
    }

    @Test
    void deleteById_expectSuccess() {
        // Arrange
        insertTestData();
        assertTrue(brewerService.findById(2).isPresent());

        // Act
        brewerService.deleteById(2);

        // Assert
        assertFalse(brewerService.findById(2).isPresent());
    }

    // DeleteById seems not very safe -> if someone else just updated element values, it will still be deleted.
    // What if someone is in the process of updating?
    // Is Delete by element safer? -> it holds its latest (~transaction/~session?) state.
    // We could verify that the service first loads the latest version and then deletes that one?
    @Test
    void deleteById_entityIs_Being_ChangedInOtherTransaction_soInvalidTransaction(){
        assertTrue(false);
    }

    @Test
    void deleteById_entityNotFound(){
        insertTestData();

        Exception thrown = assertThrows(app.FeedbackToUserException.class,
                () -> {  brewerService.deleteById(666);  }
        );
        assertEquals("Element niet gevonden. Brouwer id 666", thrown.getMessage());
    }

    @Test
    void deleteById_entityInUseByOtherElement__FK() {
        insertTestData();
        assertTrue(brewerService.findById(1).isPresent());

        Exception thrown = assertThrows(app.FeedbackToUserException.class,
                () -> {  brewerService.deleteById(1);  }
        );
        assertEquals("Dit element wordt nog voor andere elementen gebruikt. Brouwer id 1", thrown.getMessage());
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