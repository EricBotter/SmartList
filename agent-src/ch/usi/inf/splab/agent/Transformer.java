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
	
	public static enum Options{
		DO_NOTHING,
		INJECT_SMART_LIST,
		INJECT_SMALL_LIST
	}
	
	private String listClassName;
	private Options opt;
	
	public Transformer( Options opt ){
		this.opt = opt;
		switch( opt ){
		case DO_NOTHING:
			listClassName = null;
			break;
			
		case INJECT_SMALL_LIST:
			listClassName = "ch/usi/inf/splab/smartlist/SmallList";
			break;
			
		case INJECT_SMART_LIST:
			listClassName = "ch/usi/inf/splab/smartlist/SmartList";
			break;
		}
	}
	
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
			className.startsWith("ch/usi/inf/splab/smartlist/SmallList") ||
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
			instrument(mn, cn.name);
		}
	}
	
	private void instrument( MethodNode mn, String className ){
		boolean newFound = false;
		
		for( int i=0; i < mn.instructions.size(); i++ ){
			AbstractInsnNode ain = mn.instructions.get(i);
			switch(ain.getType()){
			case AbstractInsnNode.TYPE_INSN:
				if( ain.getOpcode() == Opcodes.NEW ){
					TypeInsnNode tin = (TypeInsnNode)ain;
					if( tin.desc.equals( "java/util/ArrayList" ) ){
						InsnList patch = new InsnList();
						patch.add( new TypeInsnNode( Opcodes.NEW, listClassName ) );
						if( opt != Options.DO_NOTHING ){
							mn.instructions.insertBefore( tin, patch );
							mn.instructions.remove( tin );
						}
						//System.out.println( "\tSUB: NEW " + tin.desc );
						newFound = true;
					}
				}
				break;
				
			case AbstractInsnNode.METHOD_INSN:
				if( ain.getOpcode() == Opcodes.INVOKESPECIAL ){
					MethodInsnNode tin = (MethodInsnNode)ain;
					if( tin.owner.equals( "java/util/ArrayList" ) &&
							tin.name.equals( "<init>" ) && newFound ){
						InsnList patch = new InsnList();
						patch.add( new MethodInsnNode( Opcodes.INVOKESPECIAL, 
								listClassName, tin.name, tin.desc, false ) );
						if( opt != Options.DO_NOTHING ){
							mn.instructions.insertBefore( tin, patch );
							mn.instructions.remove( tin );
						}
						//System.out.println( "\tSUB: INVOKESPECIAL " + tin.owner + " " + tin.name + tin.desc );
						//System.out.println( "\t\t" + className + ": "+  mn.name );
						newFound = false;
					}
				}
				break;
			}
		}
	}
	
}