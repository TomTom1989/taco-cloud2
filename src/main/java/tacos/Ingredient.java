package tacos;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)

public class Ingredient implements Serializable {
	
 @Id
 private String id;
 
 private String name;
 private Type type;
 
 public static enum Type {
 WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
 }
}

