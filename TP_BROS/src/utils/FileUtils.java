package utils;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;

import paa.Entity;
import paa.Field;
import paa.FieldBasicType;
import paa.FieldDependantQuery;
import paa.PaAModel;
import paa.PaaPackage;
import paa.Query;
import paa.Repository;

// Les r�ponses � la question 3 sont pr�sentes dans ce fichier
public class FileUtils {
	private static UMLFactory factory = UMLFactory.eINSTANCE;
	private static PrimitiveType uml_int = factory.createPrimitiveType();
	private static PrimitiveType uml_string = factory.createPrimitiveType();
	private static PrimitiveType uml_float = factory.createPrimitiveType();

	static {
		uml_int.setName("int");
		uml_string.setName("string");
		uml_float.setName("float");
	}

	public static void main(String[] args) {
		// Q3.1 : on lance la cr�ation de notre mod�le
		Model uml = generateModel();

		sauverModeleUML(String.format("model/%s.uml", uml.getName()), uml);
	}

	// Q3.1 : m�thode de g�n�ration d'un mod�le uml, avec les types primitif int,
	// String et float
	public static Model generateModel() {
		Model res = factory.createModel();
		res.setName("generatedModel");

		res.getOwnedTypes().add((Type) uml_int);
		res.getOwnedTypes().add((Type) uml_string);
		res.getOwnedTypes().add((Type) uml_float);
		return res;
	}

	// Q 3.2 : cr�� une propri�t� UML, de m�me nom que field et de m�me type
	public static Property createUMLProperty(Model uml, Field field) {
		String fieldName = field.getName();

		// G�n�ration de notre Property
		Property property = factory.createProperty();
		property.setName(fieldName);
		property.setVisibility(VisibilityKind.PRIVATE_LITERAL);
		String fieldType = field.getType().getName();

		// On prend le m�me type que field
		for (Type type : uml.getOwnedTypes()) {
			if (type.getName() == fieldName) {
				property.setType(type);
			}
		}

		return property;
	}

	// Q3.3 : retourne une classe UML, cr�� � partir d'une Entity pass�e en
	// param�tre
	public static org.eclipse.uml2.uml.Class createEntity(Model uml, Entity entity) {
		String entityName = entity.getName();

		// G�n�ration de notre classe
		org.eclipse.uml2.uml.Class classe = factory.createClass();
		classe.setName(entityName);

		// Gestion des champs de l'entit�
		for (Field field : entity.getFields()) {
			Property property = createUMLProperty(uml, field);
			classe.allAttributes().add(property);
		}

		return classe;
	}

	// Q3.4 : retourne une op�ration publique de l'uml
	public static Operation createOperation(Model uml, FieldDependantQuery fieldDQ) {

		// Concat�nation du type de requete et du champ sur le quel il s'�xerce
		String fieldName = fieldDQ.getField().getName();
		String queryName = fieldDQ.getType().getName() + fieldName;

		// G�n�ration de notre op�ration
		Operation operation = factory.createOperation();
		// Cr�ation du param�tre
		org.eclipse.uml2.uml.Parameter parameter = factory.createParameter();
		parameter.setName(fieldDQ.getField().getName());
		// On prend le m�me type que field pour notre param�tre
		for (Type type : uml.getOwnedTypes()) {
			if (type.getName() == fieldName) {
				parameter.setType(type);
			}
		}
		operation.getOwnedParameters().add(parameter);
		// Ajout du commentaire � l'op�ration
		Comment comment = factory.createComment();
		comment.setBody("@Entity");
		operation.getOwnedComments().add(comment);

		return operation;
	}

	// Q3.5 : retourne une interface UML � partir d'un Repository
	public static Interface createInterface(Model uml, Repository repository) {
		// Concat�nation pour le nom de l'interface
		String interfaceName = repository.getTypeEntity().getName() + "Repository";

		// G�n�ration de l'interface
		Interface interfaceUML = factory.createInterface();
		interfaceUML.setName(interfaceName);
		// Gestion des requ�tes de l'entit�
		for (Query query : repository.getQueries()) {
			Operation operation = createOperation(uml, (FieldDependantQuery) query); // Nous ne traitons pas les
																						// BasicsQuery, sinon une
																						// v�rification est n�cessaire
			interfaceUML.getAllOperations().add(operation);
		}
		// Ajout du commentaire � l'interface
		Comment comment = factory.createComment();
		comment.setBody("@Entity");
		interfaceUML.getOwnedComments().add(comment);

		return null;
	}

	// Q3.6 :

	/* M�thodes pr�alablement fournies */
	// M�thode de sauvegarde mod�le
	public static void sauverModeleUML(String uri, EObject root) {
		Resource resource = null;
		try {
			URI uriUri = URI.createURI(uri);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", new XMIResourceFactoryImpl());
			resource = (new ResourceSetImpl()).createResource(uriUri);
			resource.getContents().add(root);
			resource.save(null);
		} catch (Exception e) {
			System.err.println("ERREUR sauvegarde du mod�le : " + e);
			e.printStackTrace();
		}
	}

	// M�thode de chargement de mod�le existant
	public static PaAModel chargerModelePaA(String uri) {
		Resource resource = null;
		try {
			URI uriUri = URI.createURI(uri);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
			resource = (new ResourceSetImpl()).createResource(uriUri);
			XMLResource.XMLMap xmlMap = new XMLMapImpl();
			xmlMap.setNoNamespacePackage(PaaPackage.eINSTANCE);
			java.util.Map options = new java.util.HashMap();
			options.put(XMLResource.OPTION_XML_MAP, xmlMap);
			resource.load(options);
		} catch (Exception e) {
			System.err.println("ERREUR chargement du mod�le : " + e);
			e.printStackTrace();
		}
		return (PaAModel) resource.getContents().get(0);
	}

}