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
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;


public final class Transformer implements ClassFileTransformer {
	
	public static enum Options{
		DO_NOTHING,
		INJECT_DUMPER_LIST,
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
			
		case INJECT_DUMPER_LIST:
			listClassName = "ch/usi/inf/splab/smartlist/DumperList";
			break;
			
		case INJECT_SMALL_LIST:
			listClassName = "ch/usi/inf/splab/smartlist/SmallList";
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
			className.startsWith("ch/usi/inf/splab/smartlist/DumperList") ||
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

		if( opt == Options.INJECT_DUMPER_LIST )
			instrumentForDumper(cn);
		else
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
	
	@SuppressWarnings("unchecked")
	private void instrumentForDumper(ClassNode cn) {
		for (MethodNode mn : (List<MethodNode>)cn.methods) {
			instrumentForDumper(mn, cn.name);
		}
	}
	
	private void instrumentForDumper( MethodNode mn, String className ){
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
						mn.instructions.insertBefore( tin, patch );
						mn.instructions.remove( tin );
						newFound = true;
					}
				}
				break;
				
			case AbstractInsnNode.METHOD_INSN:
				if( ain.getOpcode() == Opcodes.INVOKESPECIAL ){
					MethodInsnNode min = (MethodInsnNode)ain;
					if( min.owner.equals( "java/util/ArrayList" ) && min.name.equals( "<init>" ) && newFound ){
						String allocationSiteId = className + "$" + mn.name + "$" + mn.instructions.indexOf(min);
						String constructorType = appendStringArgument(min.desc);
						InsnList patch = new InsnList();
						patch.add( new LdcInsnNode(allocationSiteId) );
						patch.add( new MethodInsnNode( Opcodes.INVOKESPECIAL, listClassName, min.name, constructorType, false ) );
						//System.out.println( "\tSUB: INVOKESPECIAL " + className + "$" + mn.name + "$" + mn.instructions.indexOf(min) );
						mn.instructions.insertBefore( min, patch );
						mn.instructions.remove( min );
						i += 1;
						mn.maxStack += 1;
						newFound = false;
					}
				}
				break;
			}
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
					MethodInsnNode min = (MethodInsnNode)ain;
					if( min.owner.equals( "java/util/ArrayList" ) && min.name.equals( "<init>" ) && newFound ){
						InsnList patch = new InsnList();
						patch.add( new MethodInsnNode( Opcodes.INVOKESPECIAL, listClassName, min.name, min.desc, false ) );
						if( opt != Options.DO_NOTHING ){
							//System.out.println( "\tSUB: INVOKESPECIAL " + className + "$" + mn.name + "$" + mn.instructions.indexOf(min) );
							mn.instructions.insertBefore( min, patch );
							mn.instructions.remove( min );
						}
						newFound = false;
					}
				}
				break;
			}
		}
	}

	
	private String appendStringArgument( String typeDesc ) {
		int parenPos = typeDesc.length() - 2;
		return typeDesc.substring(0, parenPos) + "Ljava/lang/String;" + typeDesc.substring(parenPos);
	}
	
}