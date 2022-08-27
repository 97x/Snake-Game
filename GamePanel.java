import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; // Object size
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE; // Total amount of units on screen
	static final int DELAY = 75; // Higher number means faster game
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6; // Amount of snake parts to begin with
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	// Constructor
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		if(running) {
			/* Turn game panel into matrix to visualize unit sizes
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			*/

			// Draw apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			// Draw head
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
//					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			//  Display score message
			g.setColor(Color.white);
			g.setFont( new Font("Arial", Font.BOLD, 18));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten,
					(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}

	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1]; // Shift x-coords in array over by one spot
			y[i] = y[i-1]; // Shift y-coords in array over by one spot
		}

		// Change direction of movement
		switch (direction) {
			case 'U' -> y[0] = y[0] - UNIT_SIZE;
			case 'D' -> y[0] = y[0] + UNIT_SIZE;
			case 'R' -> x[0] = x[0] + UNIT_SIZE;
			case 'L' -> x[0] = x[0] - UNIT_SIZE;
		}

	}

	public void checkApple() {
		// If head == x pos of apple and head == y pos of apple
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}

	}

	public void checkCollisions() {
		// Check if head collides with body
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// Check if head touches left wall
		if(x[0] < 0) {
			running = false;
		}
		// Check if head touches right wall
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// Check if head touches top wall
		if(y[0] < 0) {
			running = false;
		}
		// Check if head touches bottom wall
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		//  Display score message
		g.setColor(Color.white);
		g.setFont( new Font("Arial", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten,
				(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

		//  Game over message
		g.setColor(Color.red);
		g.setFont( new Font("Arial", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				// Left
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if(direction != 'R') {
						direction = 'L';
					} break;
					// Right
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if(direction != 'L') {
						direction = 'R';
					} break;
					// Up
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(direction != 'D') {
						direction = 'U';
					} break;
					// Down
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if(direction != 'U') {
						direction = 'D';
					} break;
			}
		}
	}
}
