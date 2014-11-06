package com.eddie.executiveexperience.UI;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.Game;

import java.util.ArrayList;

public class UI
{
    BitmapFont font;
    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

    public int lineCount = 0;
    public int leftMargin = 768;
    public int topMargin = 0;
    public int fontHeight = 18;
    public int lineDisplay = 10;

    public ArrayList<TextLine> text = new ArrayList<>();

    public UI()
    {
        //font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("fonts\\Minecraftia.ttf"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void writeText(String s)
    {
        if(text.size() >= lineDisplay)
        {
            text.remove(0);
        }

        text.add(0, new TextLine(s));
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

    public void render(Graphics g, Application gc, SpriteBatch batch)
    {
        if(font == null)
        {
            loadFont();
        }

        // Remove old lines
        for(int i = 0; i < text.size(); i++)
        {
            TextLine tl = text.get(i);

            if((tl.added) < System.currentTimeMillis() - 3000)
            {
                text.remove(i);
            }
        }

        lineCount = lineDisplay - text.size();
        leftMargin = fontHeight;
        topMargin = 768 - fontHeight * (lineDisplay - 1);

        for(TextLine tl : text)
        {
            Color c = Color.WHITE;
            writeln(tl.text, c, g, batch);
        }
    }

    public void writeln(String s, Color c, Graphics g, SpriteBatch batch)
    {
        String wrappedStr = wrap(s, 100);
        String[] lines = wrappedStr.split("\n");

//        for(int i = lines.length - 1; i >= 0; i--)
        for(int i = 0; i < lines.length; i++)
        {
            int stringX = leftMargin;
            int stringY = topMargin + fontHeight * (lineCount - 1);

            font.setColor(Color.BLACK);
            font.draw(batch, lines[i], stringX + 2, stringY + 2);
            font.setColor(c);
            font.draw(batch, lines[i], stringX, stringY);

            lineCount++;
        }
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

    public class TextLine
    {
        public long added;
        public String text;

        public TextLine(String s)
        {
            this.text = s;
            this.added = System.currentTimeMillis();
        }

    }
}
