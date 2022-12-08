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


		String inputString = stringList.get(0);
		System.out.println(inputString);
		int startOfPacketMarker = 0;
		int startOfMessageMarker = 0;
		for(int i = 3; i < inputString.length(); i++){
			HashMap<Character, Integer> chars = new HashMap<Character, Integer>();
			for(int j = i; j >= i - 3; j--){
				chars.put(inputString.charAt(j), 0);
			}
			if(chars.keySet().size() == 4){
				if(startOfPacketMarker == 0){
					System.out.println("Got packet marker at " + (i + 1));
					startOfPacketMarker = i + 1; //get the ith char, which is index + 1
				}
			}

			for(int j = i-4; j >= i - 13; j--){
				if(j >= 0) chars.put(inputString.charAt(j), 0);
			}
			if(chars.keySet().size() == 14){
				startOfMessageMarker = i + 1;
				break;
			}
		}
		System.out.println(startOfPacketMarker);
		System.out.println(startOfMessageMarker);
	}
}