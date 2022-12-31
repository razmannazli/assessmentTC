package com.razman;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {

        String st;
        ArrayList<Person> allPersonList = new ArrayList<Person>();
        ArrayList<Person> topPersonList = new ArrayList<Person>();
        HashMap<String, ArrayList<Person>> parentGroupMap = new HashMap<>();

        File file = new File("C:\\Users\\Razman-NB\\Desktop\\inputtest.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        int count = 1;
        while ((st = br.readLine()) != null) {
            if (count > 1) {
                String words[] = st.split(",");
                Person p = new Person(words[0], words[1], words[2], words[3]);
                allPersonList.add(p);
            }
            count++;
        }

        // Grouping by parentID
        for (int i=0; i<allPersonList.size(); i++) {
            Person person = allPersonList.get(i);
            boolean topParent = true;
            for (int j=0; j<allPersonList.size(); j++) {
                Person list2 = allPersonList.get(j);
                if (person.getParentID().equals(list2.getRecordID())) {
                    if (parentGroupMap.containsKey(person.getParentID())) {
                        ArrayList<Person> temp = parentGroupMap.get(person.getParentID());
                        boolean samePerson = false;
                        for (int k=0;k<temp.size();k++) {
                            if (person.getName().equals(temp.get(k).getName())) {
                                samePerson = true;
                            }
                        }
                        if (samePerson == false) {
                            temp.add(person);
                            parentGroupMap.put(person.getParentID(), temp);
                        }
                    } else {
                        ArrayList<Person> temp = new ArrayList<Person>();
                        temp.add(person);
                        parentGroupMap.put(person.getParentID(), temp);
                    }
                    topParent = false;
                }
            }
            if (topParent) {
                ArrayList<Person> temp;
                if (parentGroupMap.containsKey(person.getParentID())) {
                    temp = parentGroupMap.get(person.getParentID());
                } else {
                    temp = new ArrayList<Person>();
                }
                temp.add(person);
                parentGroupMap.put(person.getParentID(), temp);
                topPersonList.add(person);
            }
        }

        // Print parent and their child accordingly
        String textSpace = "   ";
        parentGroupMap.entrySet().forEach(entry -> {
            String name = "";
            for (int i=0; i<allPersonList.size(); i++) {
                if (entry.getKey().equals(allPersonList.get(i).getRecordID())) {
                    name = allPersonList.get(i).getName();
                }
            }
            if (!name.equals("")) {
                System.out.println(name);
                printChild(entry.getKey(), parentGroupMap, textSpace);
            }
        });
    }

    public static void printChild(String recordID, HashMap<String, ArrayList<Person>> map, String textSpace) {

        map.entrySet().forEach(entry -> {
            if (entry.getKey().equals(recordID)) {
                ArrayList<Person> list = entry.getValue();
                for (int i=0; i<list.size(); i++) {
                    System.out.println(textSpace + list.get(i).getName());
                    printChild(list.get(i).getRecordID(), map, textSpace+"   ");
                }
            }
        });
    }
}



