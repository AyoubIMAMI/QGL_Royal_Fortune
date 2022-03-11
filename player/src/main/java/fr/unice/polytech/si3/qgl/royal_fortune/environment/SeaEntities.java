package fr.unice.polytech.si3.qgl.royal_fortune.environment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.unice.polytech.si3.qgl.royal_fortune.action.*;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Reef.class, name = "reef"),
        @JsonSubTypes.Type(value = Stream.class, name = "stream"),

})
public class SeaEntities {
    Position position;
    String type;
    public SeaEntities(Position position,String type){
        this.position=position;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }
}