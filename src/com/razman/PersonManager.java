package com.razman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PersonManager {

    public static void groupPersonByParentID(ArrayList<Person> allPersonList, HashMap<String, ArrayList<Person>> parentGroupMap, HashMap<String,  String> topPersonList) {

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
    }

    public static void combineParentIDForSameChild(HashMap<String, ArrayList<Person>> parentGroupMap, HashMap<String, ArrayList<Person>> nameMap) {

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
    }

    public static void printChildName(String id, HashMap<String, ArrayList<Person>> nameMap, String texSpace, ArrayList<Person> allPersonList, ArrayList<String> existedList) {

        nameMap.entrySet().forEach(entry -> {
            String pID[] = entry.getKey().split(",");
            for (String token: pID) {
                if (token.equals(id)) {
                    ArrayList<Person> p = entry.getValue();
                    for (int i=0; i<p.size(); i++) {
                        System.out.print(texSpace + p.get(i).getName());
                        findPartner(p.get(i).getRecordID(), p.get(i).getName(), nameMap, allPersonList, existedList);
                        printChildName(entry.getValue().get(i).getRecordID(), nameMap, texSpace + "    ", allPersonList, existedList);
                    }
                }
            }
        });
    }

    public static void findPartner(String id, String name, HashMap<String, ArrayList<Person>> nameMap, ArrayList<Person> list, ArrayList<String> existedList) {

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
        boolean existed = false;
        for (int i=0; i<existedList.size(); i++) {
            if (existedList.get(i).equals(existed)) {
                existed = true;
            }
        }
        if (!existed) {
            for (int i=0; i<list.size(); i++) {
                if (temp.equals(list.get(i).getRecordID()) && !name.equals(list.get(i).getName())) {
                    partnerName = list.get(i).getName();
                }
            }
        }
        if (!partnerName.equals("")) {
            System.out.println(", " + partnerName);
            existedList.add(temp);
        } else {
            System.out.println("");
        }
    }

    public static boolean checkExistingPartner(ArrayList<String> finalExistedList, String id) {
        boolean existed = false;
        for (int j = 0; j< finalExistedList.size(); j++) {
            if (finalExistedList.get(j).equals(id)){
                existed = true;
            }
        }
        return existed;
    }
}
