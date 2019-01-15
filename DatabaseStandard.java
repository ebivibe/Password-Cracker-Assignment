import java.util.HashMap;

public class DatabaseStandard implements DatabaseInterface {
	HashMap<String, String>  database;
	 
	public DatabaseStandard(){
		database = new HashMap<String, String>();
	}

	public String save(String plainPassword, String encryptedPassword){
		return database.put(encryptedPassword, plainPassword);
	}

	public String decrypt(String encryptedPassword){
		String temp = database.get(encryptedPassword);
		if(temp==null){
			return "";
		}
		else{	
			return temp;
		}

	}
	public int size(){
		return database.size();
	}

	public void printStatistics(){
		System.out.println("*** DatabaseStandard Statistics ***\n" +
		"Size is "+size()+" passwords\nInitial Number of Indexes when Created: 16\n" +
		"*** End DatabaseStandard Statistics ***");
	}

}