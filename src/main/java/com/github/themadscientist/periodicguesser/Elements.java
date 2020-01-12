package com.github.themadscientist.periodicguesser;

import java.io.IOException;

import java.nio.file.FileSystems;
import java.nio.file.Files;

import java.util.List;
import java.util.HashMap;

public class Elements {
    
    public HashMap<Integer, String> elementMap;

    public void genElementMap() throws IOException {
        List<String> lines;
        lines = Files.readAllLines(FileSystems.getDefault().getPath(".", "periodic-table.csv"));

        elementMap = new HashMap<Integer, String>();

        for(String line : lines) {
            String[] sections = line.split(",");
            elementMap.put(Integer.valueOf(sections[0]), sections[2]);
        }
    }

    public void testPrint() {
        for(int key : elementMap.keySet()) {
            System.out.println(String.format("%d, %s", key, elementMap.get(key)));
        }
    }

}
