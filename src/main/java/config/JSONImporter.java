package config;
//package be.intecbrussel.importer.data;

//import be.intecbrussel.config.JPABeerConfig;
//import be.intecbrussel.importer.dto.BeerImportDTO;
//import be.intecbrussel.model.*;
//import jakarta.json.bind.Jsonb;
//import jakarta.json.bind.JsonbBuilder;
import jakarta.persistence.EntityManager;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JSONImporter {
    private static final Logger logger = Logger.getLogger(JSONImporter.class.getName());
    private final EntityManager em;

    public JSONImporter() {
        this.em = JPABeerConfig.getEntityManager();
    }

    public void importAll() {
        logger.info("importing data");

        em.getTransaction().begin();
        try {
            importBrewers();
            importCategories();
            importBeers();

            em.getTransaction().commit();
            logger.info("import successful");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("import failed", e);
        }
    }


    private void importBrewers() {
        List<Brewer> brewers = loadJson("/data/brewers.json", Brewer.class);
        for (Brewer brewer : brewers) {
            em.persist(brewer);
        }
        logger.info(brewers.size() + " brewers successfully imported");
    }

    private void importCategories() {
        List<Category> categories = loadJson("/data/categories.json", Category.class);
        for (Category category : categories) {
            em.persist(category);
        }
        logger.info(categories.size() + " categories successfully imported");
    }

    private void importBeers() {
        // load all brewers and categories into maps keyed by name
        Map<String, Brewer> brewerMap = em.createQuery("SELECT b FROM Brewer b", Brewer.class)
                .getResultStream()
                .collect(Collectors.toMap(Brewer::getName, b -> b));

        Map<String, Category> categoryMap = em.createQuery("SELECT c FROM Category c", Category.class)
                .getResultStream()
                .collect(Collectors.toMap(Category::getName, c -> c));

        // load beers JSON
        List<BeerImportDTO> beers = loadJson("/data/beers.json", BeerImportDTO.class);
        int count = 0;

        for (BeerImportDTO dto : beers) {
            // find brewer by name instead of ID
            Brewer brewer = brewerMap.get(dto.brewer); // You need to change DTO to hold brewer name
            if (brewer == null) {
                throw new RuntimeException("brewer not found: " + dto.brewer);
            }

            Category category = categoryMap.get(dto.category); // Same here
            if (category == null) {
                throw new RuntimeException("category not found: " + dto.category);
            }

            Beer beer = new Beer();
            beer.setName(dto.name);
            beer.setAlcoholPercentage(dto.alcohol);
            beer.setPrice(dto.price);
            beer.setBrewer(brewer);
            beer.setCategory(category);

            em.persist(beer);
            count++;
        }
        logger.info(count + " beers successfully imported");
    }


    // helper method to actually load data in specific class type
    private <T> List<T> loadJson(String path, Class<T> typeClass) {
        Jsonb jsonb = JsonbBuilder.create();

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException(".json file not found: " + path);
            }
            // create a type for a list of the specific class type
            Type listType = new java.lang.reflect.ParameterizedType() {
                @Override
                public Type getRawType() {
                    return List.class;
                }
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{typeClass};
                }
                @Override
                public Type getOwnerType() {
                    return null;
                }
            };
            // deserialize json into a list of the specified class type
            return jsonb.fromJson(is, listType);
        } catch (Exception e) {
            throw new RuntimeException("error loading .json file: " + path, e);
        }
    }
}
