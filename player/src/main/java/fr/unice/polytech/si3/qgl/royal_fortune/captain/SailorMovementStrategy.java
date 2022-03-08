package fr.unice.polytech.si3.qgl.royal_fortune.captain;

import fr.unice.polytech.si3.qgl.royal_fortune.Sailor;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Ship;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.entities.Entities;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.entities.Oar;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.entities.Rudder;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SailorMovementStrategy {
    private List<Sailor> sailors;
    private Ship ship;

    private int nbAssociatedLeftSailors = 0;
    private int nbAssociatedRightSailors = 0;
    private boolean hasAssociatedSail = false;
    private boolean hasAssociatedRudder =false;

    public static final int MAX_MOVING_RANGE = 5;

    public SailorMovementStrategy(List<Sailor> sailors, Ship ship){
        this.sailors = sailors;
        this.ship = ship;
    }

    public void reset(){
        nbAssociatedLeftSailors = 0;
        nbAssociatedRightSailors = 0;
        hasAssociatedSail = false;
        hasAssociatedRudder = false;
    }

    public int getNbAssociations(){
        return nbAssociatedLeftSailors + nbAssociatedRightSailors
                + (hasAssociatedSail ? 1 : 0) + (hasAssociatedRudder ? 1 : 0);
    }

    /**
     * Will try to associate as best the requested parameters
     * Priority order : Rudder, Sail, Oar.
     *
     * @param sailorPlacement The preferred placement of the sailors.
     * @return ...
     */
    public SailorPlacement askPlacement(SailorPlacement sailorPlacement) {
        reset();
        int oarWeight = sailorPlacement.getOarWeight();
        boolean needRudder = sailorPlacement.hasRudder();
        boolean needSail = sailorPlacement.hasSail();

        /* We are associating every entity having only one sailor nearby
         * until every entity has more than 1 sailor nearby. */
        int nbAssociations = 0;
        onlyOnePossibleAssociation(oarWeight, needRudder, needSail);
        while (nbAssociations != getNbAssociations()){
            nbAssociations = getNbAssociations();
            onlyOnePossibleAssociation(oarWeight, needRudder, needSail);
        }

        // We are associating (if possible) the nearest sailor to the Rudder.
        if(needRudder && !hasAssociatedRudder){
            hasAssociatedRudder = associateNearestSailor(ship.getRudder());
        }

        // We are associating (if possible) the left or right oar to the nearest sailor according to the oarWeight.
        if(oarWeight > 0)
            nbAssociatedRightSailors = associateSailorToOar(DirectionsManager.RIGHT,
                    Math.abs(oarWeight) - nbAssociatedRightSailors);
        if(oarWeight < 0)
            nbAssociatedLeftSailors = associateSailorToOar(DirectionsManager.LEFT,
                    Math.abs(oarWeight) - nbAssociatedLeftSailors);
        System.out.println("Marins " + sailors.size());
        System.out.println("Marins restant " + (sailors.size() - getNbAssociations()));

        return new SailorPlacement(nbAssociatedLeftSailors, nbAssociatedRightSailors, hasAssociatedSail, hasAssociatedRudder);
    }

    /**
     * Will associate (if possible) the nearest sailors from the entity to it.
     * @param entity The entity to be associated.
     * @return The association has been proceeded.
     */
    public boolean associateNearestSailor(Entities entity){
        Optional<Sailor> nearestSailor = entity.getNearestSailor(sailors, MAX_MOVING_RANGE);

        if (nearestSailor.isEmpty())
            return false;

        nearestSailor.get().setTargetEntity(entity);
        return true;
    }

    /**
     * Will associate the sailor only if the entity has only one possible sailor in range.
     * @param entity The entity to associate.
     * @return Boolean, the association can be made.
     */
    public boolean associateTheOnlyOnePossible(Entities entity){
        List<Sailor> possibleSailors = entity.getSailorsInRange(sailors, MAX_MOVING_RANGE);

        if (possibleSailors.size() != 1)
            return false;

        possibleSailors.get(0).setTargetEntity(entity);
        return true;
    }

    /**
     * Will associate a sailor to an oar only if the oar has only one possible sailor in range.
     * @param direction LEFT - RIGHT, the direction to place the sailors to.
     * @param maxSailorsToAssociate The maximum of sailors to proceed the rotation.
     * @return The number of sailors successfully associated.
     */
    public int associateTheOnlyOnePossibleToOar(int direction, int maxSailorsToAssociate){
        List<Oar> oarList = ship.getOarList(direction);
        int oarIndex = 0;
        int successfullyAssociated = 0;

        while(oarIndex < oarList.size() && successfullyAssociated < maxSailorsToAssociate){
            if (associateTheOnlyOnePossible(oarList.get(oarIndex)))
                successfullyAssociated++;
            oarIndex++;
        }

        return successfullyAssociated;
    }

    /**
     * Associating (if possible) the nearest sailor from the oar until the maxSailorsToAssociate is reach or
     * until the list of oar is reached.
     *
     * @param direction LEFT/RIGHT - The direction to oar.
     * @param maxSailorsToAssociate The maximum of sailors required to turn the ship.
     * @return The number of associates sailors.
     */
    public int associateSailorToOar(int direction, int maxSailorsToAssociate){
        List<Oar> oarList = ship.getOarList(direction);
        int nbSuccessfulAssociation = 0;
        int i = 0;

        while(i < oarList.size() && nbSuccessfulAssociation < maxSailorsToAssociate) {
            Optional<Sailor> possibleSailor = oarList.get(i).getNearestSailor(sailors, MAX_MOVING_RANGE);
            if (possibleSailor.isPresent()) {
                oarList.get(i).setSailor(possibleSailor.get());
                nbSuccessfulAssociation++;
            }
            i++;
        }
        return nbSuccessfulAssociation;
    }

    /**
     * Will apply the onlyOnePossible methods.
     */
    public void onlyOnePossibleAssociation(int oarWeight, boolean needRudder , boolean needSail){
        Rudder rudder = ship.getRudder();

        if (needRudder)
            hasAssociatedRudder = associateTheOnlyOnePossible(rudder);

        if(oarWeight > 0)
            nbAssociatedRightSailors = associateTheOnlyOnePossibleToOar(DirectionsManager.RIGHT,
                    Math.abs(oarWeight) - nbAssociatedRightSailors);

        if(oarWeight < 0)
            nbAssociatedLeftSailors = associateTheOnlyOnePossibleToOar(DirectionsManager.LEFT,
                    Math.abs(oarWeight) - nbAssociatedLeftSailors);

    }
}