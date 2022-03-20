package fr.unice.polytech.si3.qgl.royal_fortune;

import fr.unice.polytech.si3.qgl.royal_fortune.environment.shape.Rectangle;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RectangleTest {
    Rectangle rectangle;

    @BeforeEach
    void init() {
        rectangle=new Rectangle("rectangle", 50, 50, Math.PI/2);
    }
    @Test
    void rectangleInitialisionTest(){
        rectangle.setPosition(new Position(50,50,Math.PI/2));
    }

    @Test
    void positionIsInTheRectangleTest() {
        Rectangle rectangle = new Rectangle("Rectangle", 3, 5, 0);
        rectangle.setPosition(new Position(0, 0, 0));

        boolean isInRectangle = rectangle.positionIsInTheRectangle(new Position(0,0, 0));
        assertTrue(isInRectangle);
    }

    @Test
    void positionIsOnSideOfTheRectangleTest() {
        Rectangle rectangle = new Rectangle("Rectangle", 3, 5, 0);
        rectangle.setPosition(new Position(0, 0, 0));

        boolean isInRectangle = rectangle.positionIsInTheRectangle(new Position(1.5,0, 0));
        assertTrue(isInRectangle);
    }

    @Test
    void positionIsOnCornerOfTheRectangleTest() {
        Rectangle rectangle = new Rectangle("Rectangle", 3, 5, 0);
        rectangle.setPosition(new Position(0, 0, 0));

        boolean isInRectangle = rectangle.positionIsInTheRectangle(new Position(1.5,2.5, 0));
        assertTrue(isInRectangle);
    }

    @Test
    void positionIsNotInTheRectangleTest() {
        Rectangle rectangle = new Rectangle("Rectangle", 3, 5, 0);
        rectangle.setPosition(new Position(0, 0, 0));

        boolean isInRectangle = rectangle.positionIsInTheRectangle(new Position(1.6,0, 0));
        assertFalse(isInRectangle);
    }
}