package tacos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationHandler {

    // Method to serialize TacoOrder, including all Tacos.
    public void serializeTacoOrder(TacoOrder tacoOrder, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tacoOrder);
        }
    }

    // Method to deserialize TacoOrder, restoring all Tacos.
    public TacoOrder deserializeTacoOrder(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (TacoOrder) ois.readObject();
        }
    }
}



