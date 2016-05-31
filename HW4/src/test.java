//Grady Williams
//Test Case for AssassinManager object.
import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
public class test {
    
    public ArrayList<String> nameList;
    public String[] some_less_nice_names = {"\t\t\t", "       ", "name with lots of spaces i n i t", "IG-88", "123", "oneONEoneONE"};
    
    @Before
    public void setUp() throws Exception {
        
        //YOINK
        /** input file name from which to read data */                                       
        String INPUT_FILENAME = "names.txt";                                         
        
        // read names into a Set to eliminate duplicates                                 
        File inputFile = new File(INPUT_FILENAME);                                                   
        if (!inputFile.canRead()) {                               
            System.out.println("Required input file not found; exiting.\n" + inputFile.getAbsolutePath());                               
            System.exit(1);                                 
        }                                              
        Scanner input = new Scanner(inputFile);                                    
                               
        //this seems to alphabetize names in the file
        Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);                                         
        while (input.hasNextLine()) {                                     
            String name = input.nextLine().trim().intern();                                                    
            if (name.length() > 0) {                                                   
                names.add(name);                               
            }                                           
        }                                     
                                                                                    
        nameList = new ArrayList<String>(names);
        
       for (String s : some_less_nice_names){
           nameList.add(s);
       }
    }


    //*throws IllegalStateException if game is over
    @Test(expected = IllegalStateException.class)
    public void kissExceptionTestZero(){
        ArrayList<String> nameListCopy = new ArrayList<String>(nameList);
        AssassinManager manager = new AssassinManager(nameListCopy);
        for (String s : nameListCopy){
            manager.kill(s);
        }
    }
    //*throws IllegalArgumentException if the name is not part of the kissring
    @Test(expected = IllegalArgumentException.class)
    public void kissExceptionTestOne(){
        ArrayList<String> nameListCopy = new ArrayList<String>(nameList);
        AssassinManager manager = new AssassinManager(nameListCopy);
        manager.kill("ryjqewoitjqwoitjoewijt");
    }
    //*throws IllegalState if game is over and name is not in kissring
    @Test(expected = IllegalStateException.class)
    public void kissExceptionTestThree(){
        ArrayList<String> nameListCopy = new ArrayList<String>(nameList);
        AssassinManager manager = new AssassinManager(nameListCopy);
        for (String s : nameListCopy){
            if (!s.equals("oneONEoneONE")){
                manager.kill(s);
            }
        }
        manager.kill("ewjrfwejiojewiofjw");
    }

    //constructor test:
    //*must not modify list *(send names that suck, make sure they aren't reformatted, in the printout)
//*affection list must have names in same order (looked at printkissring output to confirm this, not test case)
    
    @Test
    public void constructorTest(){
        ArrayList<String> name_list_copy1 = new ArrayList<String>(nameList);
        ArrayList<String> name_list_copy2 = new ArrayList<String>(nameList);
        
        AssassinManager manager = new AssassinManager(name_list_copy1);
        for (int i = 0; i < name_list_copy2.size(); i++){
            assertEquals(name_list_copy1.get(i), name_list_copy2.get(i));
        }
        //probably shouldn't put these here until post testing, but they shouldn't
        //be crashing if you are testing
        manager.kill("erica kane");
        manager.winner();
        manager.isGameOver();
        manager.killRingContains("tad martin");
        manager.killRingContains("erica kane");
        manager.graveyardContains("tad martin");
        manager.graveyardContains("erica kane");
        manager.printKillRing();
        manager.printGraveyard();
        
        //should still be the same even after every method has been called once.
        for (int i = 0; i < name_list_copy2.size(); i++){
            assertEquals(name_list_copy1.get(i), name_list_copy2.get(i));
        }
    }
    
    //*throws IllegalArgumentException if list is null
    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTestNull(){
    	AssassinManager manager = new AssassinManager(null);
    }
    //or size zero
    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTestZero(){
        ArrayList<String> empty_list = new ArrayList<String>();
        AssassinManager manager = new AssassinManager(empty_list);
    }}