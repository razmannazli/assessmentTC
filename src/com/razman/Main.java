package com.razman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main
{
    public static void main(String[] args) throws IOException {

        String st;
        ArrayList<Person> allPersonList = new ArrayList<>();
        HashMap<String, ArrayList<Person>> parentGroupMap = new HashMap<>();
        HashMap<String,  String> topPersonList = new HashMap<>();
        HashMap<String, ArrayList<Person>> nameMap = new HashMap<>();
        ArrayList<String> existedList = new ArrayList<>();
        ArrayList<String> sortedTopPerson = new ArrayList<>();

        File file = new File("inputtest.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

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

        System.out.println("-------Start Exercise 1-----------");
        nameMap.entrySet().forEach(entry -> {
            if (entry.getValue().size() > 0) {
                String textSpace = "";
                ArrayList<String> parentID = new ArrayList<>();
                String word[] = entry.getKey().split(",");
                for (String token: word) {
                    parentID.add(token);
                }
                Collections.sort(parentID);
                for (int i=0; i< parentID.size(); i++) {
                    String name = "";
                    for (int j=0; j< allPersonList.size(); j++) {
                        if (allPersonList.get(j).getRecordID().equals(parentID.get(i))) {
                            name = allPersonList.get(j).getName();
                        }
                    }
                    if (i!=0) {
                        System.out.print(", ");
                    }
                    System.out.print(name);
                }
                System.out.println("");
                textSpace = "    ";
                for (int i=0; i<entry.getValue().size(); i++) {
                    System.out.println(textSpace + entry.getValue().get(i).getName());
                    PersonManager.printChildName(entry.getValue().get(i).getRecordID(), nameMap, textSpace + "    ", allPersonList, existedList);
                }
            }
        });
        System.out.println("-------End Exercise 1-----------");

        System.out.println("-------Start Exercise 2-----------");
        ArrayList<String> finalExistedList = existedList;
        topPersonList.entrySet().forEach(entry -> {
            sortedTopPerson.add(entry.getKey());
        });
        Collections.sort(sortedTopPerson);
        for (int i=0; i<sortedTopPerson.size(); i++) {
            String key = sortedTopPerson.get(i);
            topPersonList.entrySet().forEach(entry -> {
                if (key.equals(entry.getKey())) {
                    boolean existed = PersonManager.checkExistingPartner(finalExistedList, entry.getKey());
                    if (!existed) {
                        String textSpace = "";
                        System.out.print(entry.getValue());
                        PersonManager.findPartner(entry.getKey(), entry.getValue(), nameMap, allPersonList, finalExistedList);
                        PersonManager.printChildName(entry.getKey(), nameMap, textSpace + "    ", allPersonList, finalExistedList);
                    }
                }
            });
        }
        System.out.println("-------End Exercise 2-----------");
    }
}
