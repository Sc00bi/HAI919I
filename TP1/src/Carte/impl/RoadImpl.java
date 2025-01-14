/**
 */
package Carte.impl;

import Carte.CartePackage;
import Carte.PublicSpace;
import Carte.Road;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Road</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link Carte.impl.RoadImpl#getName <em>Name</em>}</li>
 *   <li>{@link Carte.impl.RoadImpl#getLength <em>Length</em>}</li>
 *   <li>{@link Carte.impl.RoadImpl#getMeet <em>Meet</em>}</li>
 *   <li>{@link Carte.impl.RoadImpl#getBorder <em>Border</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class RoadImpl extends MinimalEObjectImpl.Container implements Road {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLength()
	 * @generated
	 * @ordered
	 */
	protected static final int LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLength() <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLength()
	 * @generated
	 * @ordered
	 */
	protected int length = LENGTH_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMeet() <em>Meet</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeet()
	 * @generated
	 * @ordered
	 */
	protected EList<Road> meet;

	/**
	 * The cached value of the '{@link #getBorder() <em>Border</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBorder()
	 * @generated
	 * @ordered
	 */
	protected EList<PublicSpace> border;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RoadImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CartePackage.Literals.ROAD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CartePackage.ROAD__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLength() {
		return length;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLength(int newLength) {
		int oldLength = length;
		length = newLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CartePackage.ROAD__LENGTH, oldLength, length));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Road> getMeet() {
		if (meet == null) {
			meet = new EObjectResolvingEList<Road>(Road.class, this, CartePackage.ROAD__MEET);
		}
		return meet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PublicSpace> getBorder() {
		if (border == null) {
			border = new EObjectWithInverseResolvingEList.ManyInverse<PublicSpace>(PublicSpace.class, this, CartePackage.ROAD__BORDER, CartePackage.PUBLIC_SPACE__BORDERED_BY);
		}
		return border;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CartePackage.ROAD__BORDER:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getBorder()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CartePackage.ROAD__BORDER:
				return ((InternalEList<?>)getBorder()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CartePackage.ROAD__NAME:
				return getName();
			case CartePackage.ROAD__LENGTH:
				return getLength();
			case CartePackage.ROAD__MEET:
				return getMeet();
			case CartePackage.ROAD__BORDER:
				return getBorder();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CartePackage.ROAD__NAME:
				setName((String)newValue);
				return;
			case CartePackage.ROAD__LENGTH:
				setLength((Integer)newValue);
				return;
			case CartePackage.ROAD__MEET:
				getMeet().clear();
				getMeet().addAll((Collection<? extends Road>)newValue);
				return;
			case CartePackage.ROAD__BORDER:
				getBorder().clear();
				getBorder().addAll((Collection<? extends PublicSpace>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CartePackage.ROAD__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CartePackage.ROAD__LENGTH:
				setLength(LENGTH_EDEFAULT);
				return;
			case CartePackage.ROAD__MEET:
				getMeet().clear();
				return;
			case CartePackage.ROAD__BORDER:
				getBorder().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CartePackage.ROAD__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CartePackage.ROAD__LENGTH:
				return length != LENGTH_EDEFAULT;
			case CartePackage.ROAD__MEET:
				return meet != null && !meet.isEmpty();
			case CartePackage.ROAD__BORDER:
				return border != null && !border.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", length: ");
		result.append(length);
		result.append(')');
		return result.toString();
	}

} //RoadImpl
