package AlgorithmicGroupTheory;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.PermutationGroup;

public class groupFunctions {
	
	/**
	 * Summing entries until an index
	 * @param list ArrayList<Integer>
	 * @param i final index
	 */
		
	public static int sum(ArrayList<Integer> list, int index) {
		int sum=0;
		for(int i=0;i<=index;i++) {
			sum=sum+list.get(i);
		}
		return sum;
	}
	
	/**
	 * Summing entries of a integer list.
	 * @param list ArrayList<Integer>
	 * @return int
	 */
		
	public static int sum(ArrayList<Integer> list) {
		int sum=0;
		for(Integer i:list) {
			sum=sum+i;
		}
		return sum;
	}
	
	/**
	  * Values for the second generator permutation - with the full ( 1,2,..,n)
	  * @param id the id values
	  * @param begin first value in the sub partition
	  * @param end	the last value in the sub partition
	  * @return
	  */
	
	 public static int[] fullValues(int total, int begin, int end) {
		 int[] id=idValues(total);
		 for(int i=begin;i<end-1;i++) {
			 id[i]=i+1;
		 }
		 id[end-1]=begin;
		 return id;
	 }
	 
	/**
	  * Values for an id permutation for a given size
	  * @param size 
	  * @return
	  */
	 public static int[] idValues(int size) {
		 int[] id= new int[size];
		 for(int i=0;i<size;i++) {
			 id[i]=i;
		 }
		 return id;
	 }
	
	 /**
	  * Build id permutation
	  */
	 
	 public static Permutation idPermutation(int size) {
		 Permutation perm= new Permutation(size);
		 return perm;
	 }
	 
	 /**
	  * Acts a permutation p on a given int array. 
	  * The permutation permutates entries by the re-arrangement. 
	  * 
	  * @param strip int[]
	  * @param p   Permutation permutation from a permutation group
	  * @return int[] modified array based on the acts of the permutation
	  */
	
	 public static int[] actArray(int[] strip, Permutation p) {
		 int length= p.size();
		 int[] modified = new int[length];
		 for(int i=0; i<length;i++) {
			 modified[p.get(i)]=strip[i]; 
		 }
		 return modified;
	 }
	
	 /**
	  * Since permutations starts with 0, the subpartitions should also start with 0.
	  * @param partition
	  * @param index
	  * @return
	  */
		 
	 public static ArrayList<Integer> subPartition(ArrayList<Integer> partition, int index){
		 ArrayList<Integer> sub= new ArrayList<Integer>();
		 int former = sum(partition,index-1)+1; //TODO: This function is not coded in a proper way. Check later.
		 int latter = sum(partition,index);
		 if(former==latter) {
			 sub.add(former-1);
		 }else {
			 for(int i=former-1;i<=latter-1;i++) {
				 sub.add(i);
			 }
		 }
		 return sub;
	 }
		
	 public static ArrayList<ArrayList<Integer>> subPartitionsList(ArrayList<Integer> partition){
		 ArrayList<ArrayList<Integer>> parts = new ArrayList<ArrayList<Integer>>();
		 for(int i=0;i<partition.size();i++) {
			 parts.add(subPartition(partition,i)); 
		 }
		 return parts;
	 }
		
	 /**
	  * Generating a group from a list permutations
	  * @param perm a list of permutations	
	  * @return permutation group generated by a list of permutations
	  */
	
	 public static PermutationGroup generateGroup(List<Permutation> generators,int size) {
		 return new PermutationGroup(size, generators); 
	 }
	
	 /**
	  * Youngsubgroup generation based on the generator permutations. These permutations are built for
	  * each sub partitions
	  * @param partition atom partition
	  * @param size number of atoms
	  * @return
	  */
	 public static PermutationGroup getYoungGroup(ArrayList<Integer> partition, int size) {
		 //System.out.println("young part"+" "+partition+" "+getYoungsubgroupGenerators(partition,size));
		 return generateGroup(getYoungsubgroupGenerators(partition,size),size); 
	 }
	 
	 /**
	  * Example: For a set {5,6,7} we need to get S3. Generators are (12) and (123).
	  * This 1 2 3 are the indices of the set elements. So we need to replace them 
	  * with the values. The generators also should start with 0 1 2 until 5. So
	  * If the size of the total partition is 10; the generators are
	  * 
	  * [0,1,2,3,4,6,5,7,8,9]
	  * [0,1,2,3,4,6,7,5,8,9]
	  *  
	  * @param subPart sub partition
	  * @param total partition size
	  * @return
	  */
	
	 public static List<Permutation> getYoungsubgroupGenerator(ArrayList<Integer> subPart, int total){
		 List<Permutation> perms= new ArrayList<Permutation>();
		 int setSize= subPart.size();
		 int[] id= idValues(total);
		 if(setSize==1) {
			 perms.add(new Permutation(id));
		 }else if(setSize==2) {
			 id[subPart.get(0)]   = subPart.get(0)+1;
			 id[subPart.get(0)+1] = subPart.get(0);
			 perms.add(new Permutation(id));
		 }else {
			
			 /**
			  * The first generator with length 2 which is (1,2)
			  */
			 
			 int firstIndex= subPart.get(0);
			 id[firstIndex]  = firstIndex+1;
			 id[firstIndex+1]= firstIndex;
			 Permutation gen1= new Permutation(id);
			 perms.add(gen1);
			
			 /**
			  * The second generator with the full ( 1,2,..,n)
			  */
			
			 Permutation gen2= new Permutation(fullValues(total,firstIndex,firstIndex+setSize));
			 perms.add(gen2);
		 }
		 return perms;
	 }
	
	/**
	 * Getting the list of generator permutations for all the sub partitions.
	 * @param partition Atom partition
	 * @param total number of atoms
	 * @return
	 */
	
	public static List<Permutation> getYoungsubgroupGenerators(ArrayList<Integer> partition,int total){
		ArrayList<ArrayList<Integer>> parts=subPartitionsList(partition);
		List<Permutation> perms= new ArrayList<Permutation>();
		for(ArrayList<Integer> part:parts) {
			for(Permutation perm:getYoungsubgroupGenerator(part,total)) {
				if(!perm.isIdentity() && !perms.contains(perm)) {
					perms.add(perm);
				}
			}
			//perms.addAll(getYoungsubgroupGenerator(part,total));
		}
		return perms;
	}
}
