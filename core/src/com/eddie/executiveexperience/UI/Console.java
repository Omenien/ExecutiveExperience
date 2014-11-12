package com.eddie.executiveexperience.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.eddie.executiveexperience.Entity.Saw;
import com.eddie.executiveexperience.Env;
import com.eddie.executiveexperience.Game;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console
{
    protected static final String regexPattern = "\\w+|\"[\\w\\s]*\"";
    protected boolean usingConsole;
    protected String consoleInput;
    protected Pattern pattern;
    protected Matcher matcher;

    public Console()
    {
        usingConsole = false;
        consoleInput = "";

        pattern = Pattern.compile(regexPattern);
    }

    private static String removeLastChar(String str)
    {
        return str.substring(0, str.length() - 1);
    }

    private String processConsoleInput()
    {
        String returnVal = "";

        matcher = pattern.matcher(consoleInput);

        ArrayList<String> matches = new ArrayList<>();

        while(matcher.find())
        {
            matches.add(matcher.group());
        }

        try
        {
            switch(matches.get(0).toLowerCase())
            {
                case "spawn":
                    switch(matches.get(1).toLowerCase())
                    {
                        case "saw":
                            float x = Float.parseFloat(matches.get(2));
                            float y = Float.parseFloat(matches.get(3));

                            returnVal = "Spawned saw at (" + x + ", " + y + ").";

                            if(Game.instance.getGameScreen().getGameStage() != null)
                            {
                                new Saw(Game.instance.getGameScreen().getGameStage(), x, y);
                            }
                            else
                            {
                                returnVal = "Error: I was going to spawn a Saw, but then I got high.";
                            }
                            break;

                        default:
                            returnVal = "Error: Invalid Entity type specified: " + matches.get(1) + " in " + consoleInput;
                            break;
                    }
                    break;

                default:
                    returnVal = "Error: Invalid input " + consoleInput;
                    break;
            }
        }
        catch(IndexOutOfBoundsException ex)
        {
            returnVal = "Error: Invalid number of arguments from input " + consoleInput;
        }
        catch(NumberFormatException ex)
        {
            returnVal = "Error: NaN " + ex.getMessage().toLowerCase() + " from input " + consoleInput;
        }

        return returnVal;
    }

    public String inputConsole(int keycode)
    {
        switch(keycode)
        {
            case Input.Keys.ENTER:
                String returnVal = processConsoleInput();
                if(returnVal.substring(0, 5).equals("Error"))
                {
                    Game.instance.getUI().writeText(returnVal, UI.TextType.ERROR);
                }
                else
                {
                    Gdx.app.log("Console", returnVal);
                }
                usingConsole = false;
                consoleInput = "";
                break;

            case Input.Keys.ESCAPE:
                usingConsole = false;
                consoleInput = "";
                break;

            case Input.Keys.BACKSPACE:
                consoleInput = removeLastChar(consoleInput);
                break;

            case Input.Keys.SPACE:
                consoleInput += " ";
                break;

            default:
                consoleInput += getKey(keycode, (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)));
                break;
        }

        return consoleInput;
    }

    public String getKey(int keycode, boolean shift)
    {
        String key = Input.Keys.toString(keycode).toLowerCase();

        if(shift)
        {
            switch(keycode)
            {
                case Input.Keys.A:
                case Input.Keys.B:
                case Input.Keys.C:
                case Input.Keys.D:
                case Input.Keys.E:
                case Input.Keys.F:
                case Input.Keys.G:
                case Input.Keys.H:
                case Input.Keys.I:
                case Input.Keys.J:
                case Input.Keys.K:
                case Input.Keys.L:
                case Input.Keys.M:
                case Input.Keys.N:
                case Input.Keys.O:
                case Input.Keys.P:
                case Input.Keys.Q:
                case Input.Keys.R:
                case Input.Keys.S:
                case Input.Keys.T:
                case Input.Keys.U:
                case Input.Keys.V:
                case Input.Keys.W:
                case Input.Keys.X:
                case Input.Keys.Y:
                case Input.Keys.Z:
                    key = Input.Keys.toString(keycode);
                    break;

                case Input.Keys.NUM_1:
                    key = "!";
                    break;

                case Input.Keys.NUM_2:
                    key = "@";
                    break;

                case Input.Keys.NUM_3:
                    key = "#";
                    break;

                case Input.Keys.NUM_4:
                    key = "$";
                    break;

                case Input.Keys.NUM_5:
                    key = "%";
                    break;

                case Input.Keys.NUM_6:
                    key = "^";
                    break;

                case Input.Keys.NUM_7:
                    key = "&";
                    break;

                case Input.Keys.NUM_8:
                    key = "*";
                    break;

                case Input.Keys.NUM_9:
                    key = "(";
                    break;

                case Input.Keys.NUM_0:
                    key = ")";
                    break;

                case Input.Keys.MINUS:
                    key = "_";
                    break;

                case Input.Keys.EQUALS:
                    key = "+";
                    break;

                case Input.Keys.LEFT_BRACKET:
                    key = "{";
                    break;

                case Input.Keys.RIGHT_BRACKET:
                    key = "}";
                    break;

                case Input.Keys.SEMICOLON:
                    key = ":";
                    break;

                case Input.Keys.APOSTROPHE:
                    key = "'";
                    break;

                case Input.Keys.COMMA:
                    key = "<";
                    break;

                case Input.Keys.PERIOD:
                    key = ">";
                    break;

                case Input.Keys.SLASH:
                    key = "?";
                    break;

                case Input.Keys.BACKSLASH:
                    key = "|";
                    break;

                case Input.Keys.GRAVE:
                    key = "~";
                    break;
            }
        }

        return key;
    }

    public boolean isUsingConsole()
    {
        return usingConsole;
    }

    public void enableConsole()
    {
        if(Env.debug)
        {
            usingConsole = true;
            consoleInput = "";
        }
    }
}
