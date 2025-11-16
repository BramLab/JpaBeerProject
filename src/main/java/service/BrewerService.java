package service;

import config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import model.Brewer;
import repository.BrewerRepository;

import java.util.List;
import java.util.Optional;

public class BrewerService {
    private BrewerRepository brewerRepository = new BrewerRepository();

    public void create(Brewer entity) {
        brewerRepository.create(entity);
        //brewerRepository.createGeneric((Brewer)entity);
    }

    public Optional<Brewer> findById(long id){
        Optional<Brewer> optionalEntity = Optional.empty();
        try{
            optionalEntity = brewerRepository.findById(id);
        } catch(EntityNotFoundException ignored){
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return optionalEntity;
    }

    public List<Brewer> findAll(){
        return brewerRepository.findAll();
    }

    public void update(Brewer brewer){
        brewerRepository.update(brewer);
    }

    public void deleteById(long id){
        brewerRepository.deleteById(id);
    }
}
