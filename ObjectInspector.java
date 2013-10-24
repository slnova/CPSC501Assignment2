/*==========================================================================
File: ObjectInspector.java
Assignment 2 - CPSC 501

Stuart Laing 10081955
Assignment 2 
October 24th 2013

The base code for this assignment was provided to us and all credit for it
goes to Jordan Kenny.


========================================================================*/

import java.util.*;
import java.lang.reflect.*;


public class ObjectInspector
{
    public ObjectInspector() { }

    //-----------------------------------------------------------
    public void inspect(Object obj, boolean recursive)
    {
    	Class objClass = obj.getClass();

		inspect(obj, objClass, recursive);
	   
    }
    //-----------------------------------------------------------
    
    public void inspect(Object obj, Class ObjClass, boolean recursive)
	{
		Vector objectsToInspect = new Vector();

		System.out.println("Inside Inspector: " + ObjClass.getCanonicalName() + " (recursive = "+recursive+")\n");	

		inspectSuperclasses(obj, ObjClass, recursive);

		inspectInterfaces(obj, ObjClass, recursive);
		
	}
    //-------------------------------------------------------------
    private void inspectSuperclasses(Object obj, Class ObjClass, boolean recursive)
	{
		if(ObjClass.getSuperclass() != null)
		{
			System.out.println("----------- SUPERCLASS: " + ObjClass.getSuperclass().getName() + " -----------");
			inspect(obj, ObjClass.getSuperclass(), recursive);
			System.out.println("----------- SUPERCLASS " + ObjClass.getSuperclass().getName() + " INSPECTION DONE -----------\n");
		}
		else
			System.out.println("\tNo Superclass");
	}


	//-----------------------------------------------------------
	private void inspectInterfaces(Object obj, Class ObjClass, boolean recursive)
	{
		Class []listOfInterfaces = ObjClass.getInterfaces();

		if(listOfInterfaces.length > 0)
		{
			System.out.println(ObjClass.toString() + " Implements these interfaces: \n");

			for ( int i = 0; i < listOfInterfaces.length; i++ )
				if(listOfInterfaces[i] != null)
				{

					System.out.println("----------- INTERFACE (" + (i+1) + "): " + listOfInterfaces[i].getName() + " -----------");
					inspect(obj, listOfInterfaces[i], recursive);
					System.out.println("----------- INTERFACE (" + (i+1) + "): " + listOfInterfaces[i].getName() + " INSPECTION DONE -----------");

				}

			System.out.println("----------- ALL INTERFACE INSPECTIONS DONE -----------\n");

		}
		else
			System.out.println("Interfaces: \n\t No Interfaces");

		System.out.println();//for formatting
	}
	//----------------------------------------------------------------
    
    private void inspectFieldClasses(Object obj,Class ObjClass,
				     Vector objectsToInspect,boolean recursive)
    {
	
	if(objectsToInspect.size() > 0 )
	    System.out.println("---- Inspecting Field Classes ----");
	
	Enumeration e = objectsToInspect.elements();
	while(e.hasMoreElements())
	    {
		Field f = (Field) e.nextElement();
		System.out.println("Inspecting Field: " + f.getName() );
		
		try
		    {
			System.out.println("******************");
			inspect( f.get(obj) , recursive);
			System.out.println("******************");
		    }
		catch(Exception exp) { exp.printStackTrace(); }
	    }
    }
    //-----------------------------------------------------------
    private void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect)
  
    {
	
	if(ObjClass.getDeclaredFields().length >= 1)
	    {
		Field f = ObjClass.getDeclaredFields()[0];
		
		f.setAccessible(true);
		
		if(! f.getType().isPrimitive() ) 
		    objectsToInspect.addElement( f );
		
		try
		    {
			
			System.out.println("Field: " + f.getName() + " = " + f.get(obj));
		    }
		catch(Exception e) {}    
	    }

	if(ObjClass.getSuperclass() != null)
	    inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
    }
}
