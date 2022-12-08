import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;

class Main
{
	private static class Tree{
		int row, col;
		boolean isVisible;
		int scenicScore;
		int height;
		Tree(int row, int col, int height){
			this.row = row;
			this.col = col;
			this.isVisible = false;
			this.scenicScore = 0;
			this.height = height;
		}

		public void setVisible(boolean isVisible){
			if(isVisible)
				System.out.println("Tree at " + row + ", " + col);
			this.isVisible = isVisible;
		}

		public boolean getVisibility(){ return this.isVisible; }
	}

	private static Tree[][] forest;
	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}

		forest = new Tree[stringList.get(0).length()][stringList.size()];
		for(int i = 0; i < stringList.size(); i++){
			String row = stringList.get(i);
			int highestTreeLeft = -1;
			int highestTreeRight = -1;
			for(int j = 0; j < row.length(); j++){
				int leftIndex = j;
				int rightIndex = row.length() - 1 - j;
				int leftPtr = Character.getNumericValue(row.charAt(leftIndex));
				int rightPtr = Character.getNumericValue(row.charAt(rightIndex));

				if(leftPtr > highestTreeLeft){
					highestTreeLeft = leftPtr;
					markVisible(i, leftIndex, true, leftPtr);
				}
				else{
					markVisible(i, leftIndex, false, leftPtr);
				}
				if(rightPtr > highestTreeRight){
					highestTreeRight = rightPtr;
					markVisible(i, rightIndex, true, rightPtr);
				}
				else{
					markVisible(i, rightIndex, false, rightPtr);
				}
			}
		}
		for(int i = 0; i < stringList.get(0).length(); i++){
			int column = i;
			int highestTreeTop = -1;
			int highestTreeBottom = -1;
			for(int j = 0; j < stringList.size(); j++){
				int topIndex = j;
				int bottomIndex = stringList.size() - 1 - j;
				int topPtr = Character.getNumericValue(stringList.get(topIndex).charAt(column));
				int botPtr = Character.getNumericValue(stringList.get(bottomIndex).charAt(column));

				if(topPtr > highestTreeTop){
					highestTreeTop = topPtr;
					markVisible(topIndex, column, true, topPtr);
				}
				else{
					markVisible(topIndex, column, false, topPtr);
				}
				if(botPtr > highestTreeBottom){
					highestTreeBottom = botPtr;
					markVisible(bottomIndex, column, true, botPtr);
				}
				else{
					markVisible(bottomIndex, column, false, botPtr);
				}
			}
		}

		int numVisible = 0;
		int highestScore = 0;
		for(int i = 0; i < stringList.size(); i++){
			for(int j = 0; j < stringList.get(0).length(); j++){
				Tree tree = forest[i][j];
				calculateScenicScore(tree, stringList);
				if(tree.scenicScore > highestScore) highestScore = tree.scenicScore;
				if(tree.getVisibility()){
					numVisible++;
					//System.out.println("Tree at " + i + ", " + j);
				}
			}
		}
		System.out.println("Total number of visible trees is " + numVisible);
		System.out.println("The heighest scenic score is " + highestScore);
	}

	public static void markVisible(int row, int col, boolean isVisible, int height){
		if(forest[row][col] == null){
			Tree newTree = new Tree(row, col, height);
			newTree.setVisible(isVisible);
			forest[row][col] = newTree;
		}
		else if(!forest[row][col].getVisibility()){
			forest[row][col].setVisible(isVisible);
		}
	}

	public static void calculateScenicScore(Tree tree, ArrayList<String> input){
		int treesToLeft = 0;
		for (int i = tree.col - 1; i >= 0; i--){
			Tree treeToCheck = forest[tree.row][i];
			if(treeToCheck.height < tree.height) {
				treesToLeft++;
			}
			else{
				treesToLeft++;
				break;
			}
		}
		int treesToRight = 0;
		for (int i = tree.col+1; i < input.get(0).length(); i++){
			Tree treeToCheck = forest[tree.row][i];
			if(treeToCheck.height < tree.height) {
				treesToRight++;
			}
			else{
				treesToRight++;
				break;
			}
		}
		int treesAbove = 0;
		for (int i = tree.row-1; i >= 0; i--){
			Tree treeToCheck = forest[i][tree.col];
			if(treeToCheck.height < tree.height) {
				treesAbove++;
			}
			else{
				treesAbove++;
				break;
			}
		}
		int treesBelow = 0;
		for (int i = tree.row+1; i < input.size(); i++){
			Tree treeToCheck = forest[i][tree.col];
			if(treeToCheck.height < tree.height) {
				treesBelow++;
			}
			else{
				treesBelow++;
				break;
			}
		}
		tree.scenicScore = (treesToLeft * treesToRight * treesAbove * treesBelow);
	}
}