package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.junit.Test;

import processor.OrdersProcessor;

/**
 * 
 * You need student tests if you are looking for help during office hours about
 * bugs in your code.
 * 
 * @author UMCP CS Department
 *
 */
public class studentTests {

	@Test
	public void testmain() throws Exception, Throwable{
		String results = getResults("reportData.txt");
		File file = new File(results);
		file.delete();
		TestingSupport.redirectStandardInputTo("reportData.txt");
		OrdersProcessor.main(null);
	}

	private static String getResults(String filename) throws FileNotFoundException {
		Scanner scan = new Scanner(new BufferedReader(new FileReader(filename)));
		for(int i = 1; i <= 4; i++) {
			scan.next();
		}
		String name = scan.next();
		scan.close();
		
		return name;
	}
}
