package tacos.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tacos.Ingredient;

@Service
public class IngredientService {
	private static final Logger log = LoggerFactory.getLogger(IngredientService.class);
    private final RestTemplate restTemplate;
 

    public IngredientService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
   
    }

    // GET HTTP request with getForObject:
    
    /*public Ingredient getIngredientById(String ingredientId) {
        return restTemplate.getForObject(
            "http://localhost:8080/data-api/ingredients/{id}",
            Ingredient.class, ingredientId);
    }*/
    
 
    //GET HTTP request with getForEntity:
    public Ingredient getIngredientById(String ingredientId) {
    	 ResponseEntity<Ingredient> responseEntity =
    	 restTemplate.getForEntity("http://localhost:8080/data-api/ingredients/{id}",
    	 Ingredient.class, ingredientId);
    	 log.info("Fetched time: " +
    	 responseEntity.getHeaders().getDate());
    	 return responseEntity.getBody();
    	}
    
    
    
    
    
    //PUT HTTP request:
    public void updateIngredient(Ingredient ingredient) {
    	 restTemplate.put("http://localhost:8080/data-api/ingredients/{id}",
    	 ingredient, ingredient.getId());
    	}
    
    
    //DELETE HTTP request:
    public void deleteIngredient(Ingredient ingredient) {
    	 restTemplate.delete("http://localhost:8080/data-api/ingredients/{id}",
    	 ingredient.getId());
    	}

    
    //HTTP PUT:
    public Ingredient createIngredient(Ingredient ingredient) {
    	 return restTemplate.postForObject("http://localhost:8080/data-api/ingredients",
    	 ingredient, Ingredient.class);
    	}
    
    
    public Ingredient createIngredient2(Ingredient ingredient) {
    	 ResponseEntity<Ingredient> responseEntity =
    	 restTemplate.postForEntity("http://localhost:8080/data-api/ingredients",
    	 ingredient,
    	 Ingredient.class);
    	 log.info("New resource created at " +
    	 responseEntity.getHeaders().getLocation());
    	 return responseEntity.getBody();
    	}
    
    
   /* public List<Ingredient> getAllIngredients() {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "http://localhost:8080/data-api/ingredients", String.class);
            
            System.out.println("Response: " + responseEntity.getBody()); // 

            IngredientCollectionDTO response = restTemplate.getForObject(
                "http://localhost:8080/data-api/ingredients",
                IngredientCollectionDTO.class);

            return response != null ? response.getIngredients() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }*/


    
  
    
    }

