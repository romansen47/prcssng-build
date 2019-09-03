package def;

import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Vector;

public interface Particle {

	Vector getPosition();

	void setPosition(Vector position);
	
	Vector getVelocity();

	void setVelocity(Vector velocity);
	
	void draw(Main gui);

	void setMass(Double d);
	
	Double getMass();

	double getEnergy(Main main);

	VectorSpace getSpace();

	void correctVelocity(double factor, Main main);
	
}
