package utilisationMaps;

import Carte.Map;
import Carte.Pedestrian;
import Carte.Road;
import Carte.Street;
import Carte.impl.CarteFactoryImpl;
import Carte.CartePackage;
import Carte.Garden;

import java.util.ArrayList;

import org.eclipse.emf.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;

public class ManipulationsMaps {
	private static Map maMap;

	public static void main(String[] args) {

		// Je charge l'instance map.xmi du méta-modèle maps.ecore
		Resource resource = chargerModele("model/Map.xmi", CartePackage.eINSTANCE); // ligne à adapter au nom de votre
																					// modèle
		if (resource == null)
			System.err.println(" Erreur de chargement du modèle");
		// Instruction récupérant le modèle sous forme d'arbre à partir de la classe
		// racine "Map"
		maMap = (Map) resource.getContents().get(0);
		System.out.println(maMap.getName()); // affichage du nom de la carte.

		// Affichage du nom des rues
		System.out.println("Le nom des rues de cette carte sont : ");
		for (String s : getStreetsNames(maMap)) {
			System.out.println("Rue " + s);
		}

		// Affichage du nom des rues piétonnes de moins de 1000 m
		System.out.println("Le nom des rues piétonnes de moins de 100 m de cette carte sont : ");
		for (String s : getShortPedestriansNames(maMap)) {
			System.out.println("Rue " + s);
		}

		// Affichage du nom des rues adjacentes
		System.out.println("Le nom des rues adjacentes à la rue des liserons sont : ");
		for (Road r : getAdjacentRoads(maMap, "Liserons")) {
			System.out.println("Rue " + r.getName());
		}

		// Création d'un nouveau jardin
		System.out.println("On ajoute un nouveau jardin");
		addGarden(maMap, "Pinède", getAdjacentRoads(maMap, "Liserons"));
	}

	public static Resource chargerModele(String uri, EPackage pack) {
		Resource resource = null;
		try {
			URI uriUri = URI.createURI(uri);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
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
		return resource;
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

	// Obtention de toutes les rues (Streets) de notre carte
	public static ArrayList<String> getStreetsNames(Map map) {
		ArrayList<String> streetsList = new ArrayList<String>();
		for (Road road : map.getRoads()) {
			if (road instanceof Street) {
				streetsList.add(road.getName());
			}
		}
		return streetsList;
	}

	// Obtention de toutes les rues piétonnes (Pedestrian) de notre carte dont la
	// longueur est inférieure à 20 m
	public static ArrayList<String> getShortPedestriansNames(Map map) {
		ArrayList<String> streetsList = new ArrayList<String>();
		for (Road road : map.getRoads()) {
			if (road instanceof Pedestrian && (((Pedestrian) road).getLength() < 1000)) {
				streetsList.add(road.getName());
			}
		}
		return streetsList;
	}

	// Obtention de toutes les rues adjacentes à une certaines rue dans notre carte
	public static ArrayList<Road> getAdjacentRoads(Map map, String roadName) {
		ArrayList<Road> adjacentes = new ArrayList<>();
		for (Road road : map.getRoads()) {
			if (road.getName().equals(roadName)) {
				for (Road adj : road.getMeet()) {
					adjacentes.add(adj);
				}
				break;
			}
		}
		return adjacentes;
	}

	// Création d'un nouveau jardin
	public static void addGarden(Map map, String gardenName, ArrayList<Road> adj) {
		CarteFactoryImpl mapsFactory = new CarteFactoryImpl();
		Garden newGarden = mapsFactory.createGarden();
		newGarden.setName(gardenName);
		newGarden.getBorderedBy().addAll(adj);

		map.getSpaces().add(newGarden);
		sauverModele("model/Map.xmi", map);
	}

}