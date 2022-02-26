package fr.unice.polytech.si3.qgl.royal_fortune;

import fr.unice.polytech.si3.qgl.royal_fortune.captain.DirectionsManager;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Deck;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Ship;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.entities.Entities;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.entities.Oar;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.shape.Circle;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.shape.Rectangle;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DirectionsManagerTest {
    private DirectionsManager dirMan;
    private ArrayList<Entities> entities;

    @BeforeEach
    public void init(){
        entities = new ArrayList<>();
    }

    @Test
    //Positive angle
    void angleCalculatorTest() {
        Ship ship = new Ship(
                "ship",
                100,
                new Position(0, 0, 0),
                "ShipTest",
                new Deck(3, 4),
                entities,
                new Rectangle("rectangle", 3, 4, 0));

        Checkpoint checkpoint = new Checkpoint(new Position(0, 50, 0), new Circle("Circle", 50));

        ArrayList<Checkpoint> checkpointArrayList = new ArrayList<>();
        checkpointArrayList.add(checkpoint);

        Goal goal = new Goal("REGATTA", checkpointArrayList);

        dirMan = new DirectionsManager(ship, goal);

        double angle = dirMan.angleCalculator()[0];
        assertEquals(Math.PI/2, angle);
    }

    @Test
    void isInConeTest() {
        dirMan = new DirectionsManager(null, null);
        assertTrue(dirMan.isInCone(0.5,1));
        assertFalse(dirMan.isInCone(1,0.5));
    }

    @Test
    void isConeTooSmallTest() {
        //6 entities
        entities.add(new Oar("oar", 1, 0));
        entities.add(new Oar("oar", 2, 0));

        entities.add(new Oar("oar", 1, 3));
        entities.add(new Oar("oar", 2, 3));

       Ship ship = new Ship(
                "ship",
                100,
                new Position(0, 0, 1.5),
                "ShipTest",
                new Deck(3, 4),
                entities,
                new Rectangle("rectangle", 3, 4, 0));

        dirMan = new DirectionsManager(ship, null);
        assertTrue(dirMan.isConeTooSmall(0.5,0.2));
        assertFalse(dirMan.isConeTooSmall(0.5,0.3));
    }

}