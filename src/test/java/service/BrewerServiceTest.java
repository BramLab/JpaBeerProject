package service;

import config.JpaConfig;
import model.Beer;
import model.Brewer;
import model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.junit.jupiter.api.*;

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
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
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