
package compiler;

public class Variable
{
    public String type = "";
    public String name = "";
    public int memoryLocation = 0;

    public Variable(String type, String name, int memoryLocation)
    {
        this.type = type;
        this.name = name;
        this.memoryLocation = memoryLocation;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public int getMemoryLocation()
    {
        return memoryLocation;
    }
}