package def;

import definitions.structures.abstr.InnerProductSpace;
import definitions.structures.abstr.Vector;
import definitions.structures.abstr.VectorSpace;
import definitions.structures.finitedimensional.vectorspaces.EuclideanSpace;

public class PlainParticle implements Particle {

	Vector position;
	Vector velocity;
	private Double mass;
	final private VectorSpace space;
	
	public PlainParticle(VectorSpace space,Vector position,Vector velocity,double mass) {
		this.position=position;
		this.velocity=velocity;
		this.setMass(mass);
		this.space=space;
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
		gui.ellipse(
				gui.width/4+(float)(0.0003*gui.width/gui.amount*position.getGenericCoordinates()[1]), 
				gui.height/4+(float)(0.0003*gui.height/gui.amount*position.getGenericCoordinates()[0]),
				(float) Math.pow((getMass()/(gui.amount)),0.3), 
				(float) Math.pow((getMass()/(gui.amount)),0.3));
	}

	@Override
	public void setPosition(Vector position) {
		this.position=position;
	}

	@Override
	public void setVelocity(Vector velocity) {
		this.velocity=velocity;
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

	@Override
	public double getEnergy(Main main) {
		double velEn = Math.pow(((EuclideanSpace) space).norm(getVelocity()),2);
		double potEn =0;
		Vector diff;
		for (Particle particle:main.particles) {
			if (particle!=this) {
				diff=space.add(this.getPosition(),
						space.stretch(particle.getPosition(),-1));
				potEn+=-Math.pow(((InnerProductSpace) space).norm(diff),-1);
			}
		}
		return getMass()*Math.abs(velEn+potEn);
	}
	
	@Override
	public VectorSpace getSpace() {
		return space;
	}

	@Override
	public void correctVelocity(double factor, Main main) {
		setVelocity(main.space.stretch(getVelocity(),factor));
	}

	@Override
	public void setMass(Double d) {
		mass=d;
	}


}
