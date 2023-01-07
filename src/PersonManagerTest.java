import com.razman.Person;
import com.razman.PersonManager;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PersonManagerTest {
    String st;
    ArrayList<Person> allPersonList = new ArrayList<>();
    HashMap<String, ArrayList<Person>> parentGroupMap = new HashMap<>();
    HashMap<String,  String> topPersonList = new HashMap<>();
    HashMap<String, ArrayList<Person>> nameMap = new HashMap<>();
    File file = new File("inputtest.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));

    PersonManagerTest() throws FileNotFoundException {
    }

    @Test
    void groupPersonByParentID() throws IOException {
        int count = 1;
        while ((st = br.readLine()) != null) {
            if (count > 1) {
                String words[] = st.split(",");
                if (words.length == 4 && words[0] != "" && words[1] != "" && words[2] != "" && words[3] != "") {
                    Person p = new Person(words[0], words[1], words[2], words[3]);
                    allPersonList.add(p);
                }
            }
            count++;
        }
        PersonManager.groupPersonByParentID(allPersonList, parentGroupMap, topPersonList);
        assertTrue(parentGroupMap.containsKey("10"));
        assertTrue(parentGroupMap.containsKey("20"));
        assertTrue(parentGroupMap.containsKey("30"));
        assertFalse(parentGroupMap.containsKey("40"));
    }

    @Test
    void combineParentIDForSameChild() throws IOException {
        int count = 1;
        while ((st = br.readLine()) != null) {
            if (count > 1) {
                String words[] = st.split(",");
                if (words.length == 4 && words[0] != "" && words[1] != "" && words[2] != "" && words[3] != "") {
                    Person p = new Person(words[0], words[1], words[2], words[3]);
                    allPersonList.add(p);
                }
            }
            count++;
        }
        PersonManager.groupPersonByParentID(allPersonList, parentGroupMap, topPersonList);
        PersonManager.combineParentIDForSameChild(parentGroupMap, nameMap);
        assertTrue(nameMap.containsKey("10,20"));
        assertTrue(nameMap.containsKey("105,100"));
    }
}