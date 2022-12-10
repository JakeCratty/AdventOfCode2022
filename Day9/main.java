import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.lang.Math;
import java.util.Iterator;

class Main
{
	
	private static class Vector{
		int x, y;
		Vector(int x, int y)
		{ 
			this.x = x; 
			this.y = y; 
		}
	}

	static Vector headPos = new Vector(0, 0);
	static Vector tailPos = new Vector(0, 0);
	static HashMap<Integer, HashMap<Integer, Integer>> visitedSpaces = new HashMap<Integer, HashMap<Integer, Integer>>();

	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}

		addSpace(tailPos);
		for(String s : stringList){
			String[] input = s.split(" ");
			Character headDirection = input[0].charAt(0);
			int len = Integer.valueOf(input[1]);

			for(int i = 0; i < len; i++){
				if(headDirection.equals('R') || headDirection.equals('L'))
					moveHeadHorizontal(headDirection);
				else
					moveHeadVertical(headDirection);
			}
		}

		Iterator rows = visitedSpaces.keySet().iterator();
		int[] count = {0};
		rows.forEachRemaining(xPos -> {
			HashMap<Integer, Integer> map = visitedSpaces.get(xPos);
			Iterator cols = map.keySet().iterator();
			cols.forEachRemaining(yPos -> {
				count[0]++;
				System.out.println(xPos + ", " + yPos);
			});
		});

		System.out.println("Spaces: " + count[0]);
	}

	public static void moveHeadHorizontal(Character headDirection){
		int dir = headDirection.equals('R') ? 1 : -1;
		headPos.x += dir;
		if(tailPos.y == headPos.y){ //if in the same row
			if(tailPos.x == headPos.x || Math.abs(headPos.x - tailPos.x) == 1){ //if in same column, don't move tail
				//do nothing
			}
			else{
				tailPos.x += dir;
				addSpace(tailPos);
			}
		}
		else{
			if(Math.abs(headPos.x - tailPos.x) == 2){
				tailPos.y = headPos.y;
				tailPos.x += dir;
				addSpace(tailPos);
			}
		}
	}

	public static void moveHeadVertical(Character headDirection){
		int dir = headDirection.equals('U') ? 1 : -1;
		headPos.y += dir;
		if(tailPos.x == headPos.x){ //if in the same col
			if(tailPos.y == headPos.y || Math.abs(headPos.y - tailPos.y) == 1){ //if in same column, don't move tail
				//do nothing
			}
			else{
				tailPos.y += dir;
				addSpace(tailPos);
			}
		}
		else{
			if(Math.abs(headPos.y - tailPos.y) == 2){
				tailPos.x = headPos.x;
				tailPos.y += dir;
				addSpace(tailPos);
			}
		}
		//System.out.println("Head at space " + headPos.x + ", " + headPos.y);
	}

	public static void addSpace(Vector tailPos)
	{
		//System.out.println("Tail at space " + tailPos.x + ", " + tailPos.y);
		if(visitedSpaces.containsKey(tailPos.x)){
			visitedSpaces.get(tailPos.x).put(tailPos.y, 0);
		}
		else{
			visitedSpaces.put(tailPos.x, new HashMap<Integer, Integer>());
			visitedSpaces.get(tailPos.x).put(tailPos.y, 0);
		}
	}
}