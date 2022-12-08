import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;

class Main
{
	public static class Directory{
		private String name;
		private Directory parentDirectory;
		private ArrayList<Directory> subDirectoriesList;
		private int size; //size of all files and sub directories
		
		Directory(String name){
			this.name = name;
			this.subDirectoriesList = new ArrayList<>();
			this.size = 0;
		}

		Directory(String name, Directory parent){
			this.name = name;
			this.subDirectoriesList = new ArrayList<>();
			this.size = 0;
			this.parentDirectory = parent;
		}

		public void addFile(int fileSize)
		{
			System.out.println("Adding filesize of " + fileSize + " to dir " + name);
			this.size += fileSize;
			if(this.parentDirectory != null)
				this.parentDirectory.addFile(fileSize);
		}

		public void addSubDirectory(String name){
			System.out.println("Adding sub directory " + name + " to directory " + this.name);
			this.subDirectoriesList.add(new Directory(name, this));
		}

		public Directory getSubDirectory(String name){
			System.out.println("Switching to dir " + name);
			for(Directory dir : subDirectoriesList){
				if(dir.name.equals(name)) return dir;
			}
			return null;
		}

		public Directory getParentDirectory(){
			System.out.println("Switching to dir " + parentDirectory.name);
			return this.parentDirectory;
		}

		public ArrayList<Directory> getAllSubDirectories(){
			return this.subDirectoriesList;
		}

		public int getSize(){
			return this.size;
		}
	}

	public static void main(String args[])
	{
		ArrayList<String> stringList = null;
		try{
			stringList = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
		}
		catch(IOException e){
		}

		Directory currentDirectory = null;

		for(String s : stringList)
		{
			String[] tokens = s.split(" ");
			if(tokens[0].equals("$")){
				if(tokens[1].equals("cd")){
					if(currentDirectory == null){  //if no dir has been set, create a dir and make it the current one
						System.out.println("Creating root");
						currentDirectory = new Directory(tokens[2]);
					}
					else if(tokens[2].equals("..")){
						currentDirectory = currentDirectory.getParentDirectory();
					}
					else{
						currentDirectory = currentDirectory.getSubDirectory(tokens[2]);
					}
				}
			}
			else{
				if(tokens[0].equals("dir")) currentDirectory.addSubDirectory(tokens[1]);
				else{ //this can only be a filesize and file name
					currentDirectory.addFile(Integer.valueOf(tokens[0]));
				}
			}
		}

		while(currentDirectory.parentDirectory != null){
			currentDirectory = currentDirectory.getParentDirectory();
		}

		//Part A Solution - Find all directories that have a size less than 100,000
		ArrayList<Directory> toRemove = toRemove(currentDirectory);
		int totalSizeSaved = 0;
		for(Directory d : toRemove){
			totalSizeSaved += d.getSize();
		}
		System.out.println("Total Size Saved: " + totalSizeSaved);

		//Part B Solution, delete the smallest directory that frees up X space, where X = 70,000,000 - sizeOf("/");
		int totalFreeSpace = 70000000 - currentDirectory.getSize();
		int requiredSpace = 30000000;
		int spaceToFree = requiredSpace - totalFreeSpace;
		ArrayList<Directory> candidateList = findCandidatesForDeletion(currentDirectory, spaceToFree);
		int smallest = -1;
		for(Directory dir : candidateList){
			if(smallest == -1 || dir.getSize() < smallest){
				smallest = dir.getSize();
			}
		}
		System.out.println("Smallest Directory Size To Delete: " + smallest);
	}

	//part A solution. Recursive function that compiles list of all directories smaller than 100,000 bytes
	public static ArrayList<Directory> toRemove(Directory currentDirectory){
		ArrayList<Directory> toRemoveList = new ArrayList<>();
		for(Directory subDirectory : currentDirectory.getAllSubDirectories()){
			if(subDirectory.size <= 100000){
				toRemoveList.add(subDirectory);
			}
			toRemoveList.addAll(toRemove(subDirectory));
		}
		return toRemoveList;
	}

	//part B solution. Recursive function that compiles list of all directories with a size greater than the needed amount of space to install the update
	public static ArrayList<Directory> findCandidatesForDeletion(Directory currentDirectory, int sizeRequired){
		ArrayList<Directory> candidateList = new ArrayList<>();
		for(Directory subDirectory : currentDirectory.getAllSubDirectories()){
			if(subDirectory.getSize() >= sizeRequired){
				candidateList.add(subDirectory);
			}
			candidateList.addAll(findCandidatesForDeletion(subDirectory, sizeRequired));
		}
		return candidateList;
	}
}