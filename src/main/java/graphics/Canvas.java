package graphics;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import character.Player;
import geometry.Vector2D;


public class Canvas extends JPanel {
    private boolean isFullscreen;
    private Timer timer;

    // TESTING PURPOSE
    private Player player;
    private KeyStack stack;
    private boolean wasReleasedSpace;
    // ---------------

    public Canvas() {
        this(false);
    }

    public Canvas(boolean isFullscreen) {
        super(true);
        this.isFullscreen = isFullscreen;

        setBackground(new Color(42, 42, 42, 255));

        // TESTING PURPOSE
        this.player = new Player();
        this.stack = new KeyStack(this);
        this.wasReleasedSpace = true;
        stack.listenTo("Z");
        stack.listenTo("S");
        stack.listenTo("Q");
        stack.listenTo("D");
        stack.listenTo("SPACE");
        // ---------------

        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // TESTING PURPOSE
                Vector2D movement = new Vector2D();
                if (stack.isPressed("Z")) {
                    movement.y -= 4;
                }
                if (stack.isPressed("S")) {
                    movement.y += 4;
                }
                if (stack.isPressed("Q")) {
                    movement.x -= 4;
                }
                if (stack.isPressed("D")) {
                    movement.x += 4;
                }
                if (stack.isPressed("SPACE")) {
                    if (wasReleasedSpace) {
                        player.attack();
                        wasReleasedSpace = false;
                    }
                } else {
                    wasReleasedSpace = true;
                }
                player.move(movement);
                // ---------------

                repaint();
            }
        });
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        if (isFullscreen) {
            return Toolkit.getDefaultToolkit().getScreenSize();
        } else {
            return new Dimension(600, 600);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // TESTING PURPOSE
        g.setColor(new Color(56, 56, 56));
        for (int i = -this.getWidth() / 2 ; i < 3 * this.getWidth() / 2 ; i += 10) {
            for (int j = -this.getHeight() / 2 ; j < 3 * this.getHeight() / 2 ; j += 10) {
                if (((i + j) / 10) % 2 == 0) {
                    g.fillRect(i - (int)this.player.getPosition().x, j - (int)this.player.getPosition().y, 10, 10);
                }
            }
        }    

        g.setColor(new Color(0, 255, 0));
        g.fillRect(350 - (int)this.player.getPosition().x, 350 - (int)this.player.getPosition().y, 50, 50);
        g.setColor(new Color(100, 100, 60));
        g.fillRect(370 - (int)this.player.getPosition().x, 400 - (int)this.player.getPosition().y, 10, 30);

        final int SCALE = 2;

        int[] dimensions = this.player.getSpriteSize();
        Vector2D positions = Vector2D.add(new Vector2D(220, 220), Vector2D.scale(this.player.getOffset(), SCALE));
        g.drawImage(this.player.getSprite(), (int)positions.x, (int)positions.y, dimensions[0] * SCALE, dimensions[1] * SCALE, this);
        // ---------------
    }
}
