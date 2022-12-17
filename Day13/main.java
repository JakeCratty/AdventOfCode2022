import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.lang.Math;
import java.util.Iterator;
import java.lang.Thread;
import java.util.ArrayDeque;
import java.math.BigInteger;
import java.util.PriorityQueue;

class Main
{
	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}
	}
}

