package com.eddie.executiveexperience.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.Game;

import java.util.ArrayList;

public class UI
{
    public int lineCount = 0;
    public int leftMargin = 768;
    public int topMargin = 0;
    public int fontHeight = 18;
    public int lineDisplay = 10;
    public ArrayList<TextLine> text = new ArrayList<>();
    public Console console;
    private BitmapFont font;
    private SpriteBatch spriteBatch;

    public UI()
    {
        console = new Console();

        spriteBatch = new SpriteBatch();

        //font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("fonts\\Minecraftia.ttf"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static String wrap(String in, int len)
    {
        in = in.trim();
        in = in.replace("~", "");
        if(in.length() < len)
        {
            return in;
        }
        if(in.substring(0, len).contains("\n"))
        {
            return in.substring(0, in.indexOf("\n")).trim() + "\n\n" + wrap(in.substring(in.indexOf("\n") + 1), len);
        }
        int place = Math.max(Math.max(in.lastIndexOf(" ", len), in.lastIndexOf("\t", len)), in.lastIndexOf("-", len));
        return in.substring(0, place).trim() + "\n" + wrap(in.substring(place), len);
    }

    public void writeText(String s)
    {
        if(text.size() >= lineDisplay)
        {
            text.remove(0);
        }

        text.add(0, new TextLine(s));
    }

    public void writeText(String s, TextType textType)
    {
        if(text.size() >= lineDisplay)
        {
            text.remove(0);
        }

        text.add(0, new TextLine(s, textType));
    }


    public void loadFont()
    {
        font = new BitmapFont(Gdx.files.internal("assets/fonts/minecraftia-16px.fnt"), Gdx.files.internal("assets/fonts/minecraftia-16px.png"), false);
        font.setColor(1f, 1f, 1f, 1f);
        font.setScale(1, 1);
    }

    public void renderShadowBox()
    {
        // shadow box behind text pane
        if(text.size() > 0)
        {
            Color shade = new Color(0, 0, 0, 0.75f);
            Game.instance.getShapeRenderer().setColor(shade);
            Game.instance.getShapeRenderer().rect(fontHeight / 2, Env.virtualHeight - (1 + text.size()) * fontHeight - fontHeight / 2, Env.virtualWidth - fontHeight, (1 + text.size()) * fontHeight);
        }
    }

    public void render(SpriteBatch batch)
    {
        if(font == null)
        {
            loadFont();
        }

        // Remove old lines
        for(int i = 0; i < text.size(); i++)
        {
            TextLine tl = text.get(i);

            if(tl.textType == TextType.CONSOLE || ((tl.added) < System.currentTimeMillis() - 5000 && !(tl.textType == TextType.ERROR)) || ((tl.added) < System.currentTimeMillis() - 10000 && tl.textType == TextType.ERROR))
            {
                text.remove(i);
            }
        }

        if(console.usingConsole)
        {
            text.add(0, new TextLine("", TextType.CONSOLE));
        }

        lineCount = lineDisplay - text.size();
        leftMargin = fontHeight;
        topMargin = 768 - fontHeight * (lineDisplay - 1);

        for(TextLine tl : text)
        {
            Color c;

            switch(tl.textType)
            {
                case CONSOLE:
                    c = Color.GREEN;

                    tl.text = "Input: " + console.consoleInput;
                    break;

                case ERROR:
                    c = Color.RED;
                    break;

                default:
                    c = Color.WHITE;
                    break;
            }

            writeln(tl.text, c, batch);
        }
    }

    public void writeln(String s, Color c, Batch batch)
    {
        String wrappedStr = wrap(s, 100);
        String[] lines = wrappedStr.split("\n");

        for(int i = 0; i < lines.length; i++)
        {
            int stringX = leftMargin;
            int stringY = topMargin + fontHeight * (lineCount - 1);

            font.setColor(Color.BLACK);
            font.draw(batch, lines[i], stringX - 2, stringY - 2);
            font.setColor(c);
            font.draw(batch, lines[i], stringX, stringY);

            lineCount++;
        }
    }

    public void writeln(String s, Color c)
    {
        String wrappedStr = wrap(s, 100);
        String[] lines = wrappedStr.split("\n");

        int stringX = leftMargin;
        int stringY = topMargin + fontHeight * (lineCount - 1);

        spriteBatch.begin();

        for(int i = 0; i < lines.length; i++)
        {
            font.setColor(Color.BLACK);
            font.draw(spriteBatch, lines[i], stringX - 2, stringY - 2);
            font.setColor(c);
            font.draw(spriteBatch, lines[i], stringX, stringY);

            lineCount++;
        }

        spriteBatch.end();
    }

    public void drawString(String s, Color c, int x, int y, SpriteBatch batch)
    {
        if(font == null)
        {
            loadFont();
        }

        font.setColor(c);
        font.draw(batch, s, x, y);
    }

    public Console getConsole()
    {
        return console;
    }

    public enum TextType
    {
        INFO,
        ERROR,
        CONSOLE
    }

    public class TextLine
    {
        public long added;
        public String text;
        public TextType textType;

        public TextLine(String s)
        {
            this.text = s;
            this.added = System.currentTimeMillis();
            textType = TextType.INFO;
        }

        public TextLine(String s, TextType textType)
        {
            this.text = s;
            this.added = System.currentTimeMillis();
            this.textType = textType;
        }
    }
}
