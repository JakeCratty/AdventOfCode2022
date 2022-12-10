import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.lang.Math;
import java.util.Iterator;

class Main2
{
	
	private static class Vector{
		int x, y;
		Vector(int x, int y)
		{ 
			this.x = x; 
			this.y = y; 
		}

		public String toString(){
			return this.x + ", " + this.y;
		}
	}

	private static class Knot{
		Knot parent;
		Knot child;
		Vector pos;
		int id;
		public HashMap<Integer, HashMap<Integer, Integer>> visitedSpaces = new HashMap<Integer, HashMap<Integer, Integer>>();

		Knot(int id){
			System.out.println("Creating knot " + id);
			this.parent = null;
			this.child = null;
			this.pos = new Vector(0, 0);
			this.id = id;
		}

		public void addChild(Knot knot){
			if(this.child == null){
				knot.parent = this;
				this.child = knot;
			}
			else this.child.addChild(knot);
		}

		public void move(){
			if(parent.pos.x == pos.x){  //if you're in the same column as the parent
				int dif = parent.pos.y - pos.y;
				if(Math.abs(dif) == 2){ //if they're two spaces away
					this.pos.y += (dif/2); //either +1 or -1, depending on direction
				}
				//this knot won't move if the parent is on top or one away from it
			}
			else if(parent.pos.y == pos.y){ //if you're in the same row
				int dif = parent.pos.x - pos.x;
				if(Math.abs(dif) == 2){ //if they're two spaces away
					this.pos.x += (dif/2); //either +1 or -1, depending on direction
				}
			}
			else{ //if not in the same col or row, then a diagnol move is present
				int yDif = parent.pos.y - pos.y; //difference in rows
				int xDif = parent.pos.x - pos.x; //difference in cols
				if(Math.abs(xDif) == 2 && Math.abs(yDif) == 2){ //fully diagonal movement
					this.pos.y += (yDif/2); //either +1 or -1, depending on direction
					this.pos.x += (xDif/2); //either +1 or -1, depending on direction
				}
				else if(Math.abs(yDif) == 2){ //if they're two spaces away by row
					this.pos.y += (yDif/2); //either +1 or -1, depending on direction
					this.pos.x = parent.pos.x; //set knot to same col as parent knot
				}
				else if(Math.abs(xDif) == 2){
					this.pos.x += (xDif/2); //either +1 or -1, depending on direction
					this.pos.y = parent.pos.y; //set knot to same row as parent knot
				}
			}
			System.out.println("Knot " + this.id + " at " + this.pos.toString());
		}

		public void updateChild(){
			if(parent != null)
				move();
			if(this.child == null) {
				addSpace(pos);
				return;
			}
			child.updateChild();
		}

		private void addSpace(Vector tailPos)
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

	static Knot head;
	static HashMap<Integer, HashMap<Integer, Integer>> visitedSpaces = new HashMap<Integer, HashMap<Integer, Integer>>();

	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}

		head = new Knot(0);
		for(int i = 1; i < 10; i++){
			head.addChild(new Knot(i));
		}
		System.out.println();

		//read the input and move the head with the inputs
		for(String s : stringList){
			String[] input = s.split(" ");
			Character headDirection = input[0].charAt(0);
			int len = Integer.valueOf(input[1]);

			for(int i = 0; i < len; i++){
				moveHead(headDirection);
				head.updateChild();
			}
		}

		//iterate to the tail knot
		Knot[] currentKnot = {head};
		while(currentKnot[0].child != null) currentKnot[0] = currentKnot[0].child;

		Iterator rows = currentKnot[0].visitedSpaces.keySet().iterator();
		int[] count = {0};
		rows.forEachRemaining(xPos -> {
			HashMap<Integer, Integer> map = currentKnot[0].visitedSpaces.get(xPos);
			Iterator cols = map.keySet().iterator();
			cols.forEachRemaining(yPos -> {
				count[0]++;
				//System.out.println(xPos + ", " + yPos);
			});
		});
		System.out.println("Tail visited " + count[0] + " positions.");
	}

	public static void moveHead(Character headDirection){
		switch(headDirection){
			case 'R':
				head.pos.x++;
				break;
			case 'L':
				head.pos.x--;
				break;
			case 'U':
				head.pos.y++;
				break;
			case 'D':
				head.pos.y--;
				break;
		}
		System.out.println("Head at: " + head.pos.toString());
	}
}