public interface RobotComponent {

	// This method should create all the resources needed for the component
	public boolean init();

	// This method should free all resources used, like Digital IO, Servo's, etc.
	public boolean halt();

	// This method should be called repeatedly over the course of the components lifetime.
	// Any manipulation of the robot should happen here.
	public void update();
}
