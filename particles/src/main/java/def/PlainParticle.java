package def;

import definitions.structures.abstr.fields.Field;
import definitions.structures.abstr.fields.impl.RealLine;
import definitions.structures.abstr.vectorspaces.InnerProductSpace;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.FiniteVectorMethods;
import definitions.structures.abstr.vectorspaces.vectors.Vector;
import definitions.structures.euclidean.vectorspaces.EuclideanSpace;

public class PlainParticle implements Particle {

	Vector position;
	Vector velocity;
	private Double mass;
	final private VectorSpace space;

	final Vector baseVec1;
	final Vector baseVec2;

	public PlainParticle(VectorSpace space, Vector position, Vector velocity, double mass) {
		this.position = position;
		this.velocity = velocity;
		this.setMass(mass);
		this.space = space;
		baseVec1 = ((EuclideanSpace) space).genericBaseToList().get(0);
		baseVec2 = ((EuclideanSpace) space).genericBaseToList().get(1);
	}

	public Vector getPosition() {
		return position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void draw(Main gui) {
		gui.fill(255);
		gui.noStroke();
		gui.ellipse(gui.width / 4
				+ (float) (0.0003 * gui.width / gui.amount * ((FiniteVectorMethods) position).getCoordinates().get(baseVec2).getValue()),
				gui.height / 4 + (float) (0.0003 * gui.height / gui.amount
						* ((FiniteVectorMethods) position).getCoordinates().get(baseVec1).getValue()),
				(float) (1.e-3 * Math.pow((getMass() / (gui.amount)), 0.3)),
				(float) (1.e-3 * Math.pow((getMass() / (gui.amount)), 0.3)));
	}

	@Override
	public void setPosition(Vector position) {
		this.position = position;
	}

	@Override
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the mass
	 */
	@Override
	public Double getMass() {
		return mass;
	}

	/**
	 * @param mass the mass to set
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}

	final Field f = RealLine.getInstance();

	@Override
	public double getEnergy(Main main) {
		double velEn = Math.pow(((EuclideanSpace) space).norm(getVelocity()).getValue(), 2);
		double potEn = 0;
		Vector diff;
		for (Particle particle : main.particles) {
			if (particle != this) {
				diff = space.add(this.getPosition(), space.stretch(particle.getPosition(), f.get(-1)));
				potEn += -Math.pow(((InnerProductSpace) space).norm(diff).getValue(), -1);
			}
		}
		return getMass() * Math.abs(velEn + potEn);
	}

	@Override
	public VectorSpace getSpace() {
		return space;
	}

	@Override
	public void correctVelocity(double factor, Main main) {
		setVelocity(main.space.stretch(getVelocity(), f.get(factor)));
	}

	@Override
	public void setMass(Double d) {
		mass = d;
	}

}
