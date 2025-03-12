import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class SmartBot {

    private SerialLoader sl;
    private Room curr;
    private Room start;
    private Room end;
    private Set<Room> visited;
    private Map<Room, List<Door>> unexploredDoors;
    private Map<Room, Room> cameFrom;
    private List<Door> optimizedPath;
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
        optimizedPath = new ArrayList<>();
        this.path = new ArrayList<>();
    }

    public void createPath() {
        Queue<Room> queue = new LinkedList<>();
        Map<Room, Door> doorUsed = new HashMap<>();
        
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Room room = queue.poll();
            if (room.equals(end)) break;

            for (Door d : room.getDoors()) {
                Room nextRoom = room.enter(d);
                if (!visited.contains(nextRoom)) {
                    visited.add(nextRoom);
                    cameFrom.put(nextRoom, room);
                    doorUsed.put(nextRoom, d);
                    queue.offer(nextRoom);
                }
            }
        }

        List<Door> pathList = new ArrayList<>();
        Room room = end;
        while (!room.equals(start)) {
            Door door = doorUsed.get(room);
            pathList.add(door);
            room = cameFrom.get(room);
        }
        Collections.reverse(pathList);

        optimizedPath = optimizePath(pathList);
        for (Door d : optimizedPath) {
            path.add(d.name());
        }

        System.out.println("Optimized Path: " + String.join(", ", path));
        System.out.println("Explore Steps: " + Room.getNumMoves());
    }

    public void traverse() {
        int totalSteps = 0;
        for (int i = 0; i < 40; i++) {
            for (Door d : optimizedPath) {
                curr = curr.enter(d);
                totalSteps++;
            }
            for (int j = optimizedPath.size() - 1; j >= 0; j--) {
                curr = curr.enter(optimizedPath.get(j));
                totalSteps++;
            }
        }
        System.out.println("Total Path Steps: " + optimizedPath.size());
        System.out.println("Total Score: " + (Room.getNumMoves() + optimizedPath.size() * 40));
    }

    private List<Door> optimizePath(List<Door> path) {
        Stack<Door> stack = new Stack<>();
        for (Door d : path) {
            if (!stack.isEmpty() && stack.peek().equals(d)) {
                stack.pop();
            } else {
                stack.push(d);
            }
        }
        return new ArrayList<>(stack);
    }

    public static void main(String[] args) {
        SmartBot bot = new SmartBot();
        bot.load("/Users/ashumittal/Downloads/programs/2024/2025/CaveStarter/CaveData/L1.ser");
        bot.createPath();
        bot.traverse();
    }
}
