package gameClient;

import javax.swing.*;


/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame {

	private MyPanel myPanel;

	MyFrame(String a) {
		initFrame(a);
		initPanel();
	}

	private void initFrame(String a)
	{
		this.setTitle(a);
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initPanel()
	{
		 myPanel = new MyPanel();
		this.add(myPanel);
	}

	public void update(Arena ar) {
		myPanel.update(ar);
	}

}

