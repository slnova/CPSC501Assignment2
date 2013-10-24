/*==========================================================================
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
		
		inspectMethods(ObjClass);
		
		inspectConstructors(ObjClass);
		
		System.out.println("Fields:");
		inspectFields(obj, ObjClass,objectsToInspect, recursive);
	
		if(!ObjClass.isPrimitive() && ObjClass.isArray())
			arrayInfo(obj, recursive);
		
		if(recursive)
			inspectFieldClasses(obj, ObjClass, objectsToInspect, recursive);
	}
    
    private void arrayInfo(Object obj, boolean recursive)
	{
		Object arrayObject;
		for(int j = 0; j < Array.getLength(obj); j++)
		{
			arrayObject = Array.get(obj,j);
			if(arrayObject != null)
			{
				System.out.println("----------- ARRAY INFO (" + (j+1) + ") -----------");
				inspect(arrayObject, arrayObject.getClass(), recursive);
				System.out.println("----------- ARRAY INFO (" + (j+1) + ") INSPECTION DONE -----------");
			}
			else
				System.out.println("\tindex " + j + ": " + arrayObject);
			}   
	}
    
    //--------------------------------------------------------------
    
    private void inspectConstructors(Class ObjClass)
	{
		Constructor[] consList = ObjClass.getDeclaredConstructors();

		System.out.println("Constructors:");
		if(consList.length > 0)
		{
			for (int j = 0; j < consList.length; j++)
				System.out.println("\t" + consList[j].toString());
			
		}
		else
			System.out.println("\tNo Constructors");

		System.out.println();//for formatting
	}
    
    //-------------------------------------------------------------
    
    private void inspectMethods(Class ObjClass)
	{
		Method [] methods = ObjClass.getDeclaredMethods();

		try
		{
			System.out.println("Methods:");
			if(methods.length > 0)
			{
				for ( int i = 0; i < methods.length; i++)
				{
					Method m = methods[i];

					String line = "\t" + Modifier.toString(m.getModifiers()) + " ";  
					line += m.getReturnType().toString() + " ";
					line += m.getName() + " (";

					Class []argTypes = m.getParameterTypes();
					for (int j = 0; j < argTypes.length; j++ )
					{
						if(j !=  argTypes.length - 1)
							line += argTypes[j].toString() + ", ";
						else
							line += argTypes[j].toString();
					}
					line += ") ";

					Class []exTypes = m.getExceptionTypes();
					for (int j = 0; j < exTypes.length; j++ )
					{
						if(j !=  exTypes.length - 1)
							line += exTypes[j].toString() + ", ";
						else
							line += exTypes[j].toString();

					}
					System.out.println(line);
				}
			}
			else
				System.out.println("\tNo Methods");

			System.out.println(); //for formatting 
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
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


	//---------------------------------------------------------------
    
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
		System.out.println("INSPECTING FIELD: " + f.getName() + " DONE" );
	    }
    }
    //-----------------------------------------------------------
    private void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect)
  
    {
    	Object fieldObj = null;
		Field [] fields = ObjClass.getDeclaredFields();
		Field.setAccessible(fields, true);
		Object arrayObject;

		try{
			if(fields.length > 0)
			{
				for(int i = 0; i< fields.length; i++)
				{
					fieldObj = fields[i].get(obj);
					System.out.print(fields[i].toString());

					if(fieldObj != null) 
					{
						if(fieldObj.getClass().isArray()) 
						{
							System.out.println(": ");
							for(int j = 0; j < Array.getLength(fieldObj); j++)
							{
								arrayObject = Array.get(fieldObj,j);
								if(fieldObj.getClass().getComponentType().isPrimitive())
									System.out.println("\tindex " + j + ": " + arrayObject);

								else
								{
									if(arrayObject != null)
									{
										System.out.println("----------- ARRAY ITEM (" + (j+1) + ") -----------");
										inspect(arrayObject, arrayObject.getClass(), recursive);
										System.out.println("----------- END ARRAY ITEM (" + (j+1) + ") -----------");
									}
									else
										System.out.println("\tindex " + j + ": " + arrayObject);
								} 						
							}    					
						}
						else 
						{
							System.out.println(" = "  + fields[i].get(obj));
							if(!fields[i].getType().isPrimitive() && fields[i].get(obj) != null) 
								objectsToInspect.addElement(fields[i]);
						}
					}
					else 
						System.out.println(" = null");
					
				}
			}
			else
				System.out.println("\tNo Fields");
		}
		catch(Exception e) {}   
	
    }
}
