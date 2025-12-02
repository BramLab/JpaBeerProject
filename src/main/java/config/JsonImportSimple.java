package config;

import model.Beer;
import model.Category;
import org.json.*;
import service.CategoryService;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonImportSimple {

    public void importBeers(){

        CategoryService categoryService = new CategoryService();
        try{
            String content = new String(Files.readAllBytes(Paths.get(
                    "C:\\Users\\oifjq\\IdeaProjects\\JpaBeerProject\\src\\main\\java\\config\\Categories.json"
            )));
            JSONArray jsonArray = new JSONArray(content);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Category category = new Category(jsonObject.getString("name"), jsonObject.getString("description"));
                categoryService.create(category);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
