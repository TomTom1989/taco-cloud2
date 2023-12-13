package tacos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {
	

    public Ingredient(String id, String name, Type sauce) {
		this.id=id;
		this.name=name;
		this.type=sauce;
	}

	@Id
    private String id;
    private String name;
    private Type type;

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL)
    private List<Taco> tacos;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}



