package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;

public class StateManager {

	public static void main(String[] args) {
//		Instruction récupérant le modèle sous forme d'arbre à partir de la classe racine "Model"
		Model model = chargerModele("model/InputMove.uml");
		String nomModele = model.getName();
		System.out.println("Ton modèle se nomme: \"" + nomModele + "\"");

		Class cA = findClassInPackage("A", model);

//		applyStateToClass(cA);

		List<Behavior> stateMachines = getStateMachinesFromClass(cA);
		System.out.println("Nom des machines à états de l'application : ");
		for (Behavior stateMachine : stateMachines) {
			System.out.println("\t- " + stateMachine.getName());
			List<Vertex> states = getStateFromWellFormedStateMachine((StateMachine) stateMachine);
			System.out.println("\tEtats contenus dans la machine à états : ");
			for (Vertex state : states) {
				System.out.println("\t\t- " + state.getName());
			}

			List<Operation> operations = getOperationAsTriggerFromWellFormedStateMachine((StateMachine) stateMachine);
			System.out.println("\tOperations contenus dans la machine à état : ");
			for (Operation operation : operations) {
				System.out.println("\t\t- " + operation.getName());
			}

			applyStateToApplicationClasses("model/InputMove.uml");
		}

//		Modifier le nom du modèle UML.
		nomModele = model.getName();
		model.setName("Model");
		System.out.println("\"" + nomModele + "\" Changé ! Le modèle se nomme: \"" + model.getName() + "\"");

//		Sauvegarder le modèle avec son nouveau nom.
		sauverModele(String.format("model/%s.uml", model.getName()), model);

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

	public static Package createPackage(Model model, String packageName) {
		Package newPackage = model.createNestedPackage(packageName);
		newPackage.setName(packageName);
		return newPackage;
	}

	public static Class createClassFromPackage(Package pkg, String className, boolean isAbstract) {
		Class newClass = pkg.createOwnedClass(className, isAbstract);
		newClass.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		return newClass;
	}

	public static Interface createInterfaceFromPackage(Package pkg, String interfaceName) {
		Interface newInterface = pkg.createOwnedInterface(interfaceName);
		newInterface.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		return newInterface;
	}

	public static Class createClassFromModel(Model model, String className, boolean isAbstract) {
		Class newClass = model.createOwnedClass(className, isAbstract);
		newClass.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		return newClass;
	}

	public static Property createAttribute(Class c, VisibilityKind visibility, Type attrType, String attrName) {
		Property newProperty = c.createOwnedAttribute(attrName, attrType);
		newProperty.setVisibility(visibility);
		return newProperty;
	}

	public static Operation createMethod(Class c, VisibilityKind visibility, Type returnType, String methodName,
			EList<String> argsName, EList<Type> argsType, boolean isStatic) {
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

	public static List<Behavior> getStateMachinesFromClass(Class c) {
		return c.getOwnedBehaviors().stream().filter(x -> x instanceof StateMachine).collect(Collectors.toList());
	}

	public static boolean isWellFormedStateMachine(StateMachine sm) {
		return sm.getRegions().size() == 1;
	}

	public static List<Vertex> getStateFromWellFormedStateMachine(StateMachine sm) {
		assert (isWellFormedStateMachine(sm));
		return sm.getRegions().get(0).getSubvertices().stream().filter(x -> x instanceof State)
				.collect(Collectors.toList());
	}

	public static List<Operation> getOperationAsTriggerFromWellFormedStateMachine(StateMachine sm) {
		assert (isWellFormedStateMachine(sm));
		List<Operation> operations = new ArrayList<Operation>();
		List<Transition> transitions = sm.getRegions().get(0).getTransitions();
		for (Transition transition : transitions) {
			for (Trigger trigger : transition.getTriggers()) {
				if (trigger.getEvent() instanceof CallEvent) {
					operations.add(((CallEvent) trigger.getEvent()).getOperation());
				}
			}
		}
		return operations;
	}

	private static String setWordFirstCharCase(String word, char charCase) {
		char[] char_table = word.toCharArray();
		if (charCase == 'u') {
			char_table[0] = Character.toUpperCase(char_table[0]);
		} else if (charCase == 'l') {
			char_table[0] = Character.toLowerCase(char_table[0]);
		} else {
			System.err.println("La casse demandée n'est pas applicable.");
		}
		return new String(char_table);
	}

	public static void applyStateToClass(Class c) {
//		
		List<Class> concretClasses = new ArrayList<Class>();
		Class abstractC = createClassFromPackage(c.getPackage(), "Etat" + c.getName(), true);
		Property pC = createAttribute(c, VisibilityKind.PRIVATE_LITERAL, abstractC, "currentState");
		Property pAbstractC = createAttribute(abstractC, VisibilityKind.PRIVATE_LITERAL, c, "context");

		List<Behavior> stateMachines = getStateMachinesFromClass(c);

		EList<String> names = new BasicEList<String>();
		names.add(setWordFirstCharCase(abstractC.getName(), 'l'));
		EList<Type> types = new BasicEList<Type>();
		types.add(abstractC);
//		Méthode pour modifier le state courrant.
		createMethod(c, VisibilityKind.PUBLIC_LITERAL, null, "set" + setWordFirstCharCase(pC.getName(), 'u'), names,
				types, false);

//		
		if (stateMachines.size() > 0) {
			List<Operation> operations = getOperationAsTriggerFromWellFormedStateMachine(
					(StateMachine) stateMachines.get(0));
			for (Operation operation : operations) {
				Operation copyOperation = EcoreUtil.copy(operation);
				copyOperation.setIsAbstract(true);
				abstractC.getOwnedOperations().add(copyOperation);
			}

			for (Vertex state : getStateFromWellFormedStateMachine((StateMachine) stateMachines.get(0))) {
				Class concreteC = createClassFromPackage(c.getPackage(), state.getName(), false);

//				Création d'un constructeur pour chacune des classes concrète.
				names.clear();
				names.add(c.getName().toLowerCase());
				types.clear();
				types.add(c);
				Operation constructor = createMethod(concreteC, null, null, concreteC.getName(), names, types, false);

//				Généralisation
				makeInheritance(concreteC, abstractC);
				concretClasses.add(concreteC);
			}
		}
	}

	public static void applyStateToApplicationClassesBis(Element element) {
		if (element instanceof Package) {
			List<Element> elements = element.getOwnedElements();
			for (Element e : elements) {
				if (e instanceof Package) {
					applyStateToApplicationClassesBis(e);
				} else if (e instanceof Class) {
					applyStateToClass((Class) e);
				}
			}
		} else if (element instanceof Class) {
			applyStateToClass((Class) element);
		}
	}

	public static void applyStateToApplicationClasses(String umlFilePath) {
		Model model = chargerModele(umlFilePath);

		List<Element> elementsRoot = model.getOwnedElements();
		for (Element element : elementsRoot) {
			applyStateToApplicationClassesBis(element);
		}
		model.setName("new" + model.getName() + ".uml");
		sauverModele(model.getName(), model);
	}
}
