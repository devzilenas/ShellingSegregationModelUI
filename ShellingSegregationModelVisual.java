/**
 * Creates an image from model.
 */
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator; 

public class ShellingSegregationModelVisual
{
	public static BufferedImage draw(BufferedImage image, ShellingSegregationModel ssm)
	{
		Grid<ShellingSegregationModel.Element> grid = ssm.getField();

		Graphics2D g = image.createGraphics();
		Color red   = Color.RED  ;
		Color blue  = Color.BLUE ;
		Color black = Color.BLACK;

		Color color = null;
		int idx = 0;
		for (Iterator<ShellingSegregationModel.Element> iterator = grid.iterator(); iterator.hasNext(); idx++)
		{
			int x = grid.x(idx);
			int y = grid.y(idx);
			switch (iterator.next())
			{
				case BLUE:
					color = blue;
					break;
				case RED:
					color = red;
					break;
				default:
					color = black;
			}
			g.setColor(color);
			g.drawLine(x,y,x,y);
		}
		return image; 
	}
}
