import java.io.*;
import java.util.*;
import java.util.HashSet;
@SuppressWarnings("unused")
public class SerialLoader {

	protected CaveData cave;

	public Room getStart(){  return cave.getStart(); }

	public Room getEnd(){ return cave.getEnd(); }

	public void deserialize(String fileName){
		try
		{
				// Reading the object from a file
				FileInputStream file = new FileInputStream(fileName);
				ObjectInputStream in = new ObjectInputStream(file);

				// Method for deserialization of object
				cave = (CaveData)in.readObject();

				in.close();
				file.close();

				System.out.println("Object has been deserialized  from file "+fileName);
				System.out.println("Start = "+getStart()+", end = "+getEnd());
		}

		catch(IOException ex)
		{
				System.out.println("IOException is caught => "+ex);
		}

		catch(ClassNotFoundException ex)
		{
				System.out.println("ClassNotFoundException is caught => "+ex);
		}


	}

}