package com.razman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Main
{

    public static void main(String[] args) throws IOException {

        String st;
        ArrayList<Person> allPersonList = new ArrayList<>();
        HashMap<String,  String> topPersonList = new HashMap<>();
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
        for (int i = 0; i < allPersonList.size(); i++) {
            Person person = allPersonList.get(i);
            boolean topParent = true;
            for (int j = 0; j < allPersonList.size(); j++) {
                Person list2 = allPersonList.get(j);
                if (person.getParentID().equals(list2.getRecordID())) {
                    if (parentGroupMap.containsKey(person.getParentID())) {
                        ArrayList<Person> temp = parentGroupMap.get(person.getParentID());
                        boolean samePerson = false;
                        for (int k = 0; k < temp.size(); k++) {
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

            ArrayList<Person> temp = new ArrayList<Person>();
            if (topParent) {
                if (!parentGroupMap.containsKey(person.getParentID())) {
                    parentGroupMap.put(person.getParentID(), temp);
                topPersonList.put(person.getRecordID(), person.getName());
                }
            }
        }




        HashMap<String, ArrayList<Person>> nameMap = new HashMap<>();
        parentGroupMap.entrySet().forEach(entry -> {
            ArrayList<Person> pGroup = entry.getValue();
            HashSet<String> pName = new HashSet<>();
            for (int i=0; i< pGroup.size(); i++) {
                pName.add(pGroup.get(i).getRecordID());
            }
            AtomicBoolean check = new AtomicBoolean(false);
            AtomicReference<String> pID = new AtomicReference<>(entry.getKey());
            nameMap.entrySet().forEach(nm -> {

                ArrayList<Person> nmGroup = nm.getValue();
                HashSet<String> nmName = new HashSet<>();
                for (int i=0; i< nmGroup.size(); i++) {
                    nmName.add(nmGroup.get(i).getRecordID());
                }

                if (nmName.equals(pName)) {
                    check.set(true);
                    pID.set(nm.getKey());
                }
            });
            if (check.get() == true) {
                nameMap.put(entry.getKey() + "," + pID, entry.getValue());
                nameMap.remove(pID.get());
            } else {
                nameMap.put(entry.getKey(), entry.getValue());
            }
        });

        nameMap.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + "---");
            for (int i=0; i<entry.getValue().size(); i++) {
                entry.getValue().get(i).println();
            }
        });


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
                    printChildName(entry.getValue().get(i).getRecordID(), nameMap, textSpace + "    ", allPersonList);
                }
            }
        });
        System.out.println("-------End Exercise 1-----------");

        System.out.println("-------Start Exercise 2-----------");
        ArrayList<String> exist = new ArrayList<>();
        topPersonList.entrySet().forEach(entry -> {
            String textSpace = "";


            String partnerName = findPartner(entry.getKey(), entry.getValue(), nameMap, allPersonList);
            System.out.print(entry.getValue());
            if (!partnerName.equals("")) {
                System.out.println(", " + partnerName);
            } else{

                System.out.println("");
            }
            printChildName(entry.getKey(), nameMap, textSpace + "    ", allPersonList);
        });

        System.out.println("-------End Exercise 2-----------");

    }

    public static void printChildName(String id, HashMap<String, ArrayList<Person>> nameMap, String texSpace, ArrayList<Person> allPersonList) {

        nameMap.entrySet().forEach(entry -> {
            String pID[] = entry.getKey().split(",");
            for (String token: pID) {
                if (token.equals(id)) {
                    ArrayList<Person> p = entry.getValue();
                    for (int i=0; i<p.size(); i++) {
                        System.out.print(texSpace + p.get(i).getName());
                        String partnerName = findPartner(p.get(i).getRecordID(), p.get(i).getName(), nameMap, allPersonList);
                        if (!partnerName.equals("")) {
                            System.out.println(", " + partnerName);
                        } else{
                            System.out.println("");
                        }
                        printChildName(entry.getValue().get(i).getRecordID(), nameMap, texSpace + "    ", allPersonList);
                    }
                }
            }

        });
    }

    public static String findPartner(String id, String name, HashMap<String, ArrayList<Person>> nameMap, ArrayList<Person> list) {

        AtomicReference<String> partnerID = new AtomicReference<>("");
        String partnerName = "";
        nameMap.entrySet().forEach(entry -> {
            AtomicBoolean check = new AtomicBoolean(false);
            String words[] = entry.getKey().split(",");
            for (String token: words) {
                if (token.equals(id)) {
                    check.set(true);
                }
            }
            if (check.get() == true) {
                for (String token: words) {

                    if (!token.equals(id)) {
                        partnerID.set(token);
                    }
                }
            }
        });

        String temp = partnerID.get();
        for (int i=0; i<list.size(); i++) {
            if (temp.equals(list.get(i).getRecordID()) && !name.equals(list.get(i).getName())) {
                partnerName = list.get(i).getName();
            }
        }
        return partnerName;
    }

}
