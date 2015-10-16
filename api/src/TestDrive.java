public class TestDrive extends Drive {

	public boolean init() {
		System.out.println("Hello world");
		return true;
	}

	public boolean halt() {
		return true;
	}

	public void update() {
	}

	public static void main(String [] args) {
		RobotComponent test = new TestDrive();
	}
}
