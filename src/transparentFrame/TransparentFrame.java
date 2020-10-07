package transparentFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TransparentFrame extends JPanel {

    private BufferedImage originalImage;
    private int parentX = 0;
    private int parentY = 0;
    private double degrees;
    private JFrame parent;

    public TransparentFrame(BufferedImage image, JFrame parent) {
        super();
        this.originalImage = image;
        this.parent = parent;
        setOpaque(false);
    }

    public int getImageWidth() {
        return originalImage.getWidth();
    }

    public int getImageHeight() {
        return originalImage.getHeight();
    }

    public void rotateImage(double degrees, int parentX, int parentY) {
        this.degrees = degrees;
        this.parentX = parentX;
        this.parentY = parentY;
        this.updateUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();


        double radians = Math.toRadians(degrees);
        AffineTransform at = new AffineTransform();
        int halfWidth = (int) (originalImage.getWidth() * 0.5);
        int halfHeight = (int) (originalImage.getHeight() * 0.5);
        at.setToRotation(radians, halfWidth, halfHeight);
        g2d.setTransform(at);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();
        parent.setLocation(parentX - halfWidth, parentY - halfHeight);
    } 
}
