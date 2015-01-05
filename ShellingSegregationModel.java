import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ShellingSegregationModel
{
	public static enum Element
	{
		BLUE, RED, NULL;
	}

	/**
	 * Field of elements.
	 */
	public Grid<Element> field    ;
	/**
	 * Temporary field of elements.
	 */
	public Grid<Element> temporary;

	/**
	 * Number of neighbors possible.
	 */
	public static final int    NEIGHBORS = 8;
    public static final double THRESHOLD = 0.26;        
    
	public double threshold = THRESHOLD;
    public int iteration;

    public void setField(Grid<Element> field)
    {
        this.field = field;
    }
       
    public Grid<Element> getField()
    {
        return field;
    }
    
    public void setTemporary(Grid<Element> temporary)
    {
        this.temporary = temporary;
    }
    
    public Grid<Element> getTemporary()
    {
        return temporary;
    }
    
	public void setThreshold(double threshold)
	{
		this.threshold = threshold;
	}

	public double getThreshold()
	{
		return threshold;
	}

    public void setIteration(int iteration)
    {
        this.iteration = iteration;
    }

    public int getIteration()
    {
        return iteration;
    }
    
	public int getNeighbors()
	{
		return NEIGHBORS; 
	}

    public ShellingSegregationModel(double threshold, int width, int height)
    {    
		this.threshold = threshold;
		field = new Grid(width, height);
		/**
		 * Fill with random values.
		 */
		field.populate(
				Element.values());
    }

    /**
	 * How many neighbors needed for threshold.
	 */
    double getCalculatedThreshold()
    {
        return getThreshold() * getNeighbors();
    } 

	/**
	 * @return number of the neighboring cells that are the same
	 */
    int sameNeighbors(int x, int y)
    {
        return Element.NULL.equals(getField().get(x,y))
				? 0 
				: Collections.frequency(
						getField().getNeighbors(x,y),
						getField().get(x,y));
    }    

    void duplicateToTemporary()
    {
        setTemporary(
				new Grid(
					     getField()));
    }
    
    void assignFromTemporary()
    {
		getField().setElements(
				getTemporary().getElements());
    }
    
	public void iterate()
	{
		iterate(1);
	}

    public void iterate(int iterations)
    {        
        for (int i = 0; i < iterations; i++)          
        {
            duplicateToTemporary();
            for (int y = 0; y < getField().getHeight(); y++)
            {
                for (int x = 0; x < getField().getWidth(); x++)
                {
                    if (!Element.NULL.equals(getField().get(x,y)) && !isHappy(x,y))
                    {
                        swap(x,y);
                    }
                }
            }
            assignFromTemporary();
        }
        setIteration(getIteration()+iterations);
    }

    /**
	 * Swaps with empty random from temporary.
	 *
	 * @return true if swap occured
	 */
    void swap(int i, int j)
    {
		Element temp = getTemporary().get(i,j);
		getTemporary().put(getTemporary().randomIndex(Element.NULL), temp);
		getTemporary().put(i, j, Element.NULL);
    }

    boolean isHappy(int x, int y)
    {
        return   getCalculatedThreshold() 
			   < sameNeighbors(x, y);        
    }
}
