package com.razman;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Razman-NB\\Desktop\\inputtest.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<Person> personList = new ArrayList<Person>();
        HashMap<String, ArrayList<Person>> map = new HashMap<>();
        int count = 0;
        String st;
        while ((st = br.readLine()) != null) {
            if (count > 0) {
//                System.out.println(st);
                String words[] = st.split(",");
                Person temp = new Person(words[0],words[1],words[2],words[3]);
                personList.add(temp);

                if(map.containsKey(temp.getParentID())) {

                } else {
                    ArrayList<Person> newPersonList =new ArrayList<Person>();
                    newPersonList.add(temp);
                    map.put(temp.getParentID(),newPersonList);
                }
            }
            count++;
        }
        // verify result
        map.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());

            ArrayList<Person> temp = entry.getValue();
            for(int i=0;i<temp.size(); i++){
                temp.get(i).println();
            }
        });
        for(int i=0; i< personList.size();i++){
            Person temp = personList.get(i);
            temp.println();
        }
    }
}