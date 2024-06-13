package processor;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Orders {
	private HashMap<String, Double> costs;
	private HashMap<String, Integer> summary;
	private TreeMap<Integer, String> results;
	
	
	public Orders(HashMap<String, Double> costs) {
		this.costs = costs;
		summary = new HashMap<String, Integer>();
		results = new TreeMap<Integer, String>();
	}
	
	public synchronized void add(String str) {
		if(summary.containsKey(str)) {
			summary.put(str, summary.get(str)+1); 
		} else {
			summary.put(str, 1);
		}
	}
	
	public void addToString(String str, Integer clientId) {
		results.put(clientId, str);
	}
	
	public String getAll() {
		String var = "";
		results.put(Integer.MAX_VALUE, getSummary());
		for(Entry<Integer, String> entry: results.entrySet()) {
			var += entry.getValue();
		}
		return var;
	}
	
	public String getSummary() {
		String str1 = "";
		double total = 0;
		
		Map<String, Integer> sortedMap = new TreeMap<String, Integer>(summary);
		str1 += "***** Summary of all orders *****\n";
		for(Entry<String, Integer> entry: sortedMap.entrySet()) {
			String item = entry.getKey();
			double costPer = costs.get(item);
			double cost = costPer * sortedMap.get(item);
			total += cost;
			str1 += "Summary - Item's name: " + item + ", Cost per item: " + 
					NumberFormat.getCurrencyInstance().format(costPer) + 
					", Number sold: " + sortedMap.get(item) + ", Item's Total: " + 
					NumberFormat.getCurrencyInstance().format(cost) + "\n";
		}
		str1 += "Summary Grand Total: " + NumberFormat.getCurrencyInstance().format(total) + "\n";
		
		return str1;
	}

}
