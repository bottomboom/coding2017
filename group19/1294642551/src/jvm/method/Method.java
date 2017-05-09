package jvm.method;

import jvm.clz.ClassFile;
import jvm.attr.AttributeInfo;
import jvm.attr.CodeAttr;
import jvm.constant.ConstantPool;
import jvm.constant.UTF8Info;
import jvm.loader.ByteCodeIterator;



public class Method {
	
	private int accessFlag;
	private int nameIndex;
	private int descriptorIndex;
	
	private CodeAttr codeAttr;
	
	private ClassFile clzFile;
	
	
	public ClassFile getClzFile() {
		return clzFile;
	}

	public int getNameIndex() {
		return nameIndex;
	}
	public int getDescriptorIndex() {
		return descriptorIndex;
	}
	
	public CodeAttr getCodeAttr() {
		return codeAttr;
	}

	public void setCodeAttr(CodeAttr code) {
		this.codeAttr = code;
	}
	
	

	public Method(ClassFile clzFile,int accessFlag, int nameIndex, int descriptorIndex) {
		this.clzFile = clzFile;
		this.accessFlag = accessFlag;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	
	
	
	
	public String toString() {
		
		ConstantPool pool = this.clzFile.getConstantPool();
		StringBuilder buffer = new StringBuilder();
		
		String name = ((UTF8Info)pool.getConstantInfo(this.nameIndex)).getValue();
		
		String desc = ((UTF8Info)pool.getConstantInfo(this.descriptorIndex)).getValue();
		
		buffer.append(name).append(":").append(desc).append("\n");
		
		buffer.append(this.codeAttr.toString(pool));
		
		return buffer.toString();
	}
	
	public static Method parse(ClassFile clzFile, ByteCodeIterator iter){
		int accessFlag = iter.nextU2ToInt();
		int nameIndex = iter.nextU2ToInt();
		int descIndex = iter.nextU2ToInt();
		int attributeCount = iter.nextU2ToInt();
		
		Method method = new Method(clzFile, accessFlag, nameIndex, descIndex);
		for(int i = 0; i < attributeCount; i++){
			int attrNameIndex = iter.nextU2ToInt();
			String attributeName = clzFile.getConstantPool().getUTF8String(attrNameIndex);
			if(AttributeInfo.CODE.equalsIgnoreCase(attributeName)){
				CodeAttr codeAttr = CodeAttr.parse(clzFile, iter);
				method.setCodeAttr(codeAttr);
			}else{
				throw new RuntimeException("Method 中的属性"+attributeName+"未定义");
			}
		}
		
		
		return method;
		
	}

}
