package fr.unice.polytech.si3.qgl.royal_fortune.action;

public class LiftSailAction extends SailAction{
    public static final String LIFT = "LIFT_SAIL";

    public LiftSailAction(int sailorId) {
        super(sailorId, LIFT);
        this.type = LIFT;
    }

    public LiftSailAction(){}

}
