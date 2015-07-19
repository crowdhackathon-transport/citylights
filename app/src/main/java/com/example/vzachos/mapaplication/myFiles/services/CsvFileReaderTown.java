package com.example.vzachos.mapaplication.myFiles.services;

import com.example.vzachos.mapaplication.myFiles.classes.ArvadaTrafficLights;
import com.example.vzachos.mapaplication.myFiles.utils.GlobalUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by v.zachos on 7/6/2015.
 */
public class CsvFileReaderTown {

    private static final CsvFileReaderTown CSV_FILE_READER_TOWN = new CsvFileReaderTown();

    public static final String CSV_FILE_NAME = "Athens.csv";

    public static final String[] CSV_FILE_NAMES = new String[]
            {
                    "Athens.csv"
            };

    //paizoun:
        //Norfolk Norwich .csv
        //Oakland.csv
        //Hong-Kong.csv
        //Seattle.csv
        //Surrey.csv
        //Toronto-BeaconPedestrian.csv
        //Glasgow.csv

    //tiponoun ena fanari mono to teleutaio
        //"Manchester.csv",
        //"Arvada.csv"

    /*public static final String[] CSV_FILE_NAME = new String[]
            {"Arvada.csv", "Hansestadt-Rostock.csv", "Hong-Kong.csv",
                    "Manchester.csv", "Norfolk-Norwich.csv",
                    "Oakland.csv", "Seattle.csv", "Surrey.csv" };
*/


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


    private CsvFileReaderTown()
    {}

    public static CsvFileReaderTown getInstance()
    {
        return CSV_FILE_READER_TOWN;
    }


    public boolean loadCsv(List<ArvadaTrafficLights> toInit,InputStream file)
    {
        boolean flag= GlobalUtils.FALSE;
        Scanner input=null;
        try
        {
            input= openFile(file);
            toInit.addAll(readRecords(input));
            closeFile(input);
            flag= GlobalUtils.TRUE;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return flag;
    }

    private Scanner openFile(InputStream file) throws Exception
    {
        Scanner input;
        input = new Scanner(file);
        input.useDelimiter(GlobalUtils.PATTERN);
        return input;
    }


    private List<ArvadaTrafficLights> readRecords(Scanner input) throws Exception
    {
        List<ArvadaTrafficLights> toSend = new ArrayList<ArvadaTrafficLights>();

        System.out.println("Input ========================================= "+ input);

        while ( input.hasNext() )
        {
            try {
                /*if(input.next() == "\n")
                {
                    input.next().replace("\n",",");
                }*/
                ArvadaTrafficLights record = new ArvadaTrafficLights();
                record.setType(input.next());
                record.setId(input.next());
                record.setLocation(input.next());
                record.setComments(input.next());
                record.setLat(input.nextDouble());
                record.setLng(input.nextDouble());

                //String latt = input.next();
                /*String longg = input.next();
                record.setLng(convertToDouble(longg));*/
                //toSend.add(new ArvadaTrafficLights(input.next(), input.next(), input.next(), input.nextInt(),  input.nextInt(),  input.nextFloat(), input.nextFloat(), input.next(), input.next(), input.next()));
                //checkIfAlreadyExists(record, toSend);
                String s = input.toString();
                s.replace("\n",",\n");
                System.out.println("A record from csv file :"+ record.getLat() + " " + record.getLng());
                toSend.add(record);
            }
            catch (Exception e)
            {

            }

            //printOut(record);
        }

        return toSend;
    }

    private static double convertToDouble(String string) throws Exception
    {
        try{
            return Double.valueOf(string.replace("\"",""));
        }
        catch (Exception e)
        {
            throw e;
        }
    }


    private static void checkIfAlreadyExists(ArvadaTrafficLights arvadaTrafficLights,List<ArvadaTrafficLights>  list)
    {

        for(ArvadaTrafficLights model : list)
        {
            if(model.getId().equals(arvadaTrafficLights.getId()))
            {
                //System.out.println("found same : "+arvadaTrafficLights.getId());
                return;
            }
        }
    }


    private static void printOut(ArvadaTrafficLights arvadaTrafficLights)
    {
        System.out.println(arvadaTrafficLights.getId() + " - " +arvadaTrafficLights.getType()+" - " +arvadaTrafficLights.getLat()+" - "+arvadaTrafficLights.getLng());
    }

    private void closeFile(Scanner input)
    {
        try{
            if ( input != null )
                input.close();
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }

    }
}
