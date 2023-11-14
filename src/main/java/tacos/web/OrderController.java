package tacos.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import tacos.TacoOrder;
import tacos.service.TacoOrderService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final TacoOrderService orderService;

    public OrderController(TacoOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/current")
    public String orderForm(Model model) {
        model.addAttribute("tacoOrder", new TacoOrder());
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        orderService.placeOrder(order); // Use the service layer to place the order
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
