package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Mapper {
	LinkedList<Note> mappedNotes = new LinkedList<Note>();
	HashMap<Integer, LinkedList<Integer>> mappingScheme;
	
	public Mapper(File scheme){
		try{
		if(scheme == null)
			importMappingScheme(new File("default.txt"));
		else
			importMappingScheme(scheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mapFile(File f){
		if(f.isDirectory()){
			for(File subFile: f.listFiles())
				mapFile(subFile);
			return;
		}
		
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scanner.hasNextByte()){
			byte currentbyte = scanner.nextByte();
		}
		scanner.close();
	}
	
	private void importMappingScheme(File f) throws Exception{
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(fileScanner.hasNextLine()){
			Scanner lineScanner = new Scanner(fileScanner.nextLine());
			String value0 = null;
			String value1 = null;
			String noteValue = null;
			String temp = null;
			if(lineScanner.hasNext())
				value0 = lineScanner.next();
			if(lineScanner.hasNext()){
				temp = lineScanner.next();
				if(temp.equals("-")){
					if(lineScanner.hasNext())
						value1 = lineScanner.next();
					if(lineScanner.hasNext())
						noteValue = lineScanner.next();
				}
				else 
					noteValue = temp;
			}
			
			if((value0 == null) || (noteValue == null)){
				lineScanner.close();
				fileScanner.close();
				throw new Exception("Invalid mapping file format!");
			}
			
			int int_value0 = Integer.parseInt(value0, 16);
			int int_noteValue = Integer.parseInt(noteValue, 16);
			
			int_value0 = Math.abs(int_value0) % 256;
			int_noteValue = Math.abs(int_noteValue) % 256;
			
			if(value1 != null){
				int int_value1 = Integer.parseInt(value1, 16);
				int_value1 = Math.abs(int_value1) % 256;
				int start = int_value0;
				int end = int_value1;
				int d = 1;
				if(int_value0 > int_value1){
					start = int_value1;
					end = int_value0;
					d = -1;
				}
				for(int i = start ; i < end ; i += d){
					if(mappingScheme.get(new Integer(i)) == null){
						LinkedList<Integer> newList = new LinkedList<Integer>();
						newList.add(int_noteValue);
						mappingScheme.put(new Integer(i), newList);
					}
					else
						mappingScheme.get(new Integer(i)).add(new Integer(int_noteValue));
				}
			}
			else{
				if(mappingScheme.get(new Integer(int_value0)) == null){
					LinkedList<Integer> newList = new LinkedList<Integer>();
					newList.add(int_noteValue);
					mappingScheme.put(new Integer(int_value0), newList);
				}
				else
					mappingScheme.get(new Integer(int_value0)).add(new Integer(int_noteValue));
			}
			
			lineScanner.close();
		}
		fileScanner.close();
	}
}
