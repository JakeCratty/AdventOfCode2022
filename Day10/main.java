import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.lang.Math;
import java.util.Iterator;

class Main
{

	public static int x = 1;
	public static int currentCycle = 1;
	public static int sum = 0;
	public static int spriteWidth = 3;
	public static int crtWidth = 40;
	public static int crtHeight = 6;
	public static String[] monitor = new String[crtHeight];

	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}
		for(int i = 0; i < 6; i++){
			monitor[i] = "";
		}

		HashMap<String, Integer> commandToCycle = new HashMap<String, Integer>();
		commandToCycle.put("noop", 1);
		commandToCycle.put("addx", 2);

		for(String s : stringList){
			String[] tokens = s.split(" ");
			executeInstruction(tokens, commandToCycle.get(tokens[0]));
		}
		System.out.println("Signal strength sum is " + sum);
		for(int i = 0; i < 6; i++){
			System.out.println(monitor[i]);
		}
	}

	public static int currentRow = 0;
	public static int currentCol = 0;
	public static void executeInstruction(String[] instructionSet, int cyclesRemaining){
		//beginning of cycle
		//System.out.println("Beginning of Cycle " + currentCycle + "\tX is " + x);
		if((currentCycle + 20) % 40 == 0){
			System.out.println("Beginning of Cycle " + currentCycle + "\tX is " + x);
			int signalStrength = currentCycle * x;
			sum += signalStrength;
		}

		int pixelPos = x;
		String newChar = (Math.abs(pixelPos - currentCol) <= 1) ? "#" : ".";
		//draw pixel
		monitor[currentRow] += newChar;
		currentCol++;
		if(currentCol > 39){
			currentRow++;
			currentCol = 0;
		}

		currentCycle+=1;
		cyclesRemaining-=1;

		if(instructionSet[0].equals("noop")){
			if(cyclesRemaining > 0) executeInstruction(instructionSet, cyclesRemaining);
			return;
		}
		else if(instructionSet[0].equals("addx")){
			if(cyclesRemaining > 0){
				executeInstruction(instructionSet, cyclesRemaining);
				return;
			}
			else{
				x += Integer.valueOf(instructionSet[1]);
				return;
			}
		}
	}


	//part two output
	/**
	 * 
			###..####.###...##..####.####...##.###..
			#..#....#.#..#.#..#....#.#.......#.#..#.
			#..#...#..###..#......#..###.....#.###..
			###...#...#..#.#.##..#...#.......#.#..#.
			#....#....#..#.#..#.#....#....#..#.#..#.
			#....####.###...###.####.####..##..###.. 

	 * */

}