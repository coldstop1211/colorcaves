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
    private int steps;

    public void load(String path) { //load cave data from the files
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

    public void createPath() { //find the shortest path fromthe start room to the end room
        Queue<Room> queue = new LinkedList<>(); //uses a queue to search
        Map<Room, Door> doorUsed = new HashMap<>();
        
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) { //keep track of doors used to enter each room
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
            room = cameFrom.get(room); //once the end room is reached, reconstruct the path from the start to the end using the cameFrom map.
        }
        Collections.reverse(pathList);

        optimizedPath = optimizePath(pathList); //Optimize the path to remove unnecessary backtracking.
        for (Door d : optimizedPath) {
            path.add(d.name());
        }

        //Print the optimized path and the number of explore steps.
        System.out.println("Optimized Path: " + String.join(", ", path));
         steps = Room.getNumMoves();
        System.out.println("Explore Steps: " + steps);
    }

    public void traverse() { //Traverse the cave using the optimized path for 40 back-and-forth trips.
        int totalSteps = 0;
        for (int i = 0; i < 40; i++) { //For 40 iterations, move through the optimized path from start to end and back.
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
        int pathsteps = (optimizedPath.size() * 40);
        System.out.println("Total Score: " + (steps + pathsteps));
    }

    private List<Door> optimizePath(List<Door> path) { //Optimize the path to remove unnecessary backtracking.
        Stack<Door> stack = new Stack<>();
        for (Door d : path) {
            if (!stack.isEmpty() && stack.peek().equals(d)) {
                stack.pop();
            } else {
                stack.push(d);
            }
        }
        return new ArrayList<>(stack);

        /*
         * use a stack to keep track of the doors in the path.
         * if a door leads back to the previous room, remove it from the stack.
         * return the optimized path as a list of doors.
         */
    }

    public static void main(String[] args) {
        SmartBot bot = new SmartBot();
        bot.load("/Users/ashumittal/Downloads/programs/2024/2025/CaveStarter/CaveData/L3.ser");
        bot.createPath();
        bot.traverse();
    }
}
