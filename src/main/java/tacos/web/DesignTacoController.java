package tacos.web;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
	
private final IngredientRepository ingredientRepo;
private final TacoRepository tacoRepository;


public DesignTacoController(
IngredientRepository ingredientRepo,TacoRepository tacoRepository ) {
this.ingredientRepo = ingredientRepo;
this.tacoRepository= tacoRepository;

}

@ModelAttribute("tacoOrder")
public TacoOrder tacoOrder() {
    return new TacoOrder(); // This ensures a new TacoOrder is in the model if one isn't already
}
	
@ModelAttribute
public void addIngredientsToModel(Model model) {
    Iterable<Ingredient> ingredientsIterable = ingredientRepo.findAll();
    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
        List<Ingredient> ingredientsList = StreamSupport.stream(ingredientsIterable.spliterator(), false)
                .filter(ingredient -> ingredient.getType().equals(type))
                .collect(Collectors.toList());
        model.addAttribute(type.toString().toLowerCase(), ingredientsList);
    }
}



@GetMapping
public String showDesignForm(Model model) {
    if (!model.containsAttribute("tacoOrder")) {
        model.addAttribute("tacoOrder", new TacoOrder()); // This adds TacoOrder to the session
    }
    model.addAttribute("taco", new Taco()); // This adds a new Taco to the model for the form
    return "design";
}
 

@PostMapping
public String processTaco(@Valid Taco taco, Errors errors,
                          @ModelAttribute TacoOrder tacoOrder,
                          Model model) {
    if (errors.hasErrors()) {
        return "design";
    }

    Taco savedTaco = tacoRepository.save(taco);
    tacoOrder.addTacoName(savedTaco.getName());
    model.addAttribute("tacoOrder", tacoOrder);

    return "redirect:/orders/current";
}


 
 
}