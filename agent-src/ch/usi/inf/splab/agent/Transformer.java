package ch.usi.inf.splab.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;


public final class Transformer implements ClassFileTransformer {
	
	@Override
	public byte[] transform(final ClassLoader loader,
			final String className,
			final Class<?> classBeingRedefined,
			final ProtectionDomain protectionDomain,
			final byte[] classfileBuffer)
					throws IllegalClassFormatException {

		if( className.startsWith("java/") ||
			className.startsWith("sun/") ||
			className.startsWith("ch/usi/inf/splab/agent/") ){
			return classfileBuffer;
		} else {
			try {
				System.out.println("About to transform class <"+loader+", "+className+">");
				return instrument(classfileBuffer);
			} catch (Throwable t) {
				t.printStackTrace();
				return classfileBuffer;
			}
		}
	}

	private byte[] instrument(byte[] bytes) {
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, ClassReader.SKIP_FRAMES);

		instrument(cn);
		final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cn.accept(cw);
		return cw.toByteArray();
	}

	@SuppressWarnings("unchecked")
	private void instrument(ClassNode cn) {
		for (MethodNode mn : (List<MethodNode>)cn.methods) {
			instrument(mn);
		}
	}
	
	private void instrument(MethodNode mn) {
		for(int i=0; i < mn.instructions.size(); i++){
			AbstractInsnNode ain = mn.instructions.get(i);
			switch(ain.getType()){
			case AbstractInsnNode.INT_INSN:
				if (ain.getOpcode()==Opcodes.NEWARRAY){
					/*IntInsnNode iin = (IntInsnNode)ain;
					InsnList patch = new InsnList();
					patch.add(new InsnNode(Opcodes.DUP));
					patch.add(new LdcInsnNode(types[iin.operand]));
					patch.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
							"ch/usi/inf/sp/dbi/profiler/Profiler",
							"logNewArray", "(ILjava/lang/String;)V", false));
					i+=patch.size();
					mn.instructions.insertBefore(ain, patch);*/
				}
				break;				
			}
		}
	}
	
}