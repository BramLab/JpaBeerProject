package service;

import jakarta.persistence.EntityNotFoundException;
import model.Beer;
import repository.BeerRepository;

import java.util.List;
import java.util.Optional;

public class BeerService {

    private BeerRepository beerRepository = new BeerRepository();

    public void create(Beer entity) {
        beerRepository.create(entity);
    }

    public Optional<Beer> findById(long id){
        Optional<Beer> optionalEntity = Optional.empty();
        try{
            optionalEntity = beerRepository.findById(id);
        } catch(EntityNotFoundException ignored){
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return optionalEntity;
    }

    public List<Beer> findAll(){
        return beerRepository.findAll();
    }

    public void update(Beer beer){
        beerRepository.update(beer);
    }

    public void deleteById(long id){
        beerRepository.deleteById(id);
    }

}
