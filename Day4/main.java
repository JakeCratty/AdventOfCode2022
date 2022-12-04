import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;

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

		int overlappingRanges = 0;
		for(String s : stringList){
			String[] idRanges = s.split(",");

			String[] elfARange = idRanges[0].split("-");
			String[] elfBRange = idRanges[1].split("-");

			int elfA_low = Integer.valueOf(elfARange[0]);
			int elfA_high = Integer.valueOf(elfARange[1]);

			int elfB_low = Integer.valueOf(elfBRange[0]);
			int elfB_high = Integer.valueOf(elfBRange[1]);

			if((elfA_low <= elfB_low || elfA_low <= elfB_high) && (elfA_high >= elfB_low || elfA_high >= elfB_high)) overlappingRanges++;
			else if((elfB_low <= elfA_low || elfB_low <= elfA_high) && (elfB_high >= elfA_low || elfB_high >= elfA_high)) overlappingRanges++;
		}
		System.out.println(overlappingRanges);
	}
}