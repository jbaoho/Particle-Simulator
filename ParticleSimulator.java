import java.util.*;
import java.util.function.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;

public class ParticleSimulator extends JPanel {
	private Heap<Event> _events;
	private java.util.List<Particle> _particles;
	private double _duration;
	private int _width;

	/**
	 * @param filename the name of the file to parse containing the particles
	 */
	public ParticleSimulator (String filename) throws IOException {
		_events = new HeapImpl<>();

		// Parse the specified file and load all the particles.
		Scanner s = new Scanner(new File(filename));
		_width = s.nextInt();
		_duration = s.nextDouble();
		s.nextLine();
		_particles = new ArrayList<>();
		while (s.hasNext()) {
			String line = s.nextLine();
			Particle particle = Particle.build(line);
			_particles.add(particle);
		}

		setPreferredSize(new Dimension(_width, _width));
	}

	@Override
	/**
	 * Draws all the particles on the screen at their current locations
	 * DO NOT MODIFY THIS METHOD
	 */
        public void paintComponent (Graphics g) {
		g.clearRect(0, 0, _width, _width);
		for (Particle p : _particles) {
			p.draw(g);
		}
	}

	// Helper class to signify the final event of the simulation.
	private class TerminationEvent extends Event {
		TerminationEvent (double timeOfEvent) {
			super(timeOfEvent, 0);
		}
	}

	/**
	 * Helper method to update the positions of all the particles based on their current velocities.
	 */
	private void updateAllParticles (double delta) {
		for (Particle p : _particles) {
			p.update(delta);
		}
	}

	/**
	 * Executes the actual simulation.
	 */
	private void simulate (boolean show) {
		double lastTime = 0;

		// Create initial events, i.e., all the possible
		// collisions between all the particles and each other,
		// and all the particles and the walls.
		for (Particle p : _particles) {
			// Add new events.
			this.addEvents(p, lastTime);
		}
		
		_events.add(new TerminationEvent(_duration));
		while (_events.size() > 0) {
			Event event = _events.removeFirst();
			double delta = event.get_timeOfEvent() - lastTime;
			if (event instanceof TerminationEvent) {
				updateAllParticles(delta);
				break;
			}

//			 Check if event still valid; if not, then skip this event
//			 if (event not valid) {
//			   continue;
//			 }
			boolean particleCollision;
			Particle p;
			Particle q;
			if (event instanceof ParticleCollision) {
				particleCollision = true;
				p = ((ParticleCollision) event).get_p1();
				q = ((ParticleCollision) event).get_p2();
			} else {
				particleCollision = false;
				p =  ((WallCollision) event).get_p();
				q = null;
			}

			if (particleCollision) {
				if (p.get_lastUpdateTime() > event.get_timeEventCreated()
					|| q.get_lastUpdateTime() > event.get_timeEventCreated()) {
					continue;
				}
			} else {
				if (p.get_lastUpdateTime() > event.get_timeEventCreated()) {
					continue;
				}
			}

			// Since the event is valid, then pause the simulation for the right
			// amount of time, and then update the screen.
			if (show) {
				try {
					Thread.sleep((long) delta);
				} catch (InterruptedException ie) {}
			}

			// Update positions of all particles
			updateAllParticles(delta);

			// Update the velocity of the particle(s) involved in the collision
			// (either for a particle-wall collision or a particle-particle collision).
			// You should call the Particle.updateAfterCollision method at some point.
			if (particleCollision) {
				p.updateAfterCollision(event.get_timeOfEvent(), q);

				// Enqueue new events for the particles that were involved in this particle-collision event.
				this.addEvents(p, event.get_timeOfEvent());
				this.addEvents(q, event.get_timeOfEvent());
			} else {
				p.updateAfterWallCollision(event.get_timeOfEvent(), ((WallCollision) event).get_sideWall());

				// Enqueue new events for the particle that were involved in this wall-collision event.
				this.addEvents(p, event.get_timeOfEvent());
			}

			// Update the time of our simulation
			lastTime = event.get_timeOfEvent();

			// Redraw the screen
			if (show) {
				repaint();
			}
		}

		// Print out the final state of the simulation
		System.out.println(_width);
		System.out.println(_duration);
		for (Particle p : _particles) {
			System.out.println(p);
		}
	}

	private void addEvents(Particle p, double lastUpdate) {
		// add particle-particle collisions
		for (Particle q : this._particles) {
			if (!p.equals(q) || Double.isInfinite(p.getCollisionTime(q))){
				this._events.add(new ParticleCollision(p, q, p.getCollisionTime(q) + lastUpdate, lastUpdate));
			}
		}

		// add particle-wall collisions
		double sideWallCollisionTime = p.getWallCollisionTime(true, _width);
		double topBottomWallCollisionTime = p.getWallCollisionTime(false, _width);

		if (!Double.isInfinite(sideWallCollisionTime)) {
			this._events.add(new WallCollision(p, true, sideWallCollisionTime + lastUpdate, lastUpdate));
		}
		if (!Double.isInfinite(topBottomWallCollisionTime)) {
			this._events.add(new WallCollision(p, false, topBottomWallCollisionTime + lastUpdate, lastUpdate));
		}
	}

	public static void main (String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: java ParticalSimulator <filename>");
			System.exit(1);
		}

		ParticleSimulator simulator;

		simulator = new ParticleSimulator(args[0]);
		JFrame frame = new JFrame();
		frame.setTitle("Particle Simulator");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(simulator, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulator.simulate(true);
	}
}
