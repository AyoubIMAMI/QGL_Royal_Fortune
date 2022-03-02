package fr.unice.polytech.si3.qgl.royal_fortune.tooling.simulation;

import fr.unice.polytech.si3.qgl.royal_fortune.Checkpoint;
import fr.unice.polytech.si3.qgl.royal_fortune.Cockpit;
import fr.unice.polytech.si3.qgl.royal_fortune.Goal;
import fr.unice.polytech.si3.qgl.royal_fortune.Sailor;
import fr.unice.polytech.si3.qgl.royal_fortune.dao.InitGameDAO;
import fr.unice.polytech.si3.qgl.royal_fortune.action.Action;
import fr.unice.polytech.si3.qgl.royal_fortune.json_management.JsonManager;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Ship;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.shape.Circle;

import java.util.List;
import java.util.logging.Logger;

public class Game {
    Ship ship;
    Cockpit cockpit;
    List<Sailor> sailors;
    Goal goal;
    Referee referee;
    final Logger logger = Logger.getLogger(Game.class.getName());
    int i=0;
    public Game(String initialiser){
    	
    	InitGameDAO initGameDAO = JsonManager.readInitGameDAOJson(initialiser);
        sailors = initGameDAO.getSailors();
        goal = initGameDAO.getGoal();
        cockpit = new Cockpit();
        referee=new Referee(cockpit);
        cockpit.initGame(initialiser);
        goal=cockpit.getGoal();
        ship = cockpit.getShip();

    }

    void nextRound(){
        String jsonNextRound=createJson();
        logger.info("-----------------------");
        String out = "jsonNextRound="+jsonNextRound;
        logger.info(out);
        i++;
        if (i==45)
            i++;
        String jsonverif=cockpit.nextRound(jsonNextRound);
        out = "jsonverif="+jsonverif;
        logger.info(out);
        logger.info("-----------------------");

        List<Action> actions=JsonManager.readActionJson(jsonverif);
        logger.info(String.valueOf(actions));
        this.ship = referee.makeAdvance(cockpit,actions);

    }

    public String createJson() {
        return "{\"ship\":"+ cockpit.getShip().toString()+"}";
    }

    @Override
    public String toString() {
        //"Orientation: "+ship.getPosition().getOrientation()+'\n';
        return cockpit.getShip().getPosition().getX()+";"+cockpit.getShip().getPosition().getY()+";"+ship.getPosition().getOrientation()+'\n';
    }

    public boolean isFinished() {
        double distanceSCX = goal.getCurrentCheckPoint().getPosition().getX() - ship.getPosition().getX();
        double distanceSCY = goal.getCurrentCheckPoint().getPosition().getY() - ship.getPosition().getY();
        double distanceSC = Math.sqrt(Math.pow(distanceSCX,2) + Math.pow(distanceSCY,2));
        double radius=((Circle)goal.getCurrentCheckPoint().getShape()).getRadius();
        String out = "Distance to the checkpoint: "+distanceSC;
        logger.info(out);
        return (distanceSC<=radius && goal.getCheckPoints().size() == 1);
    }
    
    public StringBuilder getAllCheckpointsForOutput() {
    	StringBuilder out = new StringBuilder();
    	List<Checkpoint> checks = goal.getCheckPoints();
    	for(Checkpoint checkpoint : checks) {
    		Position pos = checkpoint.getPosition();
    		double x = pos.getX();
    		double y = pos.getY();
            double radius = ((Circle)checkpoint.getShape()).getRadius();
            out.append(x).append(";").append(y).append(";").append(radius).append("\n");
    	}
    	return out;
    }
}