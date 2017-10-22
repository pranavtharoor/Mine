import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


class Game extends JFrame
{
	public static void main(String[] args) 
	{
	new Game();
	}

	int rows=10,columns=10;
	Random random = new Random();
	JButton[][] grids = new JButton[rows][columns];
	int totalZeroesCount;
	JButton start = new JButton("start");
	final int CELL_HEIGHT = 40, CELL_LENGTH = 40, CELL_PADDING = 5, PANEL_BORDER = 25;
	Color backgroundColor = new Color(103,200,190);
	Color cellColor = new Color(134,134,134);
	Color numColor = new Color(0,0,0);
	Color postCellColor = new Color(224,224,224);
	Border mainBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
	ListenForGridButton buttonClicked =new ListenForGridButton();
	ListenForStartButton startButtonClicked = new ListenForStartButton();
	public Game()
	{
		this.setSize(CELL_LENGTH * (columns + 4) + CELL_PADDING * columns , CELL_HEIGHT * (rows + 1) + CELL_PADDING * rows + PANEL_BORDER * 2);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel= new JPanel();
		JPanel settingsPanel = new JPanel();
		JPanel outerPanel = new JPanel();
		FlowLayout outerLayout = new FlowLayout();
		outerPanel.setLayout(outerLayout);
		mainPanel.setBorder(mainBorder);
		GridLayout mainLayout = new GridLayout(rows, columns, CELL_PADDING, CELL_PADDING);
		mainPanel.setLayout(mainLayout);
		mainPanel.setBackground(backgroundColor);
		settingsPanel.setBackground(backgroundColor);
		outerPanel.setBackground(backgroundColor);
		for(int i = 0;i < rows;i++)
			for(int j = 0;j < columns;j++)
			{
				grids[i][j] = new JButton("0");
				grids[i][j].setEnabled(true);
				grids[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				grids[i][j].setFont(new Font("Ariel",Font.PLAIN,25));
				grids[i][j].setFocusPainted(false);
				grids[i][j].setMargin(new Insets(0, 0, 0, 0));
				grids[i][j].setPreferredSize(new Dimension(CELL_LENGTH, CELL_HEIGHT));
				grids[i][j].setBackground(cellColor);
				grids[i][j].setForeground(cellColor);
				grids[i][j].setBorderPainted(false);
				grids[i][j].addActionListener(buttonClicked);
				mainPanel.add(grids[i][j]);
			}
		setMines();
		start.addActionListener(startButtonClicked);
		//test data
		// for(int i = 0; i< 3;i++)
		// 	for( int j = 0; j < 3; j++)
		// 		grids[i][j].setText("3");
		
		settingsPanel.add(start);
		outerPanel.add(mainPanel);
		outerPanel.add(settingsPanel);
		outerPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.add(outerPanel);
		this.setVisible(true);
	}

	void setMines()
	{
		int x=(int)Math.sqrt(rows*columns);
		int mineCount=0;
		while(mineCount<x)
		{
			int p = random.nextInt(rows);
			int q = random.nextInt(columns);
			System.out.println(p+" "+q);
			if(grids[p][q].getText()!="-1")
			{
				grids[p][q].setText("-1");
				mineCount++;
			}
		}
		for(int r = 0; r < rows; r++)
			for( int c = 0; c < columns; c++)
				if( grids[r][c].getText()=="-1")
					for(int i = r-1; i < r+2; i++)
						for(int j = c-1; j < c+2; j++)
							if(i > -1 && i < rows && j> -1 && j < columns && (i != r || j != c))
								if(!grids[i][j].getText().equals("-1"))
									grids[i][j].setText(""+(1+Integer.parseInt(grids[i][j].getText())));	
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < columns; j++)
				if(grids[i][j].getText().equals("0"))
					totalZeroesCount+=1;
		// final totalZeroesCount = totalZeroesCount;

	}
	void setMineIcon(JButton a)
	{
		// grids[r][c].setIcon(new ImageIcon(getClass().getResource()));
		ImageIcon icon = new ImageIcon("./icon.png");
		Image img = icon.getImage(); 
   		Image newimg = img.getScaledInstance( CELL_LENGTH, CELL_LENGTH,  java.awt.Image.SCALE_SMOOTH );  
   		icon = new ImageIcon( newimg);
   		a.setHorizontalAlignment(SwingConstants.LEFT);
   		a.setIcon(icon);
   		a.setDisabledIcon(icon);
	}

	void restartGame()
	{
		for(int i = 0;i < rows;i++)
			for(int j = 0;j < columns;j++)
			{
				grids[i][j].setText("0");
				grids[i][j].setIcon(null);
				grids[i][j].setEnabled(true);
				grids[i][j].setFont(new Font("Ariel",Font.PLAIN,25));
				grids[i][j].setFocusPainted(false);
				grids[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				grids[i][j].setMargin(new Insets(0, 0, 0, 0));
				grids[i][j].setPreferredSize(new Dimension(CELL_LENGTH, CELL_HEIGHT));
				grids[i][j].setBackground(cellColor);
				grids[i][j].setForeground(cellColor);
				grids[i][j].setBorderPainted(false);
			}
		setMines();
	}

	class ListenForGridButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton w = (JButton) e.getSource();
			for(int i = 0; i < rows; i++)
				for(int j = 0; j < columns; j++)
					if(w==grids[i][j])
					{
						this.recursiveClick(w,1,i,j);
					}
		}

		void recursiveClick(JButton a, int enable,int r, int c)
		{
			if(a.getText().equals("-1"))
			{
				a.setEnabled(false);
				if(enable==1)
				{
					setMineIcon(a);
					gameOver();
				}
			}
			else if(a.getText().equals("0"))
			{
				a.setBackground(postCellColor);
				a.setEnabled(false);
				a.setText("");
				for(int i = r-1; i < r+2; i++)
					for(int j = c-1; j < c+2; j++)
						if(i > -1 && i < rows && j> -1 && j < columns)
							this.recursiveClick(grids[i][j],0,i,j);
			}
			else
			{
				a.setBackground(postCellColor);
				a.setEnabled(false);
			}
			if(won(r,c))
			{
				System.out.println("Won");
			}
		}

		void gameOver()
		{
			;
		}

		boolean won(int r, int c)
		{
			int count=0;
			for( int i =0; i < rows; i++)
				for( int j = 0; j < columns; j++)
					if(!grids[i][j].getText().equals(""))
						count+=1;
			System.out.println(r+" "+c+" "+count);
			if(count==totalZeroesCount)
				return true;
			return false;
		}
	}

	class ListenForStartButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			restartGame();
		}
	}
}