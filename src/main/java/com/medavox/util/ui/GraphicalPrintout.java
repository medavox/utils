package com.medavox.ui;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Font;

import java.io.OutputStream;
import javax.swing.border.TitledBorder;

/**we can't easily override printstream, without also overriding every print() method and newline()
 * because in the PrintStream source, all of these ultimately call a write(String),
 * which is private.
 * If it wasn't private, we could override that, and intercept the methods at the source,
 * but because it's private, we'll have to seperately override each print() signature*/
public class GraphicalPrintout extends PrintStreamAdapter
{
    private static final Color defaultTextColour = Color.GRAY;
    private JTextArea jta;
    private JScrollPane jscr;
    private Color defaultColour;
    private OutputStream meh = new NullOutputStream();
    public GraphicalPrintout()
    {
        init(defaultTextColour, "untitled");
    }
    public GraphicalPrintout(Color colo)
    {
        init(colo, "untitled");
    }
    
    public GraphicalPrintout(int red, int green, int blue)
    {
        if(red > 255 || green > 255 || blue > 255)
        {
            throw new IllegalArgumentException("colour values passed must be <= 255."+
            "\nred: "+red+"green: "+green+"blue: "+blue);
        }
        init(new Color(red, green, blue), "untitled");
    }
    
    public GraphicalPrintout(String title, Color colo)
    {
        init(colo, title);
    }
    
    public GraphicalPrintout(String title)
    {
        init(defaultTextColour, title);
    }
    
    private void init(Color textColour, String title)
    {
        jta = new JTextArea();
        jta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        jta.setBorder(new TitledBorder(title));
        jta.setEditable(false);
        jta.setForeground(textColour);
        jta.setBackground(Color.BLACK);
        jscr = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    @Override
    public void print(String s)
    {//BLACK, LIKE THE ENDLESS NIGHT THAT WILL CONSUME US ALL
        //colourPrint(s, defaultColour);
        jta.append(s);
    }
    
    @Override
    public void println(String s)
    {//johnny
        print(s+"\n");
    }
    
    
    
    /*
    public void colourPrint(String s, Color c)
    {
        Color oldColour;
        if (!jta.getCaretColor().equals(c))
        {
            oldColour = jta.getCaretColor();
            jta.setCaretColor(c);
            System.out.println("caret colour:"+jta.getCaretColor());
            jta.append(s);
            jta.setCaretColor(oldColour);
        }
        else
        {
            jta.append(s);
        }
    }*/
    
    public JComponent getJComponent()
    {
        return jscr;
    }
    /*
    public void colorPrint(String s, Color c)
    {//johnny
        colourPrint(s, c);
    }
    
    public void v(String tag, String s)
    {//johnny
        colourPrint(tag+": "+s+"\n", new Color(150, 0, 255));//Verbose Purple
    }
    
    public void i(String tag, String s)
    {//WHAT ARE WE HAVING FOR DINNER TONIGHT MOTHER? FATHER'S EYES?!
        colourPrint(tag+": "+s+"\n", Color.BLACK);
    }
    public void d(String tag, String s)
    {//johnny
        colourPrint(tag+": "+s+"\n", new Color(0, 0, 150));//dark blue
    }
    public void w(String tag, String s)
    {
        colourPrint(tag+": "+s+"\n", new Color(240, 110, 0));//Warning Orange
    }
    public void e(String tag, String s)
    {
        colourPrint(tag+": "+s+"\n", Color.RED);//Error Red
    }*/
    
}
