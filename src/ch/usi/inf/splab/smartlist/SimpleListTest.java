package ch.usi.inf.splab.smartlist;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleListTest {
	
	public List<Integer> listUnderTest;
	public long executionTimeMs;
	public ArrayList<Integer> listUnderTestSizes;
	
	public SimpleListTest( List<Integer> listUnderTest ){
		this.listUnderTest = listUnderTest;
		executionTimeMs = 0;
		listUnderTestSizes = new ArrayList<>();
	}
	
	public void dumpSizes( String filename ) throws IOException{
		PrintWriter pw = new PrintWriter( new FileWriter(filename) );
		for( int i = 0; i < listUnderTestSizes.size(); i++ ){
			pw.println( listUnderTestSizes.get(i) );
		}
		pw.close();
	}
	
	public void randomInsertGetRemove( int nOperations ){
		Random opSelector = new Random( 12345678 );
		Random indexSelector = new Random( 12345678 );
		
	    for( int i = 0 ; i < nOperations ; i++ ){
	    	double num = opSelector.nextDouble();
	    	if( num < (1.0/3.0) ){
	    		listUnderTest.add( 10 );
	    	}else if( listUnderTest.size() > 0 && num < (2.0/3.0) ){
	    		listUnderTest.get( indexSelector.nextInt( listUnderTest.size() ) );
	    	}else if( listUnderTest.size() > 0 ){
	    		listUnderTest.remove( indexSelector.nextInt( listUnderTest.size() ) );
	    	}
	    	listUnderTestSizes.add( i, listUnderTest.size() );
	    }
	}
	
	public void growingOnlyList( int nOperations ){
		Random opSelector = new Random( 12345678 );
		Random indexSelector = new Random( 12345678 );
		
	    for( int i = 0 ; i < nOperations ; i++ ){
	    	double num = opSelector.nextDouble();
	    	double addProbability = ((double)nOperations-(double)i)/(double)nOperations;
	    	if( num < addProbability ){
	    		listUnderTest.add( 10 );
	    	}else if( listUnderTest.size() > 0 ){
	    		listUnderTest.get( indexSelector.nextInt( listUnderTest.size() ) );
	    	}
	    	listUnderTestSizes.add( i, listUnderTest.size() );
	    }
	}
	
	public void executeTest01( int nOperations ){
		System.out.println( "  Starting test 01: randomInsertGetRemove for " + nOperations + " operations." );
		listUnderTest.clear();
		listUnderTestSizes.clear();
		listUnderTestSizes.ensureCapacity( nOperations );
		
		long startTime = System.nanoTime();
		randomInsertGetRemove( nOperations );
		long endTime = System.nanoTime();
		executionTimeMs = (endTime - startTime) / 1000000;
	}
	
	public void executeTest02( int nOperations ){
		System.out.println( "  Starting test 02: growingOnlyList for " + nOperations + " operations." );
		listUnderTest.clear();
		listUnderTestSizes.clear();
		listUnderTestSizes.ensureCapacity( nOperations );
		
		long startTime = System.nanoTime();
		growingOnlyList( nOperations );
		long endTime = System.nanoTime();
		executionTimeMs = (endTime - startTime) / 1000000;
	}
	

	public static void main( String[] args ) throws IOException{
		ArrayList<Integer> l = new ArrayList<Integer>();
		SimpleListTest test = new SimpleListTest( l );
		System.out.println( "Starting test..." );
		test.executeTest02( 10000000 );
		System.out.println( "...test finished in " + test.executionTimeMs + " milliseconds." );
		System.out.print( "Dumping list sizes..." );
		test.dumpSizes( "sizes.txt" );
		System.out.println( "done." );
	}
	
	/*
	 Terminal type set to 'wxt'
	gnuplot> set term wxt enhanced
	gnuplot> set title 'List size'
	gnuplot> plot [:] [:] '~/eclipsews/SmartList/sizes.txt' every 1000 title 'list size' with lines
	*/
}
