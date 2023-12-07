/**
 * This event extends Event and represents a collision between two particles.
 */
public class ParticleCollision extends Event {
    private Particle _p1;
    private Particle _p2;


    /**
     * @param timeOfEvent      the time when the collision will take place
     * @param timeEventCreated the time when the event was first instantiated and added to the queue
     */
    public ParticleCollision(Particle p1, Particle p2, double timeOfEvent, double timeEventCreated) {
        super(timeOfEvent, timeEventCreated);
        _p1 = p1;
        _p2 = p2;
    }

    /**
     * get the first particle involved in the collision of two particles
     * @return the first particle involved in the collision
     */
    public Particle get_p1() {
        return this._p1;
    }

    /**
     * get the second particle involved in the collision of two particles
     * @return the other particle involved in the collision
     */
    public Particle get_p2() {
        return this._p2;
    }
}
