public class DatabaseMine implements DatabaseInterface {

	private class Node{
		String key;
		String value;

		Node(String key, String value){
			this.key = key;
			this.value = value;
		}
	}
	 int N; // this is a prime number that gives the number of addresses
	// these constructors must create your hash tables with enough positions N
	// to hold the entries you will insert; you may experiment with primes N

	Node[] hashmap;
	int size;
	int probes;
	int displacements;
 
	public DatabaseMine() { // here you pick suitable default N
		N = 191057;
		hashmap = new Node[N];
		size=0;
		
	} 

	 public DatabaseMine(int N){  // here the N is given by the user
		this.N = N;
		hashmap = new Node[N];
		size=0;


	 }

 	int hashFunction(String key) {
		int address=key.hashCode()%N;
		return (address>=0)?address:(address+N);
 	}

	public String save(String plainPassword, String encryptedPassword) throws IllegalStateException{
		int d=1;
		int hash = hashFunction(encryptedPassword);
		//checks if index of hash is free
		if(hashmap[hash]==null){
			hashmap[hash] = new Node(encryptedPassword, plainPassword);
			size++;
			probes++;
			return null;
		}
		//else does linear probing until it finds a free spot in the list or until it returns to the original hash
		else{
			while(true){
				probes++;
				if((hash+d)%N==hash){
					//list is full
					throw new IllegalStateException();
				}
				if(hashmap[(hash+d)%N]!=null && hashmap[(hash+d)%N].key.equals(encryptedPassword)){
					//replace the old value with the new value, return the old value
					String temp = hashmap[(hash+d)%N].value;
					hashmap[(hash+d)%N].value = plainPassword;
					displacements++;
					return temp;
				}
				else if(hashmap[(hash+d)%N]!=null && !hashmap[(hash+d)%N].key.equals(encryptedPassword)){
					//continue linear probing
					d++;
				}
				else{
					//place the key value pair in the empty spot
					hashmap[(hash+d)%N] = new Node(encryptedPassword, plainPassword);
					size++;
					displacements++;
					return null;
				}
			}
		}
		
	}

	public String decrypt(String encryptedPassword){
		int d=1;
		int hash = hashFunction(encryptedPassword);

		if(hashmap[hash]==null){
			//key not in the hashmap
			return "";
		}
		if(hashmap[hash%N].key.equals(encryptedPassword)){
			//found the key on the first try
			return hashmap[hash].value;
		}
		while(true){
			//do linear probing until you find the key
			if((hash+d)%N==hash){
				//key not in the list
				return "";
			}
			if(hashmap[(hash+d)%N]==null){	
				//key not in the list
				return "";
			}
			if(hashmap[(hash+d)%N].key.equals(encryptedPassword)){
				//key found
				return hashmap[(hash+d)%N].value;
			}
			d++;
		}
	}

	public int size(){
		return this.size;
	}

	public void printStatistics() {
		// important statistics must be collected (here or during construction) and
		//printed here: size, number of indexes, load factor, average number of probes
		//and average number of displacements (see output example below)
		System.out.println("*** DatabaseMine Statistics ***\n"+
		 "Size is "+size()+" passwords \n"+
		 "Number of Indexes is "+N+
		 "\nLoad Factor is "+size()/(float)N+
		 "\nAverage Number of Probes is "+probes+
		 "\nNumber of displacements (due to collisions) is "+displacements+
		 "\n*** End DatabaseMine Statistics ***");
	}
}