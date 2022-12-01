import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;

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
		ArrayList<Integer> intList = new ArrayList();

		int bestSum = 0;
		int secondSum = 0;
		int thirdSum = 0;
		int currentSum = 0;
		for (String line : stringList){
			if(line.isEmpty()){
				if(currentSum > bestSum){
					thirdSum = secondSum;
					secondSum = bestSum;
					bestSum = currentSum;
				}
				else if(currentSum > secondSum){
					thirdSum = secondSum;
					secondSum = currentSum;
				}
				else if(currentSum > thirdSum){
					thirdSum = currentSum;
				}
				currentSum = 0;
			}
			else{
				currentSum += Integer.valueOf(line);
			}
		}
		System.out.println(bestSum + secondSum + thirdSum);
	}
}