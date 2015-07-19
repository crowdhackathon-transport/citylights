package com.example.vzachos.mapaplication.myFiles.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by v.zachos on 7/9/2015.
 */



//This class is not working yet!!!!
public class GenericCsvReader {


    private static List<List<String>> readTXTFile(String csvFileName) throws IOException {

        String line = null;
        BufferedReader stream = null;
        List<List<String>> csvData = new ArrayList<List<String>>();

        try {
            stream = new BufferedReader(new FileReader(csvFileName));
            while ((line = stream.readLine()) != null) {
                String[] splitted = line.split(",");
                List<String> dataLine = new ArrayList<String>(splitted.length);
                for (String data : splitted)
                    dataLine.add(data);
                csvData.add(dataLine);
            }
        } finally {
            if (stream != null)
                stream.close();
        }

        return csvData;

    }
}
