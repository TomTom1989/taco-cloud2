package tacos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.data.TacoRepository;
import tacos.data.OrderRepository;

@Service
public class TacoOrderService {

    private final OrderRepository orderRepo;
    private final TacoRepository tacoRepository;

    public TacoOrderService(OrderRepository orderRepo, TacoRepository tacoRepository) {
        this.orderRepo = orderRepo;
        this.tacoRepository = tacoRepository;
    }

    @Transactional
    public void placeOrder(TacoOrder order) {
        // Save the order to generate the ID, if not already saved
        if (order.getId() == null) {
            orderRepo.save(order);
        }
        
        for (Taco taco : order.getTacos()) {
            // Associate tacos with the saved order
            taco.setTacoOrder(order);
            // Save each taco which now includes a reference to the saved order
            tacoRepository.save(taco);
        }
    }
}

