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

class Monkey {
	public ArrayDeque<BigInteger> items;
	public BigInteger itemsInspected;
	public String operator;
	public String opConstant;
	public int testConstant;
	public int monkey1;
	public int monkey2;
	public int id;

	Monkey(ArrayDeque items, String operator, String opConstant, int testConstant, int monkey1, int monkey2, int id){
		this.items = items;
		this.itemsInspected = BigInteger.ZERO;
		this.operator = operator;
		this.opConstant = opConstant;
		this.testConstant = testConstant;
		this.monkey1 = monkey1;
		this.monkey2 = monkey2;
		this.id = id;
	}

	public void inspect(){
		BigInteger firstItem = this.items.peek();
		this.itemsInspected = this.itemsInspected.add(BigInteger.ONE);
	}

	public void operate(){
		BigInteger firstItem = this.items.remove();
		BigInteger constant;
		try{
			constant = new BigInteger(opConstant);
		}
		catch(Exception e){
			constant = firstItem;
		}
		switch(operator){
			case "+":
				firstItem = firstItem.add(constant);
				break;
			case "-":
				firstItem = firstItem.subtract(constant);
				break;
			case "*":
				firstItem = firstItem.multiply(constant);
				break;
			case "/":
				firstItem = firstItem.divide(constant);
				break;
		}

		//comment this in for part 1, part 2 removed it
		//firstItem /= 3; //degrade after inspection

		this.items.addFirst(firstItem);
	}
}

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

		ArrayList<Monkey> monkeys = new ArrayList();
		int cycleLength = 1;
		for(int i = 0; i < stringList.size(); i++){
			String s = stringList.get(i);
			if(s.contains("Monkey")){
				Monkey newMonkey;
				//get the items the monkey holds
				String startingItemsString = stringList.get(i+1);
				String[] tokens = startingItemsString.split(": ")[1].split(", "); //extract the items as tokens
				ArrayDeque<BigInteger> itemsQueue = new ArrayDeque<BigInteger>();
				for(int j = 0; j < tokens.length; j++){
					itemsQueue.add(new BigInteger(tokens[j]));
				}
				// get the operator and the opConstant
				String operationString = stringList.get(i+2);
				String[] operationStringItems = operationString.split("old ");
				String operator = operationStringItems[1].split(" ")[0];
				String constant = operationStringItems[1].split(" ")[1];

				//get the test constant
				int testConstant = Integer.valueOf(stringList.get(i+3).split("by ")[1]);
				cycleLength *= testConstant;

				//finally get monkey 1 and 2
				int monkey1 = Integer.valueOf(stringList.get(i+4).split("monkey ")[1]);
				int monkey2 = Integer.valueOf(stringList.get(i+5).split("monkey ")[1]);

				//construct the monkey
				newMonkey = new Monkey(itemsQueue, operator, constant, testConstant, monkey1, monkey2, monkeys.size());
				monkeys.add(newMonkey);
			}
		}

		//for each round in 20 rounds
		//part 2 is 10,000 rounds
		for(int i = 0; i < 10000; i++){
			for(Monkey monkey : monkeys){
				while(!monkey.items.isEmpty()){
					monkey.inspect();
					monkey.operate();
					throwItem(monkey, monkeys, cycleLength);
				}
			}
		}

		BigInteger highest = BigInteger.ZERO;
		BigInteger secondHighest = BigInteger.ZERO;
		for(Monkey monkey : monkeys){
			System.out.println("Monkey: " + monkey.id + " has inspected " + monkey.itemsInspected);
			if(monkey.itemsInspected.compareTo(highest) == 1){
				secondHighest = highest;
				highest = monkey.itemsInspected;
			}
			else if(monkey.itemsInspected.compareTo(secondHighest) == 1){
				secondHighest = monkey.itemsInspected;
			}
		}
		//System.out.println("The level of monkey business is " + (highest * secondHighest));
		System.out.println("The most inspected items is: " + highest);
		System.out.println("The second most inspected items is: " + secondHighest);
		BigInteger monkeyBusiness = highest.multiply(secondHighest);
		System.out.println(monkeyBusiness);
	}

	public static int monkey2Items = 0;
	public static void throwItem(Monkey monkey, ArrayList<Monkey> monkeyList, Integer cycleLength){
		BigInteger item = monkey.items.remove();
		item = (item.mod(new BigInteger(cycleLength.toString())));
		int nextMonkey = (item.mod(new BigInteger(((Integer)(monkey.testConstant)).toString())).equals(BigInteger.ZERO)) ? monkey.monkey1 : monkey.monkey2;
		monkeyList.get(nextMonkey).items.add(item);
	}
}