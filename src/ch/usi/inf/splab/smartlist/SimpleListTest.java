package ch.usi.inf.splab.smartlist;

import java.util.List;
import java.util.Random;

public class SimpleListTest {
	
	public List<Integer> listUnderTest;
	public long executionTimeMs;
	
	public SimpleListTest( List<Integer> listUnderTest ){
		this.listUnderTest = listUnderTest;
		executionTimeMs = 0;
	}
	
	public void randomInsertGetRemove( long nOperations ){
		Random opSelector = new Random( 12345678 );
		Random indexSelector = new Random( 12345678 );
		
	    for( int i = 0 ; i < nOperations ; i++ ){
	    	double num = opSelector.nextDouble();
	    	if( num < (1.0/3.0) ){
	    		listUnderTest.add( 10 );
	    	}else if( num < (2.0/3.0) ){
	    		listUnderTest.get( indexSelector.nextInt( listUnderTest.size() ) );
	    	}else{
	    		listUnderTest.remove( indexSelector.nextInt( listUnderTest.size() ) );
	    	}
	    }
	}
	
	public void executeTest01( long nOperations ){
		System.out.println( "  Starting test 01: randomInsertGetRemove for " + nOperations + " operations." );
		listUnderTest.clear();
		
		long startTime = System.nanoTime();
		randomInsertGetRemove( nOperations );
		long endTime = System.nanoTime();
		executionTimeMs = (endTime - startTime) / 1000000;
	}

	public static void main( String[] args ){
		SmartList<Integer> l = new SmartList<Integer>();
		SimpleListTest test = new SimpleListTest( l );
		System.out.println( "Starting test..." );
		test.executeTest01( 20000000 );
		System.out.println( "...test finished in " + test.executionTimeMs + " milliseconds." );
	}
}
