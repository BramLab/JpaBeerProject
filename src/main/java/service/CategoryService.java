package service;

import jakarta.persistence.EntityNotFoundException;
import model.Category;
import repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    private CategoryRepository categoryRepository = new CategoryRepository();

    public void create(Category entity) {
        categoryRepository.create(entity);
    }

    public Optional<Category> findById(long id){
        Optional<Category> optionalEntity = Optional.empty();
        try{
            optionalEntity = categoryRepository.findById(id);
        } catch(EntityNotFoundException ignored){
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return optionalEntity;
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public void update(Category category){
        categoryRepository.update(category);
    }

    public void deleteById(long id){
        categoryRepository.deleteById(id);
    }
    
}
