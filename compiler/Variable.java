/**Variable
 * This class is for storing the name, type, and memory location of variables in
 * the KCBaseVisitor class.
 * @author Fanni Kertesz
 * @version 1.0
 * Assignment 5
 * CS 322 - Compiler Construction
 * Spring 2024
 */
package compiler;

public class Variable
{
    //Initialize type, name, and memory location
    public String type = "";
    public String name = "";
    public int memoryLocation = 0;

    /**
     * Constructor with preferred argument.
     * @param type
     * @param name
     * @param memoryLocation
     */
    public Variable(String type, String name, int memoryLocation)
    {
        this.type = type;
        this.name = name;
        this.memoryLocation = memoryLocation;
    }//end constructor

    /**
     * Getter for type
     * @return type
     */
    public String getType()
    {
        return type;
    }//end getType

    /**
     * Getter for name
     * @return name
     */
    public String getName()
    {
        return name;
    }//end getName

    /**
     * Getter for memory location
     * @return memory location
     */
    public int getMemoryLocation()
    {
        return memoryLocation;
    }//end getMemoryLocation
    
}//end class