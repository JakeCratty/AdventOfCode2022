import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.lang.Math;
import java.util.Iterator;
import java.lang.Thread;
import java.util.ArrayDeque;
import java.math.BigInteger;
import java.util.PriorityQueue;

class Node implements Comparable<Node>
{
	public int height;
	public int row, col;
	public Node parent = null;
	private ArrayList<Node> neighbors = new ArrayList<Node>();
	public int dir = 0; //0, 1, 2, 3 [0 - moves up on x axis]
	public int index;
	private double f, g, h;

	public Node(int row, int col, int height, int index)
	{
		this.index = index;
		this.row = row;
		this.col = col;
		this.height = height;
	}

	@Override
	public int compareTo(Node n) {
		return Double.compare(this.f, n.f);
	}

	public void setH(double h) { this.h = h; }
	public void setG(double g){ this.g = g; }
	public void setF(double f){ this.f = f; }

	public double getH() { return this.h; }
	public double getG() { return this.g; }
	public double getF() { return this.f; }

	public ArrayList<Node> getNeighbors(){ return this.neighbors; }

	public void setParent(Node n){ this.parent = n; }
	public Node getParent(){ return this.parent; }

	public void addNeighbor(Node neighbor){
		if(neighbor != null)
			this.neighbors.add(neighbor);
	}

	public void calculateH(Node start)
	{
		double chebyDist = distance(start);
		this.h = chebyDist;
	}

	public double distance(Node n)
	{
		int xDist = Math.abs(this.col - n.col);
		int yDist = Math.abs(this.row - n.row);
		double dist = Math.sqrt(Math.pow(yDist,2) + Math.pow(xDist, 2));
		return 1;
	}

	public void printNeighbors(){
		for(Node n : neighbors)
			System.out.println("Neighbor: " + n.row + ", " + n.col);
	}
}

class Main
{
	static ArrayList<Node> nodeList = new ArrayList<>();
	static ArrayList<Integer> aIndices = new ArrayList<>();
	static ArrayList<String> stringList = null;

	static int columns;
	static int rows;
	static int startIndex = 0;
	static int endIndex = 0;

	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}
		columns = stringList.get(0).length();
		rows = stringList.size();

		for(int row = 0; row < stringList.size(); row++)
		{
			String rowString = stringList.get(row);
			for(int col = 0; col < rowString.length(); col++) 
			{
				Character c = rowString.charAt(col);
				int index = row * columns + col;
				if(c.equals('S')){
					c = 'a';
					startIndex = index;
					aIndices.add(index);
				}
				else if(c.equals('E')){
					c = 'z';
					endIndex = index;
				}
				else if(c.equals('a')){
					aIndices.add(index);
				}
				Node newNode = new Node(row, col, (int)c, index);
				nodeList.add(newNode);
				if(row > 0){
					Node aboveNode = nodeList.get(index - columns);
					if(newNode.height - aboveNode.height <= 1)
						aboveNode.addNeighbor(newNode);
					if(aboveNode.height - newNode.height <= 1)
						newNode.addNeighbor(aboveNode);
				}
				if(col > 0){
					Node leftNode = nodeList.get(index - 1);
					if(newNode.height - leftNode.height <= 1)
						leftNode.addNeighbor(newNode);
					if(leftNode.height - newNode.height <= 1)
						newNode.addNeighbor(leftNode);
				}
			}
		}

		//partOne();
		partTwo();
	}

	private static void partOne(){

		getPathToNode(nodeList.get(startIndex), nodeList.get(endIndex));
		Node currentNode = nodeList.get(endIndex);
		int count = 0;
		System.out.println(startIndex);
		System.out.println(endIndex);
		System.out.println();
		while(currentNode != null){
			System.out.println(currentNode.index);
			char c = getDirection(currentNode);
			if(endIndex == currentNode.index) c='E';
			String row = stringList.get(currentNode.row);
			row = row.substring(0, currentNode.col) + c
              + row.substring(currentNode.col + 1);
            System.out.println("adding char " + c);
            stringList.set(currentNode.row, row);
			currentNode = currentNode.parent;
			count++;
		}
		System.out.println(count-1);		

		try{
			Files.write(Paths.get("output.txt"),stringList);
		}
		catch(IOException e){
		}
	}

	private static void partTwo(){
		int shortestPath = -1;
		int shortIndex = 0;
		System.out.println(aIndices.size());
		for(Integer index : aIndices){
			int count = 0;
			Node currentNode = nodeList.get(endIndex);
			getPathToNode(nodeList.get(index), nodeList.get(endIndex));
			while(currentNode != null){
				currentNode = currentNode.parent;
				count++;
			}
			if(count > 1){
				if(count < shortestPath || shortestPath == -1 ){
					shortestPath = count;
					shortIndex = index;
				}
				System.out.println("Shortest Path: " + (shortestPath-1));
			}
			for(Node n : nodeList) n.parent = null;
		}
		System.out.println("Shortest path index: " + shortIndex);
		System.out.println("Shortest Path: " + (shortestPath-1));
	}

	private static char getDirection(Node n){

		if(n.parent == null) return 'S';
		if(n.parent.row < n.row) return '^';
		else if(n.parent.row > n.row) return 'v';
		else if(n.parent.col > n.col) return '>';
		else return '<';
	}

	private static void getPathToNode(Node start, Node destination)
	{
		PriorityQueue<Node> closed = new PriorityQueue<>();
		PriorityQueue<Node> open = new PriorityQueue<>();
		for(Node n : nodeList){
			n.calculateH(start); //update the H value based on the distance to the start node
			n.setG(0);
			n.setF(0);
		}

		start.setF(0);
		open.add(start);
		while(!open.isEmpty()) 
		{
			Node next = open.peek();
			ArrayList<Node> neighbors = next.getNeighbors();
			if(next == destination) {
				return;
			}
			else{
				//do nothing
			}
			for(Node neighbor : neighbors) {
				double newG = next.getG() + neighbor.distance(next);
				if(!closed.contains(neighbor) && !open.contains(neighbor)) { //if it has neither been opened nor closed
					neighbor.setParent(next);
					neighbor.setG(newG);
					neighbor.setF(neighbor.getG() + neighbor.getH());
					open.add(neighbor);
				}
				else{
					if(newG < neighbor.getG()){
						neighbor.setParent(next);
						neighbor.setG(newG);
						neighbor.setF(neighbor.getG() + neighbor.getH());

						if(closed.contains(neighbor)){
							closed.remove(neighbor);
							open.add(neighbor);
						}
					}
				}
			}
			open.remove(next);
			closed.add(next);
		}
		return;
	}
}

