package com.medavox.ui;

import java.awt.*;
import javax.swing.*;
import java.io.PrintStream;


public class OutputMultiplexer extends JFrame
{
    public OutputMultiplexer(String title, GraphicalPrintout... streams)
    { 
        if(streams.length < 2 || streams.length > 3)
        {
            throw new IllegalArgumentException("Wrong number of arguments passed to OutputMultiplexer:"+streams.length
                        +"\nOutputMultiplexer takes 2-3 PrintStreams as arguments.");
        }
        
        try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception bob)
		{
			bob.printStackTrace();
		}
        /*
        JTextArea[] textAreas = new JTextArea[streams.length];
        
        for(JTextArea jta : textAreas)
        {
            jta = new JTextArea();
        }
        */
        //
        
        
        switch(streams.length)
        {
            case 2:
                setLayout(new GridLayout(0, 2, 0, 1) );
                add(streams[0].getJComponent());
                add(streams[1].getJComponent());
            break;
            
            case 3:
                add(streams[0].getJComponent(), BorderLayout.EAST);
                add(streams[1].getJComponent(), BorderLayout.CENTER);
                add(streams[2].getJComponent(), BorderLayout.WEST);
            break;
        }
        /*
        
		add(imagePanel, BorderLayout.CENTER);
		add(bottomButtonPanel, BorderLayout.SOUTH);
		
		//bottomButtonPanel.setLayout(new GridLayout(1, 0) );
		bottomButtonPanel.setLayout(new BorderLayout(5, 5) );
		ImageIcon prevIcon = new ImageIcon("images/go-previous.png");
		ImageIcon nextIcon = new ImageIcon("images/go-next.png");
		JButton prevButton = new JButton(prevIcon);
		JButton nextButton = new JButton(nextIcon);
		bottomButtonPanel.add(prevButton, BorderLayout.WEST);
		bottomButtonPanel.add(nextButton, BorderLayout.EAST);
        */
        //add(new Board());
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        //setResizable(false);
    
    }
}
