/**
 * Represents a collision between a particle and another particle, or a particle and a wall.
 */
public class Event implements Comparable<Event> {
	private double _timeOfEvent;
	private double _timeEventCreated;

	/**
	 * @param timeOfEvent the time when the collision will take place
	 * @param timeEventCreated the time when the event was first instantiated and added to the queue
	 */
	public Event (double timeOfEvent, double timeEventCreated) {
		_timeOfEvent = timeOfEvent;
		_timeEventCreated = timeEventCreated;
	}

	@Override
	/**
	 * Compares two Events based on their event times. Since you are implementing a maximum heap,
	 * this method assumes that the event with the smaller event time should receive higher priority.
	 */
	public int compareTo (Event e) {
		if (_timeOfEvent < e._timeOfEvent) {
			return +1;
		} else if (_timeOfEvent == e._timeOfEvent) {
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * get the time that the event is set to occur in order to compare to time particle was last updated
	 * @return the time the event occurs
	 */
	public double get_timeOfEvent() {
		return this._timeOfEvent;
	}

	/**
	 * store the time the event was created
	 * @return the time the event was created
	 */
	public double get_timeEventCreated() {
		return this._timeEventCreated;
	}
}
