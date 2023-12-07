/**
 * This class extends Event and represents a collision between a particle and a wall.
 */
public class WallCollision extends Event {

    private Particle _p;
    private boolean _sideWall;

    /**
     * @param timeOfEvent      the time when the collision will take place
     * @param timeEventCreated the time when the event was first instantiated and added to the queue
     */
    public WallCollision(Particle p, boolean sideWall, double timeOfEvent, double timeEventCreated) {
        super(timeOfEvent, timeEventCreated);
        _p = p;
        _sideWall = sideWall;
    }

    /**
     * get the particle involved in the collision of a particle and a wall
     * @return the particle involved in the collision
     */
    public Particle get_p() {
        return this._p;
    }

    /**
     * get the boolean representation of whether the sidewalls are being collided with or the top/bottom walls
     * @return true if it is a collision with the right or left walls and false otherwise
     */
    public boolean get_sideWall() {
        return this._sideWall;
    }
}
