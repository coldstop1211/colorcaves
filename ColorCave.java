import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("unused")
public class ColorCave extends JPanel implements MouseListener, KeyListener {
    JFrame frame;
    Room currentRoom;
    Room startRoom;
    Room endRoom;
    SerialLoader rl;
    HashMap<Rectangle, Door> doorRectangles;

    int clickCount;

    // Timer variables
    double startTime;
    double endTime;
    double timeTaken;
    boolean done;

    public ColorCave() {

        frame = new JFrame("Color Cave");
        frame.setSize(1500, 800);
        frame.add(this);
        frame.addMouseListener(this);
        frame.addKeyListener(this); // Registering key listener
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        doorRectangles = new HashMap<>();
        startTime = 0;
        endTime = 0;
        clickCount = 0;
        done = false;

        // Load rooms from file
        rl = new SerialLoader();

        //change out load for deserialize when necessary
        rl.deserialize("/Users/ashumittal/Downloads/programs/2024/2025/CaveStarter/CaveData/S1.ser");
        startRoom = rl.getStart();
        endRoom = rl.getEnd();
        currentRoom = startRoom;
        startTime = System.currentTimeMillis();
        repaint();


        startDumbBot();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        double x = (System.currentTimeMillis()-startTime)/1000.0;
        x = Math.round(x * 100.0) / 100.0;

        if (currentRoom == endRoom) {
            // Display end screen
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 44));
            g2.drawString("Congratulations! You reached the end!", 80, 200);
            g2.drawString("Time taken: " + ((endTime - startTime) / 1000) + " seconds", 80, 300);
        } else if (currentRoom == startRoom) {
            // Display start page
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 44));
            g2.drawString("Welcome to Color Cave!", 80, 200);
            g2.drawString("Click on a door to begin.", 80, 300);

            Set<Door> doors = currentRoom.getDoors();
            int startX = 400;
            int startY = 400;
            int doorWidth = 100;
            int doorHeight = 200;

            for (Door d : doors) {
                g2.setColor(enumToColor(d));
                Rectangle r = new Rectangle(startX, startY, doorWidth, doorHeight);
                doorRectangles.put(r, d); // Add to HashMap
                g2.fill(r);

                // Draw doorknob
                g2.setColor(Color.BLACK);
                int knobSize = 10;
                g2.fillOval(startX + doorWidth - knobSize, startY + doorHeight / 2 - knobSize / 2, knobSize, knobSize);

                // Draw window
                g2.setColor(Color.WHITE);
                int windowSize = 20;
                int windowGap = 10;
                int borderThickness = 2;
                int windowStartX = startX + doorWidth / 4;
                int windowStartY = startY + doorHeight / 4;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        g2.fillRect(windowStartX + (windowSize + windowGap) * i, windowStartY + (windowSize + windowGap) * j, windowSize, windowSize);
                        g2.setColor(Color.BLACK);
                        g2.setStroke(new BasicStroke(borderThickness));
                        g2.drawRect(windowStartX + (windowSize + windowGap) * i, windowStartY + (windowSize + windowGap) * j, windowSize, windowSize);
                        g2.setColor(Color.WHITE); // Reset color
                    }
                }

                startX += doorWidth + 20;
            }
        } else {
            // Display room with doors
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 44));
            g2.drawString("Welcome to Room: " + currentRoom.getName(), 80, 200);

            Set<Door> doors = currentRoom.getDoors();
            int startX = 400;
            int startY = 400;
            int doorWidth = 100;
            int doorHeight = 200;

            for (Door d : doors) {
                g2.setColor(enumToColor(d));
                Rectangle r = new Rectangle(startX, startY, doorWidth, doorHeight);
                doorRectangles.put(r, d); // Add to HashMap
                g2.fill(r);

                // Draw doorknob
                g2.setColor(Color.BLACK);
                int knobSize = 10;
                g2.fillOval(startX + doorWidth - knobSize, startY + doorHeight / 2 - knobSize / 2, knobSize, knobSize);

                g2.setColor(Color.WHITE);
                int windowSize = 20;
                int windowGap = 10;
                int borderThickness = 2;
                int windowStartX = startX + doorWidth / 4;
                int windowStartY = startY + doorHeight / 4;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        g2.fillRect(windowStartX + (windowSize + windowGap) * i, windowStartY + (windowSize + windowGap) * j, windowSize, windowSize);
                        g2.setColor(Color.BLACK);
                        g2.setStroke(new BasicStroke(borderThickness));
                        g2.drawRect(windowStartX + (windowSize + windowGap) * i, windowStartY + (windowSize + windowGap) * j, windowSize, windowSize);
                        g2.setColor(Color.WHITE); // Reset color
                    }
                }

                startX += doorWidth + 20;
            }
        }
        if (!done)
          g2.drawString("Time: " + String.format("%.2f", x) + " seconds", 300, 50);
        else
          g2.drawString("Time: " + String.format("%.2f", timeTaken) + " seconds", 300, 50);
        g2.drawString("Doors moved: " +  clickCount, 300, 100);
        repaint();
    }
    public void mouseClicked(MouseEvent e) {
        for (Map.Entry<Rectangle, Door> entry : doorRectangles.entrySet()) {
            Rectangle r = entry.getKey();
            Door door = entry.getValue();
            if (r.contains(e.getX(), e.getY())) {
                clickCount++;
                Room nextRoom = currentRoom.enter(door);
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                if (nextRoom == endRoom) {
                    done = true;
                    // Stop the timer when reaching the end
                    endTime = System.currentTimeMillis();
                    timeTaken = (double)(endTime - startTime) / 1000.0;
                    // Round timeTaken to the nearest hundredth of a second
                    timeTaken = Math.round(timeTaken * 100.0) / 100.0;
                    System.out.println("Time: " + String.format("%.2f", timeTaken) + " seconds");
                }
                currentRoom = nextRoom;
                break;
            }
        }
        repaint();
        startGameLoop();
    }

    private void startGameLoop() {
        Thread gameLoopThread = new Thread(() -> {
            long lastTime = System.currentTimeMillis();
            while (true) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastTime;
                lastTime = currentTime;

                repaint(); // Repaint the screen

                try {
                    Thread.sleep(10); // Adjust the sleep time as per your requirement
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameLoopThread.start();

    }
    // Other mouse listener methods we don't need to use
    public void mouseExited(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }

    private Color enumToColor(Door d) {
        switch (d) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case PINK:
                return Color.PINK;
            case YELLOW:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }

    public static void main(String[] args) {
        ColorCave app = new ColorCave();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle key presses
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
                moveThroughDoor(Door.RED);
                break;
            case KeyEvent.VK_2:
                moveThroughDoor(Door.YELLOW);
                break;
            case KeyEvent.VK_3:
                moveThroughDoor(Door.BLUE);
                break;
            case KeyEvent.VK_4:
                moveThroughDoor(Door.GREEN);
                break;
            case KeyEvent.VK_5:
                moveThroughDoor(Door.PINK);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void moveThroughDoor(Door doorColor) {
        for (Map.Entry<Rectangle, Door> entry : doorRectangles.entrySet()) {
            Door door = entry.getValue();
            if (door == doorColor) {
                clickCount++;
                Room nextRoom = currentRoom.enter(door);
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                if (nextRoom == endRoom) {
                    // Stop the timer when reaching the end
                    endTime = System.currentTimeMillis();
                    timeTaken = (double) (endTime - startTime) / 1000.0;
                    // Round timeTaken to the nearest hundredth of a second
                    timeTaken = Math.round(timeTaken * 100.0) / 100.0;
                    System.out.println("Time: " + String.format("%.2f", timeTaken) + " seconds");
                    done = true;
                }
                currentRoom = nextRoom;
                repaint();
                break;
            }
        }
    }

    private void startDumbBot() {
        DumbBot bot = new DumbBot();
        bot.load("/Users/ashumittal/Downloads/programs/2024/2025/CaveStarter/CaveData/S1.ser");
        bot.createPath();
        
    }

    
}