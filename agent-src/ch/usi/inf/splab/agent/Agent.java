package ch.usi.inf.splab.agent;

import java.lang.instrument.Instrumentation;

public class Agent {
	
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Agent starting (arguments: '"+agentArgs+"')");
		inst.addTransformer(new Transformer());
	}
	
}