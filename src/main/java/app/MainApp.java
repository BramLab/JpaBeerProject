package app;

import config.JpaConfig;
import model.Beer;
import model.Brewer;
import model.Category;
import service.BeerService;
import service.BrewerService;
import service.CategoryService;

import static app.Helper.scanString;

public class MainApp {

    static BrewerService brewerService = new BrewerService();
    static CategoryService categoryService = new CategoryService();
    static BeerService beerService = new BeerService();

    public static void main(String[] args) {


        MainApp mainApp = new MainApp();

        insertTestData();

        Menu menu = new Menu("Hoofdmenu\n", "Keuze? ", "0");
        menu.addMenuOption("1", "Brouwerijen", () -> mainApp.manageBreweries() );
        menu.addMenuOption("0", "Exit", () -> {});
        menu.run();

        JpaConfig.shutdown();
    }

    void manageBreweries(){
        Menu menu = new Menu("Brouwerijen\n", "Keuze: ", "0");
        menu.addMenuOption("1", "Toevoegen", () -> addBrewery() );
        menu.addMenuOption("0", "Exit", () -> {});
        menu.run();

    }

    void addBrewery(){
        brewerService.create(new Brewer(scanString("Naam: "), scanString("Locatie: ")));
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
        Beer beer2 = new Beer("bier2", 1, 2, brewer2,  category2);
        Beer beer3 = new Beer("bier3", 1, 2, brewer3,  category3);
        beerService.create(beer1);
        beerService.create(beer2);
        beerService.create(beer3);
    }


}
