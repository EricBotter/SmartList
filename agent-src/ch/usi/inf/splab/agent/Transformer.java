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
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;


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
			className.startsWith("ch/usi/inf/splab/agent/") ||
			className.startsWith("ch/usi/inf/splab/smartlist/SmartList") ||
			className.startsWith("org/eclipse/core/runtime/adaptor") ){
			return classfileBuffer;
		} else {
			try {
				//System.out.println("About to transform class <"+loader+", "+className+">");
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
		cr.accept(cn, 0);

		instrument(cn);
		
		final ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		return cw.toByteArray();
	}

	@SuppressWarnings("unchecked")
	private void instrument(ClassNode cn) {
		for (MethodNode mn : (List<MethodNode>)cn.methods) {
			instrument(mn);
		}
	}
	
	private void instrument( MethodNode mn ){
		for( int i=0; i < mn.instructions.size(); i++ ){
			AbstractInsnNode ain = mn.instructions.get(i);
			switch(ain.getType()){
			case AbstractInsnNode.TYPE_INSN:
				if( ain.getOpcode() == Opcodes.NEW ){
					TypeInsnNode tin = (TypeInsnNode)ain;
					if( tin.desc.equals( "java/util/ArrayList" ) ){
						InsnList patch = new InsnList();
						patch.add( new TypeInsnNode( Opcodes.NEW, "ch/usi/inf/splab/smartlist/SmartList" ) );
						mn.instructions.insertBefore( tin, patch );
						mn.instructions.remove( tin );
						//System.out.println( "\tSUB: NEW " + tin.desc );
					}
				}
				break;
				
			case AbstractInsnNode.METHOD_INSN:
				if( ain.getOpcode() == Opcodes.INVOKESPECIAL ){
					MethodInsnNode tin = (MethodInsnNode)ain;
					if( tin.owner.equals( "java/util/ArrayList" ) &&
							tin.name.equals( "<init>" ) ){
						InsnList patch = new InsnList();
						patch.add( new MethodInsnNode( Opcodes.INVOKESPECIAL, 
								"ch/usi/inf/splab/smartlist/SmartList", tin.name, tin.desc, false ) );
						mn.instructions.insertBefore( tin, patch );
						mn.instructions.remove( tin );
						System.out.println( "\tSUB: INVOKESPECIAL " + tin.owner + " " + tin.name + tin.desc );
					}
				}
				break;
			}
		}
	}
	
}