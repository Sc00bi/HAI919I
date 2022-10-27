package utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;

public class BuildUML {
	public static Package createPackage(Model model, String packageName) {
		Package newPackage = model.createNestedPackage(packageName);
		newPackage.setName(packageName);
		return newPackage;
	}
	
	public static Class createClassFromPackage(Package pkg, String className, boolean isAbstract) {
		Class newClass =  pkg.createOwnedClass(className, isAbstract);
		newClass.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		return newClass;
	}
	
	public static Interface createInterfaceFromPackage(Package pkg, String interfaceName) {
		Interface newInterface =  pkg.createOwnedInterface(interfaceName);
		newInterface.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		return newInterface;
	}
		
	public static Class createClassFromModel(Model model, String className, boolean isAbstract) {
		Class newClass =  model.createOwnedClass(className, isAbstract);
		newClass.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		return newClass;
	}
		
	public static Property createAttribute(Class c, VisibilityKind visibility, Type attrType, String attrName) {
		Property newProperty = c.createOwnedAttribute(attrName, attrType);
		newProperty.setVisibility(visibility);
		return newProperty;
	}
	
	public static Operation createMethod(Class c, VisibilityKind visibility, Type returnType, String methodName, EList<String> argsName, EList<Type> argsType, boolean isStatic) {
		Operation newOperation = c.createOwnedOperation(methodName, argsName, argsType, returnType);
		newOperation.setVisibility(visibility);
		newOperation.setIsStatic(isStatic);
		return newOperation;
	}
	
	public static void makeInheritance(Class child, Class parent) {
		Generalization generalization = UMLFactory.eINSTANCE.createGeneralization();
	    generalization.setSpecific(child);
	    generalization.setGeneral(parent);
	}
	
}
