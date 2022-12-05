import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Collections;
import java.util.Stack;

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

		//get the index of the line that separates the diagram from the move list
		int spaceIndex = 0;
		for(String s : stringList){
			spaceIndex++;
			if(s.isEmpty()) break;
		}

		//get the length of the longest line so we can determine the number of stacks with an equation
		int longestLength = 0;
		for(int i = 0; i < spaceIndex; i++){
			String s = stringList.get(i);
			if(s.length() > longestLength) longestLength = s.length();
		}
		//determine the number of stacks and create an array of character stacks
		int numStacks = (int)((longestLength / 4)) + 1;
		Stack<Character>[] stacks = new Stack[numStacks];
		for(int i = 0; i < numStacks; i++){
			stacks[i] = new Stack<Character>();
		}

		//for each line in the diagram, from the bottom up, get each character and determine which stack it belongs to
		//we do this bottom up to build the stacks in the correct order, otherwise they would be in reverse order
		for(int i = spaceIndex - 3; i >= 0; i--){
			String s = stringList.get(i);
			for(int j = 0; j < s.length(); j++){
				if((j - 1) % 4 == 0 && s.charAt(j) != ' ') stacks[(int)((j - 1) / 4)].push(s.charAt(j));
			}
		}


		//comment in to see part 1 solution
		
		//for each command line, split the string and get the number of crates to move and the stack of each crate to move to and from
		// for(int i = spaceIndex; i < stringList.size(); i++){
		// 	String[] cmds = stringList.get(i).split(" ");
		// 	int moveNum = Integer.valueOf(cmds[1]);
		// 	int from = Integer.valueOf(cmds[3])-1;
		// 	int to = Integer.valueOf(cmds[5])-1;

		// 	//move x number of crates from stack A to stack B
		// 	System.out.println("Grabbing " + moveNum + " from stack " + from + " to stack " + to);
		// 	for(int j = 0; j < moveNum; j++){
		// 		Character crate = stacks[from].pop();
		// 		stacks[to].push(crate);
		// 	}
		// }

		// //build the answer string using the top crate on each stack
		// String partOne = "";
		// for(int i = 0; i < stacks.length; i++){
		// 	partOne += stacks[i].pop();
		// }
		// System.out.println(partOne);



		//PART 2		
		//for each command line, split the string and get the number of crates to move and the stack of each crate to move to and from
		for(int i = spaceIndex; i < stringList.size(); i++){
			String[] cmds = stringList.get(i).split(" ");
			int moveNum = Integer.valueOf(cmds[1]);
			int from = Integer.valueOf(cmds[3])-1;
			int to = Integer.valueOf(cmds[5])-1;

			//move x number of crates from stack A to stack B
			System.out.println("Grabbing " + moveNum + " from stack " + from + " to stack " + to);
			Stack<Character> tempStack = new Stack<Character>();
			for(int j = 0; j < moveNum; j++){
				Character crate = stacks[from].pop();
				tempStack.push(crate);
			}
			for(int j = 0; j < moveNum; j++){
				Character crate = tempStack.pop();
				stacks[to].push(crate);
			}
		}
		String partTwo = "";
		for(int i = 0; i < stacks.length; i++){
			partTwo += stacks[i].pop();
		}
		System.out.println(partTwo);
	}
}