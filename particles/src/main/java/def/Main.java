/**
 * 
 */
package def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import definitions.structures.abstr.Vector;
import definitions.structures.abstr.VectorSpace;
import definitions.structures.finitedimensional.vectors.impl.Tuple;
import definitions.structures.finitedimensional.vectorspaces.EuclideanSpace;
import definitions.structures.finitedimensional.vectorspaces.impl.SpaceGenerator;
import processing.template.Gui;

/**
 * @author ro
 *
 */
public class Main extends Gui {

	final int amount = 25;

//	final double cosmologicalConstant = 1.e2;

//	private double energy;
//
//	double tmpEnergy;

	double conservationLawExponent = -2;

	double gravitationalConstant = 1.e14;

	final double timeUnit = 1.e-3;

	public static void main(String[] args) {
		(new Gui()).run("def.Main");
	}

	final EuclideanSpace space = SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(2);
	Vector baseVec1 = space.genericBaseToList().get(0);
	Vector baseVec2 = space.genericBaseToList().get(1);

	final List<Particle> particles = new ArrayList<>();

	@Override
	public void settings() {
		fullScreen();
	}

	final static Random random = new Random();

	final VectorSpace statespace = SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(100);

	final double rot = 1.e2;

	@Override
	public void setup() {
		for (int i = 0; i < amount; i++) {
			for (int j = 0; j < amount; j++) {
				Vector vec = space.add(space.stretch(baseVec1, width * i + random.nextInt(400)),
						space.stretch(baseVec2, height * j + random.nextInt(300)));
				double[] delta = new double[2];
				if ((float) (0.001 * width / amount * vec.getGenericCoordinates()[1]) < 0.5 * width) {
					if ((float) (0.001 * height / amount * vec.getGenericCoordinates()[0]) < 0.5 * height) {
						delta[0] = rot * (1.5 + random.nextDouble());
					} else {
						delta[1] = rot * (1.5 + random.nextDouble());
					}
				} else {
					if ((float) (0.001 * height / amount * vec.getGenericCoordinates()[0]) < 0.5 * height) {
						delta[1] = -rot * (1.5 + random.nextDouble());
					} else {
						delta[0] = -rot * (1.5 + random.nextDouble());
					}
				}
				Vector vel = new Tuple(delta);
				double mass = 300 * (1.5 + random.nextDouble());
				particles.add(new PlainParticle(space, vec, vel, mass));
			}
		}
	}

	@Override
	public void draw() {
		background(0);
		moveParticles();
		computeNewImpulses();
		drawParticles();
	}

	private void moveParticles() {
		for (Particle particle : particles) {
			if (particle.getMass() != null) {
				particle.setPosition(
						space.add(particle.getPosition(), space.stretch(particle.getVelocity(), timeUnit)));
			}
		}
	}

	private void computeNewImpulses() {
		Map<Particle, Vector> forces = new HashMap<>();
		Vector tmp=null;
		for (Particle particle : particles) {
			if (particle.getMass() != null) {
				Vector force = space.nullVec();
				for (Particle otherParticle : particles) {
					computeForceOnParticle(particle,otherParticle,tmp,force);
					
				}
				forces.put(particle, force);
			}
		}
		List<Particle> throwAwayParticles=new ArrayList<>();
		for (Particle particle : particles) {
			if (particle.getMass() != null) {
				particle.setVelocity(space.add(particle.getVelocity(), forces.get(particle)));
			}
			else {
				throwAwayParticles.add(particle);
			}
		}
		for (Particle particle:throwAwayParticles) {
			particles.remove(particle);
		}
	}

	private void computeForceOnParticle(Particle particle, Particle otherParticle, Vector tmp, Vector force) {
		if (particle != otherParticle && otherParticle.getMass() != null) {
			if (space.getDistance(particle.getPosition(), otherParticle.getPosition()) < 50) {
				double mass = particle.getMass() + otherParticle.getMass();
				Vector v1 = space.stretch(particle.getVelocity(), particle.getMass() / mass);
				Vector v2 = space.stretch(otherParticle.getVelocity(), otherParticle.getMass() / mass);
				Vector vel = space.add(v1, v2);
				((PlainParticle) particle).setMass(mass);
				((PlainParticle) otherParticle).setMass(null);
				particle.setVelocity(vel);
				otherParticle = null;
			} else {
				tmp = space.add(otherParticle.getPosition(), space.stretch(particle.getPosition(), -1));
				double norm = space.norm(tmp);
				force = space.add(force, space.stretch(tmp, gravitationalConstant * otherParticle.getMass()
						/ particle.getMass() * Math.pow(norm, conservationLawExponent)));
			}
		}
	}

	private void drawParticles() {
		for (Particle particle : particles) {
			if (particle.getMass() != null) {
				particle.draw(this);
			}
		}
	}

}
