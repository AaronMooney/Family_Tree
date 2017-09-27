/*
 * Aaron Mooney 20072163
 */
package models;

import java.util.ArrayList;

public class Person implements Comparable<Person> {
	private String name;
	private String gender;
	private int dob;
	private String mother;
	private String father;
	private ArrayList<Person> children = new ArrayList<Person>();
	private int familyCode;
	public ArrayList<Person> auntsAndUncles = new ArrayList<Person>();
	public ArrayList<Person> cousins = new ArrayList<Person>();

	public Person() {

	}

	public Person(String name) {
		this.name = name;
	}

	public Person(String name, String gender, int dob) {
		this.name = name;
		this.gender = gender;
		this.dob = dob;
	}

	public Person(String name, String gender, int dob, String mother, String father) {
		this.name = name;
		this.gender = gender;
		this.dob = dob;
		this.mother = mother;
		this.father = father;
		children = new ArrayList<Person>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setgender(String gender) {
		this.gender = gender;
	}

	public String getgender() {
		if (gender == "M") {
			return "Male";
		} else
			return "Female";
	}

	public void setDob(int dob) {
		this.dob = dob;
	}

	public int getDob() {
		return dob;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getMother() {
		return mother;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getFather() {
		return father;
	}

	public void setChildren(Person person) {
		children.add(person);
	}

	public ArrayList<Person> getChildren() {
		return children;
	}

	public String toString() {
		return "Name: " + name + " gender: " + gender + " DOB: " + dob + " Mother: " + mother + " Father: " + father;
	}

	public String toDataString() {
		return name + " " + gender + " " + dob + " " + mother + " " + father;
	}

	public void setFamilyCode(int i) {
		this.familyCode = i;
	}

	public int getFamilyCode() {
		return familyCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public int compareTo(Person that){
		return this.getDob() - that.getDob();
	}
}
