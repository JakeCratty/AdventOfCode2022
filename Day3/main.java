import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;

class Main
{
	public static void main(String args[])
	{
		ArrayList<String> ruckSacks = null;
		try{
			ruckSacks = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}

		///part A solution
		int partAScore = 0;
		for(String s : ruckSacks){
			int sLen = s.length();
			HashMap<Character, Integer> componentA = new HashMap<>();
			HashMap<Character, Integer> componentB = new HashMap<>();
			for(int i = 0; i < s.length()/2; i++){
				char componentAItem = s.charAt(i);
				char componentBItem = s.charAt(i+(sLen/2));
				componentA.put(componentAItem, 0);
				componentB.put(componentBItem, 0);
				if(componentB.containsKey(componentAItem)){
					int asciiValue = (int)componentAItem;
					int priorityScore = asciiValue <= 90 ? asciiValue -38 : asciiValue - 96;
					partAScore += priorityScore;
					break;
				}
				else if(componentA.containsKey(componentBItem)){
					int asciiValue = (int)componentBItem;
					int priorityScore = asciiValue <= 90 ? asciiValue -38 : asciiValue - 96;
					partAScore += priorityScore;
					break;
				}
			}
		}

		//part B Solution
		int partBScore = 0;
		for(int i = 0; i < ruckSacks.size(); i+=3){
			String ruckSackA = ruckSacks.get(i);
			String ruckSackB = ruckSacks.get(i+1);
			String ruckSackC = ruckSacks.get(i+2);
			char badgeItem = ' ';
			HashMap<Character, Boolean> ruckSackAItems = new HashMap();
			for(Character c : ruckSackA.toCharArray()){
				ruckSackAItems.put(c, true);
			}
			HashMap<Character, Boolean> ruckSackBItems = new HashMap();
			for(Character c : ruckSackB.toCharArray()){
				if(ruckSackAItems.containsKey(c)){
					ruckSackBItems.put(c, true);
				}
			}
			HashMap<Character, Boolean> ruckSackCItems = new HashMap();
			for(Character c : ruckSackC.toCharArray()){
				if(ruckSackAItems.containsKey(c) && ruckSackBItems.containsKey(c)){
					badgeItem = c;
					break;
				}
			}
			int asciiValue = (int)badgeItem;
			int priorityScore = asciiValue <= 90 ? asciiValue -38 : asciiValue - 96;
			partBScore += priorityScore;
		}
		System.out.println("Total Priority Score For Part A: " + partAScore);
		System.out.println("Total Priority Score For Part B: " + partBScore);
	}
}