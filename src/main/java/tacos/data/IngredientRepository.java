package tacos.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import tacos.Ingredient;


public interface IngredientRepository extends JpaRepository<Ingredient, String> {
}
