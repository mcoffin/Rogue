// Defines a few methods that a drive class should implement
public interface Drive {

	// Both these methods would take one or more joystick axis
	public void move(double sX, double sY);
	public void turn(double sT);

	//Encoder Methods
	public double getEncoderDistance();
	public void resetEncoders();
	
	//Autonomous timed movement
	public void timedMove(double sxT, double syT);
	public void timedTurn(double sTT);
	
}
