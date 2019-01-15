import java.util.Calendar;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
public class PasswordCracker {

	void createDatabase(ArrayList<String> commonPasswords, DatabaseInterface database) {
		// receives list of passwords and populates database with entries consisting
		// of (key,value) pairs where the value is the password and the key is the
		// encrypted password (encrypted using Sha1)
		// in addition to passwords in commonPasswords list, this class is 
		// responsible to add mutated passwords 
		/*
		1. Capitalize the first letter of each word starting with a letter, e.g. dragon becomes Dragon
		2. Add the current year to the word, e.g. dragon becomes dragon2018
		3. Use @ instead of a, e.g. dragon becomes dr@gon
		4. Use 3 instead of e, e.g. baseball becomes bas3ball
		5. Use 1 instead of i, e.g. michael becomes m1chael
		*/

		for(int i=0; i<commonPasswords.size(); i++){
			String password = commonPasswords.get(i);
			try{
				database.save(password, Sha1.hash(password));
			}
			catch(UnsupportedEncodingException e){
				System.out.println("Error hashing");
			}

			//get all the combinations
			ArrayList<String> combs = allcombs(password);
			//loop through the combinations
			for(int j=0; j<combs.size(); j++){
				try{
					//save the combination, combination with capital, and combination with year appended
					database.save(combs.get(j), Sha1.hash(combs.get(j)));
					String comb1;
					if(combs.get(j).length()>1){
						comb1 = combs.get(j).substring(0, 1).toUpperCase()+combs.get(j).substring(1);
					}
					else{
						comb1 = combs.get(j).substring(0, 1).toUpperCase();
					}
					database.save(comb1, Sha1.hash(comb1));
					database.save(combs.get(j)+Calendar.getInstance().get(Calendar.YEAR), 
					Sha1.hash(combs.get(j)+Calendar.getInstance().get(Calendar.YEAR)));
					database.save(comb1+Calendar.getInstance().get(Calendar.YEAR), 
					Sha1.hash(comb1+Calendar.getInstance().get(Calendar.YEAR)));

					
				}
				catch(UnsupportedEncodingException e){
					System.out.println("Error hashing");
				}

			}
			
		}
	}

	String crackPassword(String encryptedPassword, DatabaseInterface database) {
		return database.decrypt(encryptedPassword);
	} 

	/**
	 * Generates all permutations with e->3, a->@, i->1
	 * @param value String for which to get all permutations of
	 */
	ArrayList<String> allcombs(String value){
		ArrayList<Integer> positions = new ArrayList<Integer>();
		//finds all indexes with a, e, or i
        for(int i=0; i<value.length(); i++){
			if(value.charAt(i)=='a' || value.charAt(i)=='e' || value.charAt(i)=='i' 
			|| value.charAt(i)=='A' || value.charAt(i)=='E' || value.charAt(i)=='I'){
                positions.add(i);
            }
        }
		//all combinations of the indexes
		ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>();
		
		//loop through the combinations possible
        for(int j = 0;j <Math.pow(2, positions.size());j++) {
			ArrayList<Integer> combination = new ArrayList<>();
			//loop through all the indexes
            for(int i = 0;i < positions.size(); i++) {
				//masks num, the current binary string with a string containing a 1 at the location of the index i
				//and 0 everywhere else 
                if((j & (1 << i)) != 0) {  
                    combination.add(positions.get(i));
                }
            }
            // (adds the current combination)
            combinations.add(combination);
        }

        
		ArrayList<String> finalcombinations = new ArrayList<String>();
		//loops through all the combinations of indexes
        for (int i=0; i<combinations.size(); i++){
			//convert string to array of chars
			char[] temp = value.toCharArray();
			//loop through all indexes in current combination
            for(int j=0; j<combinations.get(i).size(); j++){
				//replace with the appropriate character
				if(value.charAt(combinations.get(i).get(j))=='a' || value.charAt(combinations.get(i).get(j))=='A'){
					temp[combinations.get(i).get(j)]='@';
				}
				else if(value.charAt(combinations.get(i).get(j))=='e' || value.charAt(combinations.get(i).get(j))=='A'){
					temp[combinations.get(i).get(j)]='3';
				}
				else{
					temp[combinations.get(i).get(j)]='1';
				}

			}
			//add current combination to the list
            finalcombinations.add(new String(temp));
        }
		
		return finalcombinations;
    }

}