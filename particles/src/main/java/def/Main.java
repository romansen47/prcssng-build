/**
 * 
 */
package def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import definitions.structures.abstr.fields.Field;
import definitions.structures.abstr.fields.impl.RealLine;
import definitions.structures.abstr.fields.scalars.Scalar;
import definitions.structures.abstr.fields.scalars.impl.Real;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.FiniteVectorMethods;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.vectors.impl.Tuple;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;
import definitions.structures.euclidean.vectorspaces.impl.SpaceGenerator;
import processing.template.Gui;

/**
 * @author ro
 *
 */
public class Main extends Gui {

	final int amount = 15;

//	final double cosmologicalConstant = 1.e2;

//	private double energy;
//
//	double tmpEnergy;

	final Field f = RealLine.getInstance();
	
	double conservationLawExponent = -2;

	double gravitationalConstant = 6.91e-11;

	final double timeUnit = 0.001;

	public static void main(String[] args) {
		(new Gui()).run("def.Main");
	}

	final EuclideanSpace space = (EuclideanSpace) SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(2);
	Vector baseVec1 = space.genericBaseToList().get(0);
	Vector baseVec2 = space.genericBaseToList().get(1);

	final List<Particle> particles = new ArrayList<>();

	@Override
	public void settings() {
		fullScreen();
	}

	final static Random random = new Random();

	final VectorSpace statespace = SpaceGenerator.getInstance().getFiniteDimensionalVectorSpace(100);

	final double rot = 1.e-1;

	@Override
	public void setup() {
		for (int i = 0; i < amount; i++) {
			for (int j = 0; j < amount; j++) {
				Vector vec = space.add(space.stretch(baseVec1, f.get(width * i + random.nextInt(400))),
						space.stretch(baseVec2, f.get(height * j + random.nextInt(300))));
				Scalar[] delta = new Scalar[2];
				delta[0]=(Scalar) f.getZero();
				delta[1]=(Scalar) f.getZero();
				if ((float) (0.001 * width / amount * ((FiniteVectorMethods) vec).getCoordinates().get(baseVec2).getValue()) < 0.5 * width) {
					if ((float) (0.001 * height / amount * ((FiniteVectorMethods) vec).getCoordinates().get(baseVec1).getValue()) < 0.5 * height) {
						delta[0] = f.get(rot * (1.5 + random.nextDouble()));
					} else {
						delta[1] = f.get(rot * (1.5 + random.nextDouble()));
					}
				} else {
					if ((float) (0.001 * height / amount * ((FiniteVectorMethods) vec).getCoordinates().get(baseVec1).getValue()) < 0.5 * height) {
						delta[1] = f.get(-rot * (1.5 + random.nextDouble()));
					} else {
						delta[0] = f.get(-rot * (1.5 + random.nextDouble()));
					}
				}
				Vector vel = new Tuple(delta);
				double mass = 6.e12 * (1.5 + random.nextDouble()%0.5);
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
					space.add(particle.getPosition(), space.stretch(particle.getVelocity(), f.get(timeUnit))));
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
			if (space.getDistance(particle.getPosition(), otherParticle.getPosition()).getValue() < 40) {
				double mass = particle.getMass() + otherParticle.getMass();
				Vector v1 = space.stretch(particle.getVelocity(), new Real(particle.getMass() / mass));
				Vector v2 = space.stretch(otherParticle.getVelocity(),f.get(otherParticle.getMass() / mass));
				Vector vel = space.add(v1, v2);
				((PlainParticle) particle).setMass(mass);
				((PlainParticle) otherParticle).setMass(null);
				particle.setVelocity(vel);
				otherParticle = null;
			} else {
				tmp = space.add(otherParticle.getPosition(), space.stretch(particle.getPosition(), f.get(-1)));
				double norm = space.norm(tmp).getValue();
				Vector newForce = space.add(force, space.stretch(tmp, f.get(gravitationalConstant * otherParticle.getMass()
						* Math.pow(norm, conservationLawExponent))));
				((FiniteVectorMethods) force).setCoordinates(((FiniteVectorMethods) newForce).getCoordinates());
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
