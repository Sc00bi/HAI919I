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

// Les réponses à la question 3 sont présentes dans ce fichier
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
		// Q3.1 : on lance la création de notre modèle
		Model uml = generateModel();

		sauverModeleUML(String.format("model/%s.uml", uml.getName()), uml);
	}

	// Q3.1 : méthode de génération d'un modèle uml, avec les types primitif int,
	// String et float
	public static Model generateModel() {
		Model res = factory.createModel();
		res.setName("generatedModel");

		res.getOwnedTypes().add((Type) uml_int);
		res.getOwnedTypes().add((Type) uml_string);
		res.getOwnedTypes().add((Type) uml_float);
		return res;
	}

	// Q 3.2 : créé une propriété UML, de même nom que field et de même type
	public static Property createUMLProperty(Model uml, Field field) {
		String fieldName = field.getName();

		// Génération de notre Property
		Property property = factory.createProperty();
		property.setName(fieldName);
		property.setVisibility(VisibilityKind.PRIVATE_LITERAL);
		String fieldType = field.getType().getName();

		// On prend le même type que field
		for (Type type : uml.getOwnedTypes()) {
			if (type.getName() == fieldName) {
				property.setType(type);
			}
		}

		return property;
	}

	// Q3.3 : retourne une classe UML, créé à partir d'une Entity passée en
	// paramètre
	public static org.eclipse.uml2.uml.Class createEntity(Model uml, Entity entity) {
		String entityName = entity.getName();

		// Génération de notre classe
		org.eclipse.uml2.uml.Class classe = factory.createClass();
		classe.setName(entityName);

		// Gestion des champs de l'entité
		for (Field field : entity.getFields()) {
			Property property = createUMLProperty(uml, field);
			classe.allAttributes().add(property);
		}

		return classe;
	}

	// Q3.4 : retourne une opération publique de l'uml
	public static Operation createOperation(Model uml, FieldDependantQuery fieldDQ) {

		// Concaténation du type de requete et du champ sur le quel il s'éxerce
		String fieldName = fieldDQ.getField().getName();
		String queryName = fieldDQ.getType().getName() + fieldName;

		// Génération de notre opération
		Operation operation = factory.createOperation();
		// Création du paramètre
		org.eclipse.uml2.uml.Parameter parameter = factory.createParameter();
		parameter.setName(fieldDQ.getField().getName());
		// On prend le même type que field pour notre paramètre
		for (Type type : uml.getOwnedTypes()) {
			if (type.getName() == fieldName) {
				parameter.setType(type);
			}
		}
		operation.getOwnedParameters().add(parameter);
		// Ajout du commentaire à l'opération
		Comment comment = factory.createComment();
		comment.setBody("@Entity");
		operation.getOwnedComments().add(comment);

		return operation;
	}

	// Q3.5 : retourne une interface UML à partir d'un Repository
	public static Interface createInterface(Model uml, Repository repository) {
		// Concaténation pour le nom de l'interface
		String interfaceName = repository.getTypeEntity().getName() + "Repository";

		// Génération de l'interface
		Interface interfaceUML = factory.createInterface();
		interfaceUML.setName(interfaceName);
		// Gestion des requêtes de l'entité
		for (Query query : repository.getQueries()) {
			Operation operation = createOperation(uml, (FieldDependantQuery) query); // Nous ne traitons pas les
																						// BasicsQuery, sinon une
																						// vérification est nécessaire
			interfaceUML.getAllOperations().add(operation);
		}
		// Ajout du commentaire à l'interface
		Comment comment = factory.createComment();
		comment.setBody("@Entity");
		interfaceUML.getOwnedComments().add(comment);

		return null;
	}

	// Q3.6 :

	/* Méthodes préalablement fournies */
	// Méthode de sauvegarde modèle
	public static void sauverModeleUML(String uri, EObject root) {
		Resource resource = null;
		try {
			URI uriUri = URI.createURI(uri);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", new XMIResourceFactoryImpl());
			resource = (new ResourceSetImpl()).createResource(uriUri);
			resource.getContents().add(root);
			resource.save(null);
		} catch (Exception e) {
			System.err.println("ERREUR sauvegarde du modèle : " + e);
			e.printStackTrace();
		}
	}

	// Méthode de chargement de modèle existant
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
			System.err.println("ERREUR chargement du modèle : " + e);
			e.printStackTrace();
		}
		return (PaAModel) resource.getContents().get(0);
	}

}