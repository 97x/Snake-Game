import javax.swing.*;

public class GameFrame extends JFrame {

	// Constructor
	GameFrame() {
		this.add(new GamePanel());
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); // Takes JFrame and fits components around the frame
		this.setVisible(true);
		this.setLocationRelativeTo(null); // Opens window in middle of screen
	}
}
