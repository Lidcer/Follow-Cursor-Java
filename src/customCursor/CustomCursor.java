package customCursor;

import javax.imageio.ImageIO;
import javax.swing.*;

import eventLoop.EventLoop;
import eventLoop.EventLoopListener;
import transparentFrame.TransparentFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CustomCursor extends JPanel implements EventLoopListener {
	private JFrame frame;
	private boolean visible = false;
	private Point last;
	private BufferedImage image;
	private double rotation = 134;
	private EventLoop eventLoop;
	TransparentFrame panel;
	private Point[] mouseHistory;
	private int imageSize = 70;

	public CustomCursor() throws IOException {
		image = ImageIO.read(this.getClass().getResource("/normal.png"));
		eventLoop = new EventLoop();
		eventLoop.addListener(this);
		mouseHistory = new Point[50];
		last = new Point(0, 0);
		for (int i = 0; i < mouseHistory.length; i++) {
			mouseHistory[i] = last;
		}
		
		frame = new JFrame();
		panel = new TransparentFrame(image, frame);
		frame.setUndecorated(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setAlwaysOnTop(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setBounds(0, 0, imageSize, imageSize);
		frame.setVisible(true);

	}

	public void show() {
		frame.setVisible(true);
		visible = true;
		eventLoop.start();
	}
	public void hide() {
		frame.setVisible(false);
		visible = false;
		eventLoop.stop();
	}

	private void onMouseMove(Point pt) {
		this.pushToMouseHistory(pt);
		double x = pt.x;
		double y = pt.y;

		double deg = this.rotation;

		double avgX = 0;
		double avgY = 0;

		for (Point point : mouseHistory) {
			avgX += point.x;
			avgY += point.y;
		}

		avgX = avgX / this.mouseHistory.length;
		avgY = avgY / this.mouseHistory.length;

		if (avgY > y && avgX < x) {
			double sumY = avgY - y;
			double sumX = x - avgX;
			deg = this.radiansToDegrees(Math.atan(sumX / sumY));
		}
		if (avgY > y && avgX > x) {
			double sumY = avgY - y;
			double sumX = avgX - x;
			deg = this.radiansToDegrees(Math.atan(sumY / sumX)) + 270;
		}
		if (avgY < y && avgX > x) {
			double sumY = y - avgY;
			double sumX = avgX - x;
			deg = this.radiansToDegrees(Math.atan(sumX / sumY)) + 180;
		}
		if (avgY < y && avgX < x) {
			double sumY = y - avgY;
			double sumX = x - avgX;
			deg = this.radiansToDegrees(Math.atan(sumY / sumX)) + 90;
		}
		// deg += 0.01;
		// System.out.print(deg + " ");
		this.rotation = this.correctAngle(deg);
		
		int rounded = (int) Math.round(this.rotation);
		panel.rotateImage(rounded, pt.x, pt.y);
		// if (deg >= 45 && deg < 135) {
		// 	drawingX = width; 
		// 	drawingY = 0; 
		// 	panel.rotateImage(rounded, drawingX, drawingY, pt.x - imageSize - offset, pt.y + size);
		// } else if (deg >= 135 && deg < 225) {
		// 	drawingX = 0; 
		// 	drawingY = height; 
		// 	panel.rotateImage(rounded, drawingX, drawingY, pt.x + size, pt.y - imageSize - offset);
		// } else if (deg >= 225 && deg < 315)  {
		// 	drawingX = -width; 
		// 	drawingY = 0; 
		// 	panel.rotateImage(rounded, drawingX, drawingY, pt.x + offset, pt.y + size);
		// } else {
		// 	drawingX = 0; 
		// 	drawingY = -height; 
		// 	panel.rotateImage(rounded, drawingX, drawingY, pt.x + size, pt.y - offset);
		// }


		// if (deg > 45 && deg < 135) {
		// 	//right
		// 	double fixed = deg - 45;
		// 	double percentage = fixed / 90;
		// 	double multiplyY = this.imageSize * percentage * -1;
	
		// 	frame.setLocation(pt.x - imageSize - offset, pt.y + (int) multiplyY);
		// } else if (deg > 135 && deg < 225) {
		// 	//top
		// 	double fixed = deg - 135;
		// 	double percentage = fixed / 90;
		// 	double multiplyY = this.imageSize * percentage - this.imageSize;
		// 	frame.setLocation(pt.x + (int) multiplyY, pt.y - imageSize - offset);
		// } else if (deg > 225 && deg < 315)  {
		// 	//left
		// 	double fixed = deg - 225;
		// 	double percentage = fixed / 90;
		// 	double multiplyY = this.imageSize * percentage - this.imageSize;
	
		// 	frame.setLocation(pt.x + offset, pt.y + (int) multiplyY);
		// } else {
		// 	//top
		// 	double fixed = (deg + 45) % 360;
		// 	double percentage = fixed / 90;
		// 	double multiplyY = this.imageSize * percentage * -1;
		// 	frame.setLocation(pt.x + (int) multiplyY, pt.y - offset);
		// }
	}
	
	private double correctAngle(double angle) {
		int FULL_ANGLE = 360;
		boolean negative = angle < 0;
		if (negative) angle *= -1;
		angle = getOneSegment(angle, FULL_ANGLE);
		if (negative) angle = FULL_ANGLE - angle;
		return angle;
	  }
	  private double getOneSegment(double value, double maxNumber) {
		double result = Math.floor(value / maxNumber) * maxNumber + maxNumber;
		return maxNumber - (result - value);
	  }


	private void pushToMouseHistory(Point pt) {
		for (int i = mouseHistory.length - 1; i > 0; i--) {
			mouseHistory[i] = mouseHistory[i - 1];
		}
		mouseHistory[0] = pt;
	}

	private double radiansToDegrees(double radians) {
		return radians * (180 / Math.PI);
	}

	@Override
	public void onFrame(long delta) {
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point pt = pi.getLocation(); 
		
		if (last.distance(pt) != 0) {
			last = pt;
			onMouseMove(pt);
		}
	}
}