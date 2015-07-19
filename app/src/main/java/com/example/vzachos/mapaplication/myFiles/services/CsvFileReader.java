package com.example.vzachos.mapaplication.myFiles.services;

import com.example.vzachos.mapaplication.myFiles.classes.NottinTrafficLights;
import com.example.vzachos.mapaplication.myFiles.utils.GlobalUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by v.zachos on 7/6/2015.
 */
public class CsvFileReader {
    private static final CsvFileReader CSV_FILE_READER = new CsvFileReader();

    public static final String CSV_FILE_NAME = "nottinghamTrafficLights.csv";



/*	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		List<NottinTrafficLights> toInit = new ArrayList<NottinTrafficLights>();
		boolean flag = CsvFileReader.getInstance().loadCsv(toInit);
		if(flag)
			System.out.println(toInit.size());
		System.out.println(((double)System.currentTimeMillis()-start)/1000+" Seconds");
	}
	*/


    private CsvFileReader()
    {}

    public static CsvFileReader getInstance()
    {
        return CSV_FILE_READER;
    }


    public boolean loadCsv(List<NottinTrafficLights> toInit,InputStream file)
    {
        try
        {
            Scanner input= openFile(file);
            toInit.addAll(readRecords(input));
            closeFile(input);
            return GlobalUtils.TRUE;

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return GlobalUtils.FALSE;
    }

    private static Scanner openFile(InputStream file) throws Exception
    {

        Scanner input;
        input = new Scanner(file);
        input.useDelimiter(GlobalUtils.PATTERN);
        return input;
    }



    private List<NottinTrafficLights> readRecords(Scanner input) throws Exception
    {
        List<NottinTrafficLights>  toSend = new ArrayList<NottinTrafficLights>();

        while ( input.hasNext() )
        {
            NottinTrafficLights record = new NottinTrafficLights();
            record.setScn_no(input.next());
            record.setName(input.next());
            record.setType(input.next());
            record.setX(input.nextInt());
            record.setY(input.nextInt());
            record.setLat(input.nextFloat());
            record.setLng(input.nextFloat());
            record.setBody(input.next());
            record.setBody_name(input.next());
            record.setCreate_dat(input.next());
//        	 toSend.add(new NottinTrafficLights(input.next(), input.next(), input.next(), input.nextInt(),  input.nextInt(),  input.nextFloat(), input.nextFloat(), input.next(), input.next(), input.next()));
            //checkIfAlreadyExists(record, toSend);
            toSend.add(record);

//        	 printOut(record);
        }

        return toSend;
    }

    private static void checkIfAlreadyExists(NottinTrafficLights nottinTrafficLights,List<NottinTrafficLights>  list)
    {

        for(NottinTrafficLights model : list)
        {
            if(model.getScn_no().equals(nottinTrafficLights.getScn_no()))
            {
                //System.out.println("found same : "+nottinTrafficLights.getScn_no());
                return;
            }
        }
    }


    private static void printOut(NottinTrafficLights nottinTrafficLights)
    {
        System.out.println(nottinTrafficLights.getScn_no() + " - " +nottinTrafficLights.getName()+" - "+nottinTrafficLights.getType()+" - " +nottinTrafficLights.getX()+" - "+nottinTrafficLights.getLat());
    }

    private static void closeFile(Scanner input) throws Exception
    {
        if ( input != null )
            input.close();
    }


}
