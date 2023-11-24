package tacos;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import tacos.data.IngredientRepository;
import tacos.data.IngredientService;


@SpringBootApplication
public class TacoCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

    
    //for demonstration or testing purpose: check if the API works!
    //HTTP GET:
    @Bean
    public CommandLineRunner testIngredientService(IngredientService ingredientService) {
        return args -> {
            Ingredient ingredient = ingredientService.getIngredientById("FLTO");
            if (ingredient != null) {
                System.out.println("Ingredient found: " + ingredient);
            } else {
                System.out.println("Ingredient not found.");
            }
        };
    }
    //HTTP PUT:
    @Bean
    public CommandLineRunner testUpdateIngredient(IngredientService ingredientService) {
        return args -> {
            // Create an instance of Ingredient with the necessary data
            Ingredient ingredientToUpdate = new Ingredient();
            ingredientToUpdate.setId("FLTO"); // Set this to an existing ingredient's ID
            ingredientToUpdate.setName("BINGO");
            ingredientToUpdate.setType(Ingredient.Type.VEGGIES);

            // Call the updateIngredient method
            ingredientService.updateIngredient(ingredientToUpdate);

            System.out.println("Ingredient updated: " + ingredientToUpdate);
        };
    }

   
  //HTTP DELETE:
    @Bean
    public CommandLineRunner testDeleteIngredient(IngredientService ingredientService) {
        return args -> {
            // Create an instance of Ingredient with the necessary ID
            Ingredient ingredientToDelete = new Ingredient();
            ingredientToDelete.setId("FLTO"); // Set this to the ID of the ingredient you want to delete

            // Call the deleteIngredient method
            ingredientService.deleteIngredient(ingredientToDelete);

            System.out.println("Ingredient deletion requested for ID: " + ingredientToDelete.getId());
        };
    }


     //HTTP POST:
    @Bean
    public CommandLineRunner testCreateIngredient(IngredientService ingredientService) {
        return args -> {
            // Create a new Ingredient instance with the required details
            Ingredient newIngredient = new Ingredient("CHPT", "Chipotle", Ingredient.Type.SAUCE);
            
            // Call the createIngredient method
            Ingredient createdIngredient = ingredientService.createIngredient(newIngredient);

            // Output the result
            if (createdIngredient != null) {
                System.out.println("Ingredient created: " + createdIngredient);
            } else {
                System.out.println("Ingredient creation failed.");
            }
        };
    }

    @Bean
    public CommandLineRunner testCreateIngredient2(IngredientService ingredientService) {
        return args -> {
            // Create a new Ingredient instance with the required details
            Ingredient newIngredient = new Ingredient("CHPT", "Chipotle", Ingredient.Type.SAUCE);

            // Call the createIngredient2 method
            Ingredient createdIngredient = ingredientService.createIngredient2(newIngredient);

            // Output the result
            if (createdIngredient != null) {
                System.out.println("Ingredient created: " + createdIngredient);
            } else {
                System.out.println("Ingredient creation failed.");
            }
        };
    }


    
    
    
    
   /* @Bean
    public CommandLineRunner testAllIngredientsService(IngredientService ingredientService) {
        return args -> {
            List<Ingredient> ingredients = ingredientService.getAllIngredients();
            if (ingredients != null && !ingredients.isEmpty()) {
                System.out.println("Ingredients found:");
                ingredients.forEach(ingredient -> System.out.println(ingredient));
            } else {
                System.out.println("No ingredients found.");
            }
        };
    }*/


}