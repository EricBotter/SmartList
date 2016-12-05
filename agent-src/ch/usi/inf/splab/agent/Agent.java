package ch.usi.inf.splab.agent;

import java.lang.instrument.Instrumentation;

import ch.usi.inf.splab.agent.Transformer.Options;

public class Agent {
	
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Agent starting (arguments: '"+agentArgs+"')");
		if( "SmallList".equals(agentArgs) )
			inst.addTransformer( new Transformer( Options.INJECT_SMALL_LIST ) );
		else if( "DumperList".equals(agentArgs) )
			inst.addTransformer( new Transformer( Options.INJECT_DUMPER_LIST ) );
		else if( "DoNothing".equals(agentArgs) )
			inst.addTransformer( new Transformer( Options.DO_NOTHING ) );
		else
			throw new IllegalArgumentException( "Bad agent argument. \n\tUsage: "
					+ "-javaagent:agent.jar=(DoNothing | DumperList | SmallList)" );	
	}
	
}