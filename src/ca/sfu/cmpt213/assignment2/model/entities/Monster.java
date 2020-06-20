package ca.sfu.cmpt213.assignment2.model.entities;

import ca.sfu.cmpt213.assignment2.Handler;
import ca.sfu.cmpt213.assignment2.model.Coordinates;
import ca.sfu.cmpt213.assignment2.model.Direction;
import ca.sfu.cmpt213.assignment2.model.Terrain;
import ca.sfu.cmpt213.assignment2.model.Tile;

import java.util.ArrayList;
import java.util.Random;

/**
 * Monster class which inherits its fields from Entity.
 * Manages monster objects movement patterns
 */
public class Monster extends Entity {

    public static final String SYMBOL = "!";
    public static final int PRIORITY = 3;
    private Coordinates previousLocation;

    public Monster(int x, int y, int id) {
        super(x,y,id,SYMBOL,PRIORITY);
        previousLocation = new Coordinates(0,0);
    }

    public void setPreviousLocation(Coordinates location) { previousLocation = location; }

    public Direction getAIDirection (Tile[][] map) {
        ArrayList<Direction> validDirections = new ArrayList<>();
        for (Direction direction : Direction.cardinals) {
            Coordinates targetCoordinates = Handler.locateDirection(this.getPosition(), direction);
            Tile targetTile = map[targetCoordinates.getY()][targetCoordinates.getX()];
            if (targetTile.getTerrain().equals(Terrain.EMPTY)) validDirections.add(direction);
        }

        // Seek random direction if backtrack is not the only viable option
        if (validDirections.size() > 1) {
            while (true) {
                Direction direction = validDirections.get(new Random().nextInt(validDirections.size()));
                Coordinates targetCoordinates = Handler.locateDirection(this.getPosition(), direction);
                Tile targetTile = map[targetCoordinates.getY()][targetCoordinates.getX()];

                boolean isBackTrack = false;
                if (
                        targetTile.getPosition().getX() == previousLocation.getX()
                        && targetTile.getPosition().getY() == previousLocation.getY()
                ) isBackTrack = true;

                if (targetTile.getTerrain().equals(Terrain.EMPTY) && !isBackTrack) return direction;
            }
        }
        else return validDirections.get(0);
    }
}
