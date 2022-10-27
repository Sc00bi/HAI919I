package utils;

import javax.lang.model.element.PackageElement;

import org.eclipse.emf.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
//import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.impl.UMLFactoryImpl;
import org.eclipse.uml2.uml.Package;

public class LoadUML {

	public static void main(String[] args) {

		// Instruction récupérant le modèle sous forme d'arbre à partir de la classe
		// racine "Model"
		Model umlP = chargerModele("model/model.uml");

		String nomModele = umlP.getName();

		System.out.println("Ton modèle se nomme: \"" + nomModele + "\"");

		// Modifier le nom du modèle UML
		umlP.setName("TomModel");
		System.out.println("\"" + nomModele + "\" Changé ! Le modèle se nomme: \"" + umlP.getName() + "\"");

		// Changer le package d'une classe
		switchPackage(umlP, "A", "p1", "p2");

		// Créer un attribut dans une classe
		addAttribute(umlP, "A", "attribute");

		// Changer la visibilité de notre attribut
		switchVisibility(umlP, "A", "attribute");
		
		// Ajout d'une super classe à la classe A
		//createSuperClass(umlP, "A", "SuperA");

		// Sauvegarder le modèle avec son nouveau nom
		sauverModele("model/TomModel.uml", umlP);

	}

	public static void sauverModele(String uri, EObject root) {
		Resource resource = null;
		try {
			URI uriUri = URI.createURI(uri);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
			resource = (new ResourceSetImpl()).createResource(uriUri);
			resource.getContents().add(root);
			resource.save(null);
		} catch (Exception e) {
			System.err.println("ERREUR sauvegarde du modèle : " + e);
			e.printStackTrace();
		}
	}

	public static Model chargerModele(String uri) {
		Resource resource = null;
		EPackage pack = UMLPackage.eINSTANCE;
		try {
			URI uriUri = URI.createURI(uri);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", new XMIResourceFactoryImpl());
			resource = (new ResourceSetImpl()).createResource(uriUri);
			XMLResource.XMLMap xmlMap = new XMLMapImpl();
			xmlMap.setNoNamespacePackage(pack);
			java.util.Map options = new java.util.HashMap();
			options.put(XMLResource.OPTION_XML_MAP, xmlMap);
			resource.load(options);
		} catch (Exception e) {
			System.err.println("ERREUR chargement du modèle : " + e);
			e.printStackTrace();
		}
		return (Model) resource.getContents().get(0);
	}

	public static Class findClassInPackage(String name, Package p) {
		Class c = null;
		for (PackageableElement pd : p.getPackagedElements()) {
			if (pd instanceof Class) {
				Class pdc = (Class) pd;
				if (pdc.getName().equals(name)) {
					return pdc;
				}
			} else if (pd instanceof Package) {
				Package pdp = (Package) pd;
				c = findClassInPackage(name, pdp);
				if (c != null) {
					return c;
				}
			}
		}
		return c;
	}

	public static Package findPackageInPackage(String name, Package p) {
		Package result = null;
		for (PackageableElement pd : p.getPackagedElements()) {
			if (pd instanceof Package) {
				Package pdp = (Package) pd;
				if (pdp.getName().equals(name)) {
					return pdp;
				}
				result = findPackageInPackage(name, pdp);
				if (result != null) {
					return result;
				}
			}
		}
		return result;
	}

	// méthode permettant de déplacer notre classe de nom "className" du package
	// source au package cible
	public static void switchPackage(Model model, String className, String packageSourceName,
			String packageTargetName) {
		Class classToMove = findClassInPackage(className, model);
		Package newPackageHost = findPackageInPackage(packageSourceName, model);
		Package oldPackageHost = findPackageInPackage(packageTargetName, model);
		newPackageHost.getPackagedElements().add(classToMove);
		oldPackageHost.getPackagedElements().remove(classToMove);
	}

	// Ajoute un attribut privé à notre modèle
	public static void addAttribute(Model model, String className, String attributeName) {
		Package p1 = findPackageInPackage("p1", model);
		Class classToModify = findClassInPackage(className, p1);
		classToModify.createOwnedAttribute(attributeName, classToModify);
	}

	// Modifie la visibilité d'un attribut de public à privé et met à disposition un
	// accesseur et mutateur
	public static void switchVisibility(Model model, String className, String attributeName) {
		Class classToModify = findClassInPackage(className, model);
		for (Property attribute : classToModify.getAllAttributes())
		{
			if( attribute.getName().equals(attributeName))
			{
				attribute.setVisibility(VisibilityKind.PRIVATE_LITERAL);
				Operation opGetter = UMLFactory.eINSTANCE.createOperation();
				opGetter.setVisibility(VisibilityKind.PUBLIC_LITERAL);
				opGetter.setType(classToModify);
				opGetter.setName("get" + attributeName);
				Operation opSetter = UMLFactory.eINSTANCE.createOperation();
				opSetter.parameterableElements();
				opSetter.createOwnedParameter(attributeName, classToModify);
				opSetter.setVisibility(VisibilityKind.PUBLIC_LITERAL);
				opSetter.setName("set" + attributeName);
			}
		}
	}

	// Créé une nouvelle super classe à la classe A de nom superClassName
	public static void createSuperClass(Model model, String className, String superClassName)
	{
		Generalization generalization = UMLFactory.eINSTANCE.createGeneralization();
		
		Package p1 = findPackageInPackage("p1", model);
		Class childClass = findClassInPackage(className, p1);
		
		//Class parentClass = UMLFactory.eINSTANCE.createClass();
		/*
		p1.createOwnedClass(superClassName, false);
		parentClass.setName(superClassName);
		p1.getPackagedElements().add(parentClass);
		
		generalization.setSpecific(childClass);
		generalization.setGeneral(parentClass);*/
	}
	
	

}
