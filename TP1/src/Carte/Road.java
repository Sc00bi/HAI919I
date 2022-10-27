/**
 */
package Carte;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Road</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link Carte.Road#getName <em>Name</em>}</li>
 *   <li>{@link Carte.Road#getLength <em>Length</em>}</li>
 *   <li>{@link Carte.Road#getMeet <em>Meet</em>}</li>
 *   <li>{@link Carte.Road#getBorder <em>Border</em>}</li>
 * </ul>
 *
 * @see Carte.CartePackage#getRoad()
 * @model abstract="true"
 * @generated
 */
public interface Road extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see Carte.CartePackage#getRoad_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link Carte.Road#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see #setLength(int)
	 * @see Carte.CartePackage#getRoad_Length()
	 * @model
	 * @generated
	 */
	int getLength();

	/**
	 * Sets the value of the '{@link Carte.Road#getLength <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(int value);

	/**
	 * Returns the value of the '<em><b>Meet</b></em>' reference list.
	 * The list contents are of type {@link Carte.Road}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Meet</em>' reference list.
	 * @see Carte.CartePackage#getRoad_Meet()
	 * @model
	 * @generated
	 */
	EList<Road> getMeet();

	/**
	 * Returns the value of the '<em><b>Border</b></em>' reference list.
	 * The list contents are of type {@link Carte.PublicSpace}.
	 * It is bidirectional and its opposite is '{@link Carte.PublicSpace#getBorderedBy <em>Bordered By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Border</em>' reference list.
	 * @see Carte.CartePackage#getRoad_Border()
	 * @see Carte.PublicSpace#getBorderedBy
	 * @model opposite="borderedBy"
	 * @generated
	 */
	EList<PublicSpace> getBorder();

} // Road
