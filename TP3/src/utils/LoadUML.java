package utils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;

public class LoadUML {
	Model model;
	
	public LoadUML(String uri) {
		chargerModele(uri);
	}
	
	public void sauverModele(String uri, EObject root) {
		   Resource resource = null;
		   try {
		      URI uriUri = URI.createURI(uri);
		      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
	      resource = (new ResourceSetImpl()).createResource(uriUri);
	      resource.getContents().add(root);
	      resource.save(null);
	   } catch (Exception e) {
	      System.err.println("ERREUR sauvegarde du mod�le : "+e);
	      e.printStackTrace();
	   }
	}

	public Model chargerModele(String uri) {
	   Resource resource = null;
	   EPackage pack=UMLPackage.eINSTANCE;
	   try {
	      URI uriUri = URI.createURI(uri);
	      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", new XMIResourceFactoryImpl());
	      resource = (new ResourceSetImpl()).createResource(uriUri);
	      XMLResource.XMLMap xmlMap = new XMLMapImpl();
	      xmlMap.setNoNamespacePackage(pack);
	      java.util.Map options = new java.util.HashMap();
	      options.put(XMLResource.OPTION_XML_MAP, xmlMap);
	      resource.load(options);
	   }
	   catch(Exception e) {
	      System.err.println("ERREUR chargement du mod�le : "+e);
	      e.printStackTrace();
	   }
	   return (Model) resource.getContents().get(0);
	}
	
	public void setModelName(String newModelName) {
		System.out.println(model.getName()+".uml renomm� : "+newModelName+".uml");
		model.setName(newModelName);
	}
}
