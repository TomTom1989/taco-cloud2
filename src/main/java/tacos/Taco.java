package tacos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Taco implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ManyToOne
    private TacoOrder tacoOrder;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
    private Date createdAt = new Date();
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @ManyToMany()
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
    
    public void setTacoOrder(TacoOrder tacoOrder) {
        this.tacoOrder = tacoOrder;
    }
    
    @Override
    public String toString() {
        return "Taco{" +
               // "tacoOrder=" + tacoOrder + // Commented out to prevent recursion
               // other fields...
               '}';
    }
}
