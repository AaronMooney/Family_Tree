/*
 * Aaron Mooney 20072163
 */
package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import models.Person;

public class FamilyTreeAPI {

	private HashMap<String, Person> db;
	private int count = 0;
	List<String> copy = new ArrayList<String>();
	int familyCount = 0;
	HashMap<String, Integer> family;

	public FamilyTreeAPI() {
		db = new HashMap<String, Person>();
	}
	/*
	 * Reads a file and creates the Person object for each line
	 */
	public void readFile() throws FileNotFoundException {
		File data = new File("../FamilyTree/data/data.txt");
		Scanner read = new Scanner(data);
		String delim = "\\s";
		while (read.hasNextLine()) {
			String details = read.nextLine();
			String[] tokens = details.split(delim);
			Person person = new Person(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
			if (tokens[3] != "?")
				person.setMother(tokens[3]);
			if (tokens[4] != "?")
				person.setFather(tokens[4]);
			db.put((tokens[0]), person);
		}
		System.out.println(db);
		read.close();
		addChildren();
	}

	/*
	 * Add children to each person (if any)
	 */
	private void addChildren() {
		for (String key : db.keySet()) {
			Person person = db.get(key);
			Person mother = db.get(person.getMother());
			Person father = db.get(person.getFather());
			if (father != null)
				father.setChildren(person);
			if (mother != null)
				mother.setChildren(person);
		}
	}

	public Person getPerson(String name) {
		if (!db.containsKey(name)) {
			System.out.println("no such person");
			return null;
		}
		return db.get(name);
	}

	public ArrayList<Person> getChildren(Person p) {
		return p.getChildren();
	}

	public Person getFather(Person p) {
		return db.get(p.getFather());
	}

	public Person getMother(Person p) {
		return db.get(p.getMother());
	}

	public void getCousins(Person p) {
		ArrayList<Person> auntsAndUncles = new ArrayList<Person>();
		ArrayList<Person> cousins = new ArrayList<Person>();
		if (p != null) {
			auntsAndUncles = p.auntsAndUncles;
			cousins = p.cousins;
			ArrayList<Person> dadSide = getSiblings(getFather(p));
			ArrayList<Person> mamSide = getSiblings(getMother(p));
			auntsAndUncles.addAll(mamSide);
			auntsAndUncles.addAll(dadSide);
			for (Person per : auntsAndUncles) {
				for (Person person : per.getChildren()) {
					cousins.add(person);
				}

			}
			System.out.println("Cousins of " + p.getName() + " : " + cousins.toString());
		}
	}

	public ArrayList<Person> getParents(Person p) {
		ArrayList<Person> parents = new ArrayList<Person>();
		parents.add(getMother(p));
		parents.add(getFather(p));
		return parents;
	}

	public ArrayList<Person> getGrandParents(Person p) {
		ArrayList<Person> gp = new ArrayList<Person>();
		Person grandMother = db.get(db.get(p.getMother()).getMother());
		Person grandFather = db.get(db.get(p.getFather()).getFather());
		if (grandMother != null)
			gp.add(grandMother);
		if (grandFather != null)
			gp.add(grandFather);
		System.out.println(gp.toString());
		return gp;
	}

	public ArrayList<Person> getGrandChildren(Person p) {
		ArrayList<Person> gc = new ArrayList<Person>();
		for (Person per : p.getChildren()) {
			if (per.getChildren() != null) {
				for (Person child : per.getChildren()) {
					gc.add(child);
				}
			}
		}
		System.out.println(gc.toString());
		return gc;
	}

	/*
	 * method to show the siblings of the person specified, does not cater for
	 * step siblings.
	 */
	public ArrayList<Person> getSiblings(Person p) {
		ArrayList<Person> siblings = new ArrayList<Person>();
		ArrayList<Person> children = new ArrayList<Person>();
		if (p.getFather() != "?" && p.getMother() != "?") {
			Person father = db.get(p.getFather());
			if (father != null)
				children = father.getChildren();
			for (Person person : children) {
				if (!person.equals(p)) {
					System.out.println(p.getName() + " and " + person.getName() + " are siblings.");
					siblings.add(person);
				}
			}
		}
		return siblings;
	}

	/*
	 * Add a new person to the database
	 */
	private void addPerson(String name, String gender, int dob, String mother, String father) {
		String pName = name;
		if (db.containsKey(name)) {
			count++;
			System.out.println("That name already exists, so when referring to this person use '" + name + count
					+ "' to reference them");
			pName = name + count;
		}
		Person p = new Person(pName, gender, dob);
		if (mother != "?")
			p.setMother(mother);
		if (father != "?")
			p.setFather(father);
		db.put(pName, p);
		System.out.println("Added " + p.getName());

		addChildren();
		String data = "\n" + p.toDataString();
		write(data);
	}

	/*
	 * Collect data from user to create a new person
	 */
	public void addPerson() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the following details");
		System.out.println("First Name:");
		String name = in.nextLine();
		System.out.println("Gender(M/F):");
		String gender = in.nextLine();
		System.out.print("Birth Year:");
		int dob = Integer.parseInt(in.nextLine());
		System.out.println("Mother's First Name:");
		String mother = in.nextLine();
		System.out.println("Father's First Name:");
		String father = in.nextLine();
		addPerson(name, gender, dob, mother, father);
	}

