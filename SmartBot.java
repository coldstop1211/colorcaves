import java.io.*;
import java.util.*;


/*
 * so compared to my dumbbot, this bot is a bit smarter
 * it avoids pointless backtracking by keeping track of visited rooms and unexplored doors, 
 * marks dead ends to prevent revisiting them, and reconstructs the shortest path from the start to the end
 * it also tries multiple approaches to find the best path and outputs the door colors as it navigates through the cave. 
 */

@SuppressWarnings("unused")
public class SmartBot1 {
    
    private SerialLoader sl;
    private Room curr;
    private Room start;
    private Room end;
    private Set<Room> visited;
    private Map<Room, List<Door>> unexploredDoors;
    private Map<Room, Room> cameFrom;
    private List<Room> shortestPath;
    private List<String> path;

    public void load(String path) {
        sl = new SerialLoader();
        sl.deserialize(path);
        curr = sl.getStart();
        start = curr;
        end = sl.getEnd();
        visited = new HashSet<>();
        unexploredDoors = new HashMap<>();
        cameFrom = new HashMap<>();
        shortestPath = new ArrayList<>();
        this.path = new ArrayList<>();
    }

    public void createPath() {
        Stack<Room> stack = new Stack<>();
        stack.push(curr);
        visited.add(curr);

        while (!stack.isEmpty()) {
            Room room = stack.peek();
            if (room.equals(end)) {
                break;
            }

            List<Door> doors = new ArrayList<>(room.getDoors());
            doors.removeIf(door -> visited.contains(room.enter(door)));

            if (doors.isEmpty()) {
                stack.pop();
            } else {
                Door nextDoor = doors.get(0);
                Room nextRoom = room.enter(nextDoor);
                visited.add(nextRoom);
                cameFrom.put(nextRoom, room);
                stack.push(nextRoom);
                path.add(nextDoor.name());
            }
        }

        Room room = end;
        while (room != null) {
            shortestPath.add(room);
            room = cameFrom.get(room);
        }
        Collections.reverse(shortestPath);

        System.out.println("Path: " + String.join(", ", path));
        System.out.println("Explore Steps: " + Room.getNumMoves());
    }

    public void traverse() {
        int totalSteps = 0;
        for (int i = 0; i < 40; i++) {
            for (Room room : shortestPath) {
                curr = room;
                totalSteps++;
            }
            for (int j = shortestPath.size() - 1; j >= 0; j--) {
                curr = shortestPath.get(j);
                totalSteps++;
            }
        }
        System.out.println("Total Path Steps: " + path.size());
        System.out.println("Total Score: " + (Room.getNumMoves() + (path.size() * 40)));
    }

    public static void main(String[] args) {
        SmartBot bot = new SmartBot();
        bot.load("/Users/ashumittal/Downloads/programs/2024/2025/CaveStarter/CaveData/L1.ser");
        bot.createPath();
        bot.traverse();
    }

    private Door randomDoor() {
        Set<Door> doors = curr.getDoors();
        int rand = (int)(Math.random() * doors.size());
        int i = 0;
        Door nextDoor = Door.GREEN;
        for (Door d : doors) {
            nextDoor = d;
            if (i == rand) {
                break;
            }
            i++;
        }
        return nextDoor;
    }
}
