/*
 * Aaron Mooney 20072163
 */
package controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import asg.cliche.CLIException;
import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import controllers.FamilyTreeAPI;
import models.Person;
public class Main {

	public FamilyTreeAPI api;
	
	public Main(){
		api = new FamilyTreeAPI();
		try {
			api.readFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		api.calculateFamilies();
	}
	/*
	 * cli interface using cliche
	 */
	public static void main(String[] args) throws IOException{
		Main main = new Main();
		
		Shell shell = ShellFactory.createConsoleShell(">>", "Welcome to the Family Tree console, ?list or ?l for commands, ?help for instructions, type exit to exit", main);
		try {
			shell.processLine("?l");
		} catch (CLIException e) {
			e.printStackTrace();
		}
		shell.commandLoop();
	}
	
	@Command(description = "Get a person's information")
	public void getPerson(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		System.out.println(p.toString());
	}
	
	@Command(description = "Get a person's children")
	public void getChildren(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		System.out.println(api.getChildren(p).toString());
	}
	
	@Command(description = "Get a person's father")
	public void getFather(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		System.out.println(api.getFather(p).toString());
	}
	
	@Command(description = "Get a person's mother")
	public void getMother(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		System.out.println(api.getMother(p).toString());
	}
	
	@Command(description = "Get a person's parents")
	public void getParents(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		System.out.println(api.getParents(p).toString());
	}
	
	@Command(description = "Get a person's grand parents")
	public void getGrandParents(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		System.out.println(api.getGrandParents(p).toString());
	}
	
	@Command(description = "Get a person's grand children")
	public void getGrandChildren(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		System.out.println(api.getGrandChildren(p).toString());
	}
	
	@Command(description = "Get a person's siblings")
	public void getSiblings(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		api.getSiblings(p);
	}
	
	@Command(description = "Get a person's cousins")
	public void getCousins(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		api.getCousins(p);
	}
	
	@Command(description = "Add a new person")
	public void addPerson(){
		api.addPerson();
	}
	
	@Command(description = "Check if two people are related")
	public void isRelated(@Param(name = "name") String name1, @Param(name = "name") String name2){
		api.isRelated(name1, name2);
	}
	
	@Command(description = "Get all of a person's relatives")
	public void getRelatives(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		api.getRelatives(p);
	}
	
	@Command(description = "Print all families")
	public void printFamilies(){
		api.printFamilies();
	}
	
	@Command(description = "Get the oldest person in a family")
	public void getOldest(@Param(name = "name") String name){
		Person p = api.getPerson(name);
		api.getOldest(p);
	}
	
	@Command(description = "Get largest Family")
	public void printLargestFamily(){
		api.printLargestFamily();
	}
	
	@Command(description = "Get number of families in the model")
	public void numberOfFamilies(){
		api.numberOfFamilies();
	}
	
	@Command(description = "Get how many individuals are in the model")
	public void numberOfPeople(){
		api.numberOfPeople();
	}
}