	/*
	 * Method to iterate over everyone and if someone has not been seen before
	 * add them to a family
	 */
	public void calculateFamilies() {

		familyCount = 0;
		family = new HashMap<String, Integer>();

		for (String name : db.keySet()) {
			if (!family.containsKey(name)) {
				setFamily(name, familyCount);
				familyCount++;
			}
		}
	}

	/*
	 * Method to recursively  add a person, their parents and their children to a family
	 */
	private void setFamily(String name, int index) {
		if (name == null || name.equals("?") || family.containsKey(name))
			return;

		family.put(name, index);
		Person me = db.get(name);
		if (me == null)
			return;
		me.setFamilyCode(index);
		setFamily(me.getFather(), index);
		setFamily(me.getMother(), index);

		for (Person c : db.get(name).getChildren())
			setFamily(c.getName(), index);
	}

	/*
	 * test if two people are related
	 */
	public boolean isRelated(String a, String b) {
		System.out.println(family.get(a) == family.get(b));
		return (family.get(a) == family.get(b));
	}

	/*
	 * gets relatives for a particular person
	 */
	public ArrayList<Person> getRelatives(Person p) {
		ArrayList<Person> relatives = new ArrayList<Person>();
		for (String key : db.keySet()) {
			Person person = db.get(key);
			if ((person.getFamilyCode() == p.getFamilyCode()) && !person.equals(p)) {
				relatives.add(person);
			}
		}
		System.out.println(relatives.toString());
		return relatives;
	}

	/*
	 * Method to get all families in the model
	 */
	private HashMap<Integer, ArrayList<String>> getAllFamilies() {
		HashMap<Integer, ArrayList<String>> map = new HashMap<>();

		for (String s : family.keySet()) {
			if (!map.containsKey(family.get(s))) {
				map.put(family.get(s), new ArrayList<>());
			}
			ArrayList<String> keys = map.get(family.get(s));
			keys.add(s);
			map.put(family.get(s), keys);
		}
		return map;
	}

	/*
	 * prints out all families
	 */
	public void printFamilies() {
		System.out.println(getAllFamilies());
	}

	/*
	 * Get oldest person in a family
	 */
	public Person getOldest(Person p) {
		ArrayList<Person> relatives = getRelatives(p);
		relatives.add(p);
		Collections.sort(relatives);
		System.out.println(relatives.get(0));
		return relatives.get(0);
	}

	/*
	 * Prints out the largest family and how many people are in it
	 */
	public void printLargestFamily() {
		HashMap<Integer, ArrayList<String>> map = getAllFamilies();
		ArrayList<Person> aFamily = new ArrayList<Person>();
		int count = 0;
		int familyCode = 0;
		for (ArrayList<String> families : map.values()) {
			if (count < families.size()) {
				count = families.size();
				familyCode = family.get(families.get(0));
			}
		}
		for (String s : map.get(familyCode)) {
			aFamily.add(db.get(s));
		}
		System.out.println(aFamily.toString());
		System.out.println("There are " + count + " people in this family");
	}

	public void numberOfFamilies() {
		System.out.println("There are " + familyCount + " families in the database");
	}

	public void numberOfPeople(){
		System.out.println("There are " + db.size() + " people in the database");
	}
	
	/*
	 * Write new data to the file
	 */
	private void write(String data) {
		try {
			String filename = "../FamilyTree/data/data.txt";
			FileWriter writer = new FileWriter(filename, true);
			writer.write(data);
			writer.close();
			System.out.println("File updated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
