import java.util.*;

public class midterm3collections {
	public static void main(String[] args) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("Alyssa", 25);
		m.put("Char", 25);
		m.put("Dan", 25);
		m.put("Jeff", 20);
		m.put("Kasey", 20);
		m.put("Kim", 20);
		m.put("Mogran", 25);
		m.put("Ryan", 25);
		m.put("Stef", 25);
	
		int rare = rarestAge(m);
		System.out.println(rare);
	}
	
	
	
	public static int rarestAge(Map<String, Integer> m) {
		if(m == null || m.size() == 0) {
			throw new IllegalArgumentException();
		}
		
		Map<Integer, Integer> ageCount = new TreeMap<Integer, Integer>();
		
		for(String s : m.keySet()) {
			if(!ageCount.containsKey(m.get(s))) {
				ageCount.put(m.get(s), 1); // Create new entry
			} else {
				ageCount.put(m.get(s), ageCount.get(m.get(s)) + 1); // ++ existing entry
			}
			
		}
			
		int minCount = m.size() + 1;
		int rarest = -1;
		for(int i : ageCount.keySet()) {
			int curr = ageCount.get(i);
			if(curr < minCount) {
				minCount = curr;
				rarest = i;
			}
		}		
		return rarest;
	}
}
