/**
 * @author Marius Žilėnas
 *
 * @since 2014-12-31
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.image.BufferedImage;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics2D;

public class Application
{
	JFrame        mainFrame    = null;
	JLabel        pictureLabel = null;
	public static final Dimension PICTURE_PREFERRED_DIM = new Dimension(150, 150);

	/**
	 * Dimension the application was run with.
	 */
	public static Dimension dimension = null;
	public static double    threshold = 0.0 ;
	ShellingSegregationModel ssm = null;

	/**
	 * Image to display on the panel.
	 */
	BufferedImage picture   = null;

	ButtonFactory buttonFactory = new ButtonFactory(
			new ButtonActionListener());

	public void setMainFrame(JFrame mainFrame)
	{
		this.mainFrame = mainFrame;
	}

	public JFrame getMainFrame()
	{
		return mainFrame;
	}

	public void setPictureLabel(JLabel pictureLabel)
	{
		this.pictureLabel = pictureLabel;
	}

	public JLabel getPictureLabel()
	{
		return pictureLabel;
	}

	public static Dimension getPicturePreferredDimension()
	{
		return PICTURE_PREFERRED_DIM;
	}

	public static void setDimension(Dimension dim)
	{
		dimension = dim;
	}

	public static Dimension getDimension()
	{
		return dimension;
	}

	public static void setThreshold(double thr)
	{
		threshold = thr;
	}

	public static double getThreshold()
	{
		return threshold;
	} 

	public void setShellingSegregationModel(ShellingSegregationModel ssm) 
	{ 
		this.ssm = ssm;
	}

	public ShellingSegregationModel getShellingSegregationModel()
	{
		return ssm;
	}

	public void setPicture(BufferedImage picture)
	{
		this.picture = picture;
	}

	public BufferedImage getPicture()
	{
		return picture;
	}

	public void setButtonFactory(ButtonFactory buttonFactory)
	{
		this.buttonFactory = buttonFactory;
	}

	public ButtonFactory getButtonFactory()
	{
		return buttonFactory;
	}

	/**
	 * Resizes picture so that it fits to imageicon area.
	 */
	void repaintImageIcon(BufferedImage image)
	{
		AffineTransform at = AffineTransform.getScaleInstance(
				getPicture().getWidth()  / (double) image.getWidth(),
				getPicture().getHeight() / (double) image.getHeight());
		AffineTransformOp aop = new AffineTransformOp(
				at, AffineTransformOp.TYPE_BICUBIC);
		Graphics2D g2d = (Graphics2D) (getPicture().createGraphics());
		g2d.drawImage(image, aop, 0, 0);
		getPictureLabel().setIcon(
				new ImageIcon(getPicture()));
	}

	/**
	 * Constructor.
	 *
	 * @param dimension of the image.
	 */
	public Application()
	{
		setPicture(
				new BufferedImage(
					(int) getDimension().getWidth(), 
					(int) getDimension().getHeight(),
					BufferedImage.TYPE_INT_RGB));

		this.ssm = createShellingSegregationModel();
	}

	public ShellingSegregationModel createShellingSegregationModel()
	{
		return new ShellingSegregationModel(
				getThreshold(),
				(int)getDimension().getWidth(), 
				(int)getDimension().getHeight());
	}

	public void createAndShowMainFrame()
	{
		JFrame frame = new JFrame("Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(makeMainPanel());

		frame.pack();
		frame.setVisible(true);
		setMainFrame(frame);
	}

	public class DefaultButtonActionListener
			implements ActionListener
	{ 
		/**
		 * Default action command.
		 */
		String defaultActionCommand = "default_action_command";

		public void setDefaultActionCommand(String defaultActionCommand)
		{
			this.defaultActionCommand = defaultActionCommand;
		}

		public String getDefaultActionCommand()
		{
			return defaultActionCommand;
		}

		public void defaultAction(Object source)
		{
			JOptionPane.showMessageDialog(
					null, 
					"You just pressed a button \"" + ((JButton)source).getText() + "\"",
					"Info",
					JOptionPane.INFORMATION_MESSAGE);
		}

		public void actionPerformed(ActionEvent e)
		{
			switch (e.getActionCommand())
			{
				default:
					defaultAction(e.getSource());
					break;
			}
		}
	}

	public class ButtonActionListener
			extends DefaultButtonActionListener
	{
		public void iterate()
		{
			getShellingSegregationModel().iterate();
		}

		public void reset()
		{
			ssm = createShellingSegregationModel();
		}

		public void actionPerformed(ActionEvent e)
		{
			switch (e.getActionCommand())
			{
				case "iterate":
					iterate();
					repaint();
					break;
				case "reset":
					reset();
					repaint();
					break;
			}
		}
	}

	public class ButtonFactory
	{
		ActionListener actionListener = null;

		/**
		 * Sets default action listener.
		 */
		public void setListener(ActionListener actionListener)
		{
			this.actionListener = actionListener;
		}

		public ActionListener getActionListener()
		{
			return actionListener;
		}

		public ButtonFactory(ActionListener actionListener)
		{
			this.actionListener = actionListener;
		}

		public JButton createButton(
									String title,
									String actionCommand, 
									ActionListener listener)
		{
			JButton b = new JButton(title);
			b.setActionCommand(actionCommand);
			b.addActionListener(listener);
			return b;
		}

		public JButton createButton(String title)
		{
			return createButton(
								title,
								((ButtonActionListener) getActionListener()).getDefaultActionCommand(),
								getActionListener());
		}

	}

	/**
	 * Creates a panel with an image placeholder and buttons.
	 */
	public JPanel makeMainPanel()
	{
		JPanel panel = new JPanel(
								  new GridBagLayout());

		GridBagConstraints c;

		JButton b1 = getButtonFactory().createButton("Iterate");
		b1.setActionCommand("iterate");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		panel.add(b1, c);

		JButton b2 = getButtonFactory().createButton("Reset");
		b2.setActionCommand("reset");
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(10,10,10,10);
		panel.add(b2, c);

		JLabel picLabel = new JLabel(
									 new ImageIcon(getPicture()));
		setPictureLabel(picLabel);

		c = new GridBagConstraints();
		c.fill  = GridBagConstraints.BOTH;
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,10,10,10);
		panel.add(picLabel, c);

		return panel;
	}

	/**
	 * Repaints model's state.
	 */
	public void repaint()
	{
		repaintImageIcon(
				ShellingSegregationModelVisual.draw(
					getPicture(), 
					getShellingSegregationModel()));
	}

	public static void main(String[] args)
	{ 
		if (3 == args.length)
		{
			setThreshold(Double.valueOf(args[0]));
			setDimension(
					new Dimension(
						Integer.valueOf(args[1]),
						Integer.valueOf(args[2])));
		}
		else
		{
			setThreshold(ShellingSegregationModel.THRESHOLD);
			setDimension(getPicturePreferredDimension());
		}
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				new Application().createAndShowMainFrame();
			}
		});
	}
}
