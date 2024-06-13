package processor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class OrdersProcessor{	
	
	public static void main(String[] args) {
		HashMap<String, Double> items = new HashMap<String, Double>();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		
		Scanner input = new Scanner(System.in);
		String fileName = input.next();
		String mult = input.next();
		int numOrders = input.nextInt();
		String baseName = input.next();
		String results = input.next();
		BufferedWriter writer;
		
		
		System.out.println("Enter item's data file name: " + fileName);
		System.out.println("Enter 'y' for multiple threads, any other character otherwise: " + mult);
		System.out.println("Enter number of orders to process: " + numOrders);
		System.out.println("Enter order's base filename: " + baseName);
		System.out.println("Enter result's filename: " + results);
		
		File text = new File(fileName);
		
		long begin = System.currentTimeMillis();
		
		try {
			Scanner scan = new Scanner(text);
			while(scan.hasNextLine()) {
				String item = scan.nextLine();
				double price = Double.parseDouble(item.substring(item.indexOf(" ") + 1)); 
				items.put(item.substring(0, item.indexOf(" ")), price);
			}
			
			input.close();
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Orders order = new Orders(items);
		
		if(mult.equals("y")) {
			//beginning of multi
			for(int i = 1; i <= numOrders; i++) {
				String name = baseName + i;
				Thread files = new Thread(new OrdersProcessor().new Order(name, items, order));
				threads.add(files);
			}
			
			for(int i = 0; i < threads.size(); i++) {
				threads.get(i).start();
				try {
					threads.get(i).join();
				} catch (InterruptedException e ) {
					e.printStackTrace();
				}
			}
			
			try {
				writer = new BufferedWriter(new FileWriter(results, true));
				writer.append(order.getAll());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long end = System.currentTimeMillis();
			long time = end - begin;
		

			//Print outs
			System.out.println("Processing time (msec): " + time);
			System.out.println("Results can be found in the file: " + results);
			//end of multi
		} else {
			//Beginning of single
			TreeMap<Integer, String> summary = new TreeMap<Integer, String>();
			TreeMap<String, Integer> sum = new TreeMap<String, Integer>();
			for(int i = 1; i <= numOrders; i++) {
				HashMap<String, Integer> quantity = new HashMap<String, Integer>();
				String name = baseName + i + ".txt";
				
				text = new File(name);
				Scanner scan;
				int clientID = 0;
				
				try {
					scan = new Scanner(text);
					String firstLine = scan.nextLine();
					clientID = Integer.parseInt(firstLine.substring(firstLine.indexOf(" ") + 1));
					System.out.println("Reading order for client with id: " + clientID);
					while(scan.hasNext()) {
						String val = scan.nextLine();
						String itm = val.substring(0, val.indexOf(" "));
						
							if(quantity.containsKey(itm)) {
								Integer num = quantity.get(itm);
								quantity.put(itm, num + 1);
								if(sum.containsKey(itm)) {
									sum.put(itm, sum.get(itm)+1); 
								} else {
									sum.put(itm, 1);
								}
							} else {
								quantity.put(itm , 1);
								if(sum.containsKey(itm)) {
									sum.put(itm, sum.get(itm)+1); 
								} else {
									sum.put(itm, 1);
								}
							}
						
					}
					scan.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Map<String, Integer> sortedMap = new TreeMap<String, Integer>(quantity);
				
				String str = "";
				double total = 0;
				str += "----- Order details for client with Id: " + clientID + " -----\n";
				for(Entry<String, Integer> entry: sortedMap.entrySet()) {
					String item = entry.getKey();
					double costPer = items.get(item);
					double cost = costPer * sortedMap.get(item);
					total += cost;
					str += "Item's name: " + item + ", Cost per item: " + 
							NumberFormat.getCurrencyInstance().format(costPer) + 
							", Quantity: " + sortedMap.get(item) + ", Cost: " + 
							NumberFormat.getCurrencyInstance().format(cost) + "\n";
				}
				str += "Order Total: " + NumberFormat.getCurrencyInstance().format(total) + "\n";
				
				summary.put(clientID, str);

			}
			
			String str1 = "";
			double total = 0;
			str1 += "***** Summary of all orders *****\n";
			for(Entry<String, Integer> entry: sum.entrySet()) {
				String item = entry.getKey();
				double costPer = items.get(item);
				double cost = costPer * sum.get(item);
				total += cost;
				str1 += "Summary - Item's name: " + item + ", Cost per item: " + 
						NumberFormat.getCurrencyInstance().format(costPer) + 
						", Number sold: " + sum.get(item) + ", Item's Total: " + 
						NumberFormat.getCurrencyInstance().format(cost) + "\n";
			}
			str1 += "Summary Grand Total: " + NumberFormat.getCurrencyInstance().format(total) + "\n";
			
			String fin = "";
			summary.put(Integer.MAX_VALUE, str1);
			for(Entry<Integer, String> entry: summary.entrySet()) {
				fin += entry.getValue();
			}
			
			try {
				writer = new BufferedWriter(new FileWriter(results, true));
				writer.append(fin);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			long end = System.currentTimeMillis();
			long time = end - begin;

			//Print outs
			System.out.println("Processing time (msec): " + time);
			System.out.println("Results can be found in the file: " + results);
		} //End of single
	}
	
	private class Order implements Runnable{
		private String fileName;
		private HashMap<String, Integer> quantity;
		private HashMap<String, Double> costs;
		private Orders summary;
		
		public Order(String fileName, HashMap<String, Double> costs, Orders order) {
			this.fileName = fileName + ".txt";
			this.costs = costs;
			quantity = new HashMap<String, Integer>();
			summary = order;
		}
		
		@Override
		public void run() {
			File text = new File(fileName);
			Scanner scan;
			int clientID = 0;
			
			try {
				scan = new Scanner(text);
				String firstLine = scan.nextLine();
				clientID = Integer.parseInt(firstLine.substring(firstLine.indexOf(" ") + 1));
				System.out.println("Reading order for client with id: " + clientID);
				while(scan.hasNext()) {
					String val = scan.nextLine();
					String name = val.substring(0, val.indexOf(" "));
					
						if(quantity.containsKey(name)) {
							Integer num = quantity.get(name);
							quantity.put(name, num + 1);
							synchronized(summary) {
								summary.add(name);
							}
						} else {
							synchronized(summary) {
								summary.add(name);
							}
							quantity.put(name , 1);
						}
					
				}
				scan.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Map<String, Integer> sortedMap = new TreeMap<String, Integer>(quantity);
			
			String str = "";
			double total = 0;
			str += "----- Order details for client with Id: " + clientID + " -----\n";
			for(Entry<String, Integer> entry: sortedMap.entrySet()) {
				String item = entry.getKey();
				double costPer = costs.get(item);
				double cost = costPer * sortedMap.get(item);
				total += cost;
				str += "Item's name: " + item + ", Cost per item: " + 
						NumberFormat.getCurrencyInstance().format(costPer) + 
						", Quantity: " + sortedMap.get(item) + ", Cost: " + 
						NumberFormat.getCurrencyInstance().format(cost) + "\n";
			}
			str += "Order Total: " + NumberFormat.getCurrencyInstance().format(total) + "\n";
			
			synchronized(summary) {
				summary.addToString(str, clientID);
			}
			
		}
	}

}