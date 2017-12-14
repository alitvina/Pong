package de.berlin.htw;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Pong {
	private static int height = 640, width = 800;
	float p1x = 110, p1y = 320;
	float p2x = 690, p2y = 320;
	float ballx = 400, bally = 320;
	Vector2f dir = new Vector2f(1, 1);
	
	long lastFrame;
	int fps;
	long lastFPS;
	
	public void run() {
		setup();
		getDelta();
		lastFPS = getTime();
		
		while(!end()) {
			int delta = getDelta();
			update(delta);
			draw();
			
			Display.update();
			Display.sync(60);
		}
		
		finish();
	}
	
	public void setup() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		}
		catch(LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glColor3f(0.6f, 0.8f, 0.3f);
				
	}
	
	public boolean end() {
		boolean end = Display.isCloseRequested();
		end |= Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
		return end;
	}
	
	public void update(int delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			p1y -= 0.35f * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			p1y += 0.35f * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			p2y += 0.35f * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			p2y -= 0.35f * delta;
		}
		
		if (p1y < 30) { p1y = 30; }
		if (p1y > height - 30) { p1y = height - 30; }
		if (p2y < 30) { p2y = 30; }
		if (p2y > height - 30) { p2y = height - 30; }
		
//		moveBall();
		if (bally <= 5 || bally >= (height - 6)) { dir.y *= -1; }
		if (((int)ballx == ((int)p1x + 10) && bally >= (p1y - 30) && bally <= (p1y + 30)) || ((int)ballx == ((int)p2x - 10) && bally >= (p2y - 30) && bally <= (p2y + 30))) {
			dir.x *= -1; 
			}
		if (ballx <= 5 || ballx >= (width - 6)) {
			ballx = 400;
			bally = 320;
		}
		
		
		ballx += dir.x * 0.11f * delta;
		bally += dir.y * 0.11f * delta;
		
		updateFPS();
		
	}
	
	/**
	 * Calculate how many millisecond since last frame.
	 * @return milliseconds since last frame
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	/**
	 * Get the accurate System time
	 * @return the system time in milliseconds
	 */
	public long getTime() {
		// getTime returns time in ticks
		// getTimerResolution returns ticks pro second
		return (Sys.getTime() * 1000) / Sys.getTimerResolution(); 
	}
	
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	
	public void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor3f(0.5f, 0.8f, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(p1x - 10, p1y - 30);
		GL11.glVertex2f(p1x + 10, p1y - 30);
		GL11.glVertex2f(p1x + 10, p1y + 30);
		GL11.glVertex2f(p1x - 10, p1y + 30);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(p2x - 10, p2y - 30);
		GL11.glVertex2f(p2x + 10, p2y - 30);
		GL11.glVertex2f(p2x + 10, p2y + 30);
		GL11.glVertex2f(p2x - 10, p2y + 30);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(ballx - 5, bally - 5);
		GL11.glVertex2f(ballx + 5, bally - 5);
		GL11.glVertex2f(ballx + 5, bally + 5);
		GL11.glVertex2f(ballx - 5, bally + 5);
		GL11.glEnd();
	}

	public void finish() {
		Display.destroy();
	}
	
	public static void main (String[] args) {
		Pong pong = new Pong();
		pong.run();
	}
}
