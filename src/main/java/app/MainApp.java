package app;

import config.JpaConfig;
import model.Beer;
import model.Brewer;
import model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.BeerService;
import service.BrewerService;
import service.CategoryService;
import java.util.List;
import java.util.Optional;
import static app.InputHelper.*;

public class MainApp {

    static BrewerService brewerService = new BrewerService();
    static CategoryService categoryService = new CategoryService();
    static BeerService beerService = new BeerService();

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {

        LOGGER.error("Logger test `{}`", "iets");

        MainApp mainApp = new MainApp();

        insertTestData();

        Menu menu = new Menu("\nHoofdmenu\n", "Keuze: ", "0");
        menu.addMenuOption("1", "Brouwerijen", () -> mainApp.manageBreweries() );
        menu.addMenuOption("2", "Bieren", () -> mainApp.manageBeers() );
        menu.addMenuOption("3", "Categorieën", () -> mainApp.manageCategories() );
        menu.addMenuOption("0", "Exit", () -> {});
        menu.run();

        JpaConfig.shutdown();
    }


    void manageBreweries(){
        Menu menu = new Menu("\nBrouwerijen\n", "Keuze: ", "0");
        menu.addMenuOption("1", "Toevoegen", () -> addBrewery() );
        menu.addMenuOption("2", "Zoeken", () -> findBrewery() );
        menu.addMenuOption("3", "Toon alle", () -> findAllBreweries() );
        menu.addMenuOption("4", "Aanpassen", () -> updateBrewery() );
        menu.addMenuOption("5", "Verwijder", () -> deleteBrewery() );
        menu.addMenuOption("0", "Exit", () -> {});
        menu.run();
    }

    void addBrewery(){
        brewerService.create(new Brewer(scanString("Naam: "), scanString("Locatie: ")));
    }

    void findBrewery() {
        Optional<Brewer> brewer = brewerService.findById(scanLong("Id: "));
        if (brewer.isPresent()) {
            System.out.println(brewer.get());
            for (Beer beer : brewer.get().getBeers()) {
                System.out.println("  " + beer);
            }
        }else {
            throw new FeedbackToUserException("Brouwer niet gevonden.");
        }
    }

    void findAllBreweries(){
        List<Brewer> breweries = brewerService.findAll();
        for(Brewer brewer : breweries){
            System.out.println(brewer.toString());
        }
    }

    void updateBrewery(){
        Optional<Brewer> optionalBrewer = brewerService.findById(scanLong("Id: "));
        if (optionalBrewer.isPresent()){
            Brewer brewer = optionalBrewer.get();
            System.out.println(brewer);
            brewer.setName(scanString("Naam: "));
            brewer.setLocation(scanString("Locatie: "));
            brewerService.update(brewer);
        } else {
            throw new FeedbackToUserException("Brouwer niet gevonden.");
        }
    }

    void deleteBrewery(){
        brewerService.deleteById(scanLong("Id: "));
    }


    void manageBeers(){
        Menu menu = new Menu("\nBieren\n", "Keuze: ", "0");
        menu.addMenuOption("1", "Toevoegen", () -> addBeer() );
        menu.addMenuOption("2", "Zoeken", () -> findBeer() );
        menu.addMenuOption("3", "Toon alle", () -> findAllBeers() );
        menu.addMenuOption("4", "Aanpassen", () -> updateBeer() );
        menu.addMenuOption("5", "Verwijder", () -> deleteBeer() );
        menu.addMenuOption("6", "Toon per category", () -> findBeersByCategory() );
        menu.addMenuOption("0", "Exit", () -> {});
        menu.run();
    }

    void addBeer(){
        beerService.create(new Beer(scanString("Naam: "),
                scanFloat("alcoholPercentage: "),
                scanFloat("prijs: "),
                brewerService.findById(scanInt("id brouwerij: ")).get(),
                categoryService.findById(scanInt("id categorie: ")).get()
        ));
    }

    void findBeer(){
        Optional<Beer> beer = beerService.findById(scanLong("Id: "));
        if (beer.isPresent()) {
            System.out.println(beer.get());
            System.out.println(beer.get().getCategory());
            System.out.println(beer.get().getBrewer());
        }else {
            System.out.println("Niet gevonden.");
        }
    }

    void findAllBeers(){
        List<Beer> beers = beerService.findAll();
        for(Beer beer : beers){
            System.out.println(beer.toString());
        }
    }

    void findBeersByCategory(){
        List<Beer> beers = beerService.findByCategoryId(scanLong("Id: "));
        for(Beer beer : beers){
            System.out.println(beer.toString());
        }
    }

    void updateBeer(){
        Optional<Beer> optionalBeer = beerService.findById(scanLong("Id: "));
        if (optionalBeer.isPresent()){
            Beer beer = optionalBeer.get();
            System.out.println(beer);
            beer.setName(scanString("Naam: "));
            beer.setAlcoholPercentage(scanFloat("alcoholPercentage: "));
            beer.setPrice(scanFloat("prijs: "));
            beer.setCategory(categoryService.findById(scanInt("id categorie: ")).get());
            beer.setBrewer(brewerService.findById(scanInt("id brouwerij: ")).get());
            beerService.update(optionalBeer.get());
        } else {
            System.out.println("Niet gevonden.");
        }
    }

    void deleteBeer(){
        Optional<Beer> beer = beerService.findById(scanLong("Id: "));
        if (beer.isPresent()){
            System.out.println(beer.get());
            beerService.deleteById(beer.get().getId());
        } else {
            System.out.println("Niet gevonden.");
        }
    }

    void manageCategories(){
        Menu menu = new Menu("\nCategoriën\n", "Keuze: ", "0");
        menu.addMenuOption("1", "Toevoegen", () -> addCategory() );
        menu.addMenuOption("2", "Zoeken", () -> findCategory() );
        menu.addMenuOption("3", "Toon alle", () -> findAllCategories() );
        menu.addMenuOption("4", "Aanpassen", () -> updateCategory() );
        menu.addMenuOption("5", "Verwijder", () -> deleteCategory() );
        menu.addMenuOption("0", "Exit", () -> {});
        menu.run();
    }

    void addCategory(){
        categoryService.create(new Category(scanString("Naam: "), scanString("Omschrijving: ")));
    }

    void findCategory(){
        Optional<Category> category = categoryService.findById(scanLong("Id: "));
        if (category.isPresent()) {
            System.out.println(category.get());
            for (Beer beer : category.get().getBeers()) {
                System.out.println("  " + beer);
            }
        }else {
            System.out.println("Niet gevonden.");
        }
    }

    void findAllCategories(){
        List<Category> categories = categoryService.findAll();
        for(Category category : categories){
            System.out.println(category.toString());
        }
    }

    void updateCategory(){
        Optional<Category> category = categoryService.findById(scanLong("Id: "));
        if (category.isPresent()){
            System.out.println(category.get());
            category.get().setName(scanString("Naam: "));
            category.get().setDescription(scanString("Omschrijving: "));
            categoryService.update(category.get());
        } else {
            System.out.println("Niet gevonden.");
        }
    }

    void deleteCategory(){
        Optional<Category> category = categoryService.findById(scanLong("Id: "));
        if (category.isPresent()){
            System.out.println(category.get());
            categoryService.deleteById(category.get().getId());
        } else {
            System.out.println("Niet gevonden.");
        }
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
