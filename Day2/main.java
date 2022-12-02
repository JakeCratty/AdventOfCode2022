import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;

class Main
{
	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		ArrayList<Integer> otherMoves = new ArrayList();
		ArrayList<Integer> myMoves = new ArrayList();
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}
		for(String s : stringList){
			String[] moves = s.split(" ");
			if(moves[0].equals("A")) otherMoves.add(1);
			else if(moves[0].equals("B")) otherMoves.add(2);
			else otherMoves.add(3);

			int otherMove = otherMoves.get(otherMoves.size()-1);
			if(moves[1].equals("Z")){
				int myMove = 0;
				if(otherMove+1 == 4) myMove = 1;
				else myMove = otherMove + 1;
				myMoves.add(myMove); //1 - 3
			}
			else if(moves[1].equals("Y")) myMoves.add(otherMove);
			else{
				int myMove = 0;
				if(otherMove-1 == 0) myMove = 3;
				else myMove = otherMove - 1;
				myMoves.add(myMove); //1 - 3
			}
		}

		int totalScore = 0;
		for(int i = 0; i < otherMoves.size(); i++){
			int otherMove = otherMoves.get(i);
			int myMove = myMoves.get(i);

			int outcome = 0;
			if(myMove == otherMove) outcome = 3;
			else if(otherMove == 1 && myMove == 3) outcome = 0;
			else if(otherMove == 3 && myMove == 1) outcome = 6;
			else if(myMove > otherMove) outcome = 6;
			totalScore += myMove + outcome;
		}
		System.out.println(totalScore);
	}
}