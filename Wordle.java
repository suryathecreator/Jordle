/*
 * File: Wordle.java
 * -----------------
 * This module is the starter file for the Wordle assignment.
 * 
 * Wordle is a web-based word game created and developed by Welsh software engineer 
 * Josh Wardle, and owned and published by The New York Times Company since 2022. 
 * Players have six attempts to guess a five-letter word, with feedback given for 
 * each guess in the form of colored tiles indicating when letters match or occupy 
 * the correct position. The mechanics are nearly identical to the 1955 pen-and-paper 
 * game Jotto and the television game show franchise Lingo. (Wikipedia)
 * 
 * Author: Surya Duraivenkatesh
 * Date: 11/13/2022
 */

/*
DECIDED NOT TO IMPORT:
import java.util.random.*;
import java.lang.Math.*;
import java.util.Arrays.*;
*/

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wordle {
    /* Private instance variables */
    private WordleGWindow gw;
    private static String word;
    private int currentRow = 0;
    private static HashMap<String, String> guessToClue = new HashMap<String, String>();
    private static String[] dictionary = WordleDictionary.FIVE_LETTER_WORDS;

    public void run() 
    {
        // DEBUG: useful for testing
        // word = "GLASS"; 
        word = chooseWord(); // choose random word
        gw = new WordleGWindow();
        gw.setCurrentRow(currentRow);
        gw.addEnterListener((s) -> enterAction(s));
    }

    /**
     * returns a random word from WordleDictionary.
     */
    public String chooseWord(){
        int randomWordIndex = (int)(Math.random()  * WordleDictionary.FIVE_LETTER_WORDS.length);
        return WordleDictionary.FIVE_LETTER_WORDS[randomWordIndex]; 
    }

    /*
    * Called when the user hits the RETURN key or clicks the ENTER button,
    * passing in the string of characters on the current row.
    */
    public void enterAction(String s) 
    {
        String annotatedGuess;
        List<String> possibleWords;
        /* 
        MILESTONE #1
        gw.setSquareLetter(WordleGWindow.N_ROWS - 6,WordleGWindow.N_COLS - 5, String.valueOf(word.charAt(0)));
        gw.setSquareLetter(WordleGWindow.N_ROWS - 6,WordleGWindow.N_COLS - 4, String.valueOf(word.charAt(1)));
        gw.setSquareLetter(WordleGWindow.N_ROWS - 6,WordleGWindow.N_COLS - 3, String.valueOf(word.charAt(2)));
        gw.setSquareLetter(WordleGWindow.N_ROWS - 6,WordleGWindow.N_COLS - 2, String.valueOf(word.charAt(3)));
        gw.setSquareLetter(WordleGWindow.N_ROWS - 6,WordleGWindow.N_COLS - 1, String.valueOf(word.charAt(4)));
        */
        if ((java.util.Arrays.asList(WordleDictionary.FIVE_LETTER_WORDS).contains(s.toLowerCase())) && (s.length() == 5))
        {
            annotatedGuess = getHint(s, word);
            // USED FOR DEBUGGING:
            /* 
            System.out.println("This is the correct word: " + word);
            */
            guessToClue.put(word, annotatedGuess);
            // USED FOR DEBUGGING:
            /* 
            System.out.println("This is word: " + word);
            System.out.println("This is guess s: " + s);
            System.out.println("This is annotatedGuess: " + annotatedGuess);
            */
            if (word.toUpperCase().equals(annotatedGuess))
            {   
                // Changes squares to rainbow colors when the user wins.
                for (int i = 0; i < currentRow; i++)
                {
                    gw.setSquareColor(i, WordleGWindow.N_COLS-1, Color.RED);
                    gw.setSquareLetter(i, WordleGWindow.N_COLS-1, ":");
                    gw.setSquareColor(i, WordleGWindow.N_COLS-2, Color.ORANGE);
                    gw.setSquareLetter(i, WordleGWindow.N_COLS-2, ")");
                    gw.setSquareColor(i, WordleGWindow.N_COLS-3, Color.MAGENTA);
                    gw.setSquareLetter(i, WordleGWindow.N_COLS-3, ":");
                    gw.setSquareColor(i, WordleGWindow.N_COLS-4, Color.CYAN);
                    gw.setSquareLetter(i, WordleGWindow.N_COLS-4, "D");
                    gw.setSquareColor(i, WordleGWindow.N_COLS-5, Color.BLUE);
                    gw.setSquareLetter(i, WordleGWindow.N_COLS-5, ":");
                }
                for (int i = 0; i < annotatedGuess.length(); i++)
                {
                    gw.setSquareColor(currentRow, i, WordleGWindow.CORRECT_COLOR);
                    gw.setSquareLetter(currentRow, i, String.valueOf(s.charAt(i)));
                    gw.setKeyColor(Character.toString(s.charAt(i)), WordleGWindow.CORRECT_COLOR);
                }
                gw.showMessage("Psst... delete your last guess and enter 'SURYA'.");
            }
            else 
            {
                for (int i = 0; i < annotatedGuess.length(); i++)
                {
                    if (annotatedGuess.charAt(i) == word.toUpperCase().charAt(i))
                    {
                        gw.setSquareColor(currentRow, i, WordleGWindow.CORRECT_COLOR);
                        gw.setSquareLetter(currentRow, i, String.valueOf(s.charAt(i)));
                        gw.setKeyColor(Character.toString(s.charAt(i)), WordleGWindow.CORRECT_COLOR);
                    }
                    else if (Character.isLowerCase(annotatedGuess.charAt(i)))
                    {
                        gw.setSquareColor(currentRow, i, WordleGWindow.PRESENT_COLOR);
                        gw.setSquareLetter(currentRow, i, String.valueOf(s.charAt(i)));
                        if (gw.getKeyColor(Character.toString(s.charAt(i))) != WordleGWindow.CORRECT_COLOR)
                        {
                            gw.setKeyColor(Character.toString(s.charAt(i)), WordleGWindow.PRESENT_COLOR);
                        }
                    }
                    else
                    {
                        gw.setSquareColor(currentRow, i, WordleGWindow.MISSING_COLOR);
                        gw.setSquareLetter(currentRow, i, String.valueOf(s.charAt(i)));
                        if ((gw.getKeyColor(Character.toString(s.charAt(i))) != WordleGWindow.PRESENT_COLOR) && (gw.getKeyColor(Character.toString(s.charAt(i))) != WordleGWindow.CORRECT_COLOR))
                        {
                            gw.setKeyColor(Character.toString(s.charAt(i)), WordleGWindow.MISSING_COLOR);
                        }
                    }
                }

                if (s.equals("SURYA"))
                {
                    new Wordle().run();
                }
                else if (currentRow == 5)
                {
                    gw.showMessage("The word was " + word.toUpperCase() + ". BTW.. enter 'SURYA'.");
                }
                else
                {
                    currentRow++;
                    gw.setCurrentRow(currentRow);
                }
            }
        }
        else
        {
            if (s.length() < WordleGWindow.N_COLS)
            {
                possibleWords = listOfPossibleWords(guessToClue, dictionary);
                if (!possibleWords.isEmpty())
                {
                    System.out.print("Possible Words: ");
                    System.out.println(possibleWords);
                }
            }
            else if (s.equals("SURYA"))
            {
                new Wordle().run();
            }
            else 
            {
                gw.showMessage("The given word does not exist.");
            }
        }
    }
    
    /**
     * @param guess the user's guess
     * @param word the secret word to be guessed
     * @return a String version of the hint where a capital letter 
     * represents a correct guess at the correct location, a lower
     * case letter represents a correct guess at the wrong location, 
     * and a '*' represents an incorrect letter (neither in the 
     * correct place nor a correct letter anywhere in the word)
     * 
     * You will use this helper method when coloring the squares.
     * It's also the crucial method that is tested in codePost.
     * 
     * Examples: 
     * word        = "CLASS"
     * guess       = "SASSY"
     * returns:      "sa*S*"
     * 
     * word        = "FLUFF"
     * guess       = "OFFER"
     * returns:      "*ff**"
     * 
     * word        = "STACK"
     * guess       = "TASTE"
     * returns:      "tas**"
     * 
     * word        = "MYTHS"
     * guess       = "HITCH"
     * returns:      "h*T**"
     * 
     */
    public static String getHint(String guess, String word)
    {
        word =  word.toUpperCase();
        guess = guess.toUpperCase();
        HashMap<Character, Integer> mapForAnno = new HashMap<Character, Integer>();
        HashMap<Character, Integer> mapForWord = new HashMap<Character, Integer>();
        int isItPresent = 0;
        String returnString = "";
        for (int i = 0; i < word.length(); i++)
        {
            if (word.charAt(i) == guess.charAt(i))
            {
                returnString = returnString + guess.charAt(i);
            }
            else
            {
                for (int j = 0; j < word.length(); j++)
                {
                    // USED WHEN DEBUGGING:
                    /* 
                    System.out.println("This is the word: " + word);
                    System.out.println("This is the guess: " + guess);
                    System.out.println("This is i: " + i);
                    System.out.println("This is j: " + j);
                    */
                    if (word.charAt(j) == guess.charAt(i))
                    {
                        returnString = returnString + guess.toLowerCase().charAt(i);
                        isItPresent = 1;
                        break;
                    }
                }
                if (isItPresent == 0)
                {
                    returnString = returnString + '*';
                }
            }
            isItPresent = 0;
        }
        
        for (int i = 0; i < word.length(); i++)
        {
            Character anno = returnString.charAt(i);
            Character wor = word.charAt(i);
            if (anno.charValue() == '*')
            {

            }
            else if (mapForAnno.containsKey(Character.toLowerCase(anno))) 
            {
                mapForAnno.put(Character.toLowerCase(anno), mapForAnno.get(Character.toLowerCase(anno)).intValue() + 1);
            }
            else 
            {
               mapForAnno.put(Character.toLowerCase(anno), 1);
            }
            if (mapForWord.containsKey(Character.toLowerCase(wor)))
            {
                mapForWord.put(Character.toLowerCase(wor), mapForWord.get(Character.toLowerCase(wor)).intValue() + 1);
            }
            else
            {
                mapForWord.put(Character.toLowerCase(wor), 1);
            }
        }

        StringBuilder string = new StringBuilder(returnString);
        int value;
        for (Character key: mapForAnno.keySet()) 
        {
            value = mapForAnno.get(key).intValue();
            if (value > 1)
            {
                if (value > mapForWord.get(key).intValue())
                {
                    // USED WHEN DEBUGGING:
                    /* 
                    System.out.println("This is the value:" + value);
                    System.out.println("This is the key value from Word map:" + mapForWord.get(key).intValue());
                    System.out.println("This is returnString:" + returnString);
                    */
                    do
                    {
                        // USED WHEN DEBUGGING:
                        /* 
                        System.out.println("This is value inside DOWHILE:" + value);
                        System.out.println("This is key.charval():" + key.charValue());
                        */
                        for (int j = string.length() - 1; j >= 0; j--)
                        {  
                            // USED WHEN DEBUGGING:
                            /* 
                            System.out.println(returnString);
                            */
                            if (string.charAt(j) == key.charValue())
                            {
                                string.setCharAt(j, '*');
                                value--;
                                break;
                            }
                        }       
                    }
                    while (value > mapForWord.get(key).intValue()); 
                }
            }
        }
    return string.toString();
    }

    /**
     * @param guessToClue a Map of guesses (so far) with their associated clues
     * @param dictionary the dictionary used for this Wordle
     * @return a List of all the possible words (given the guesses and clues so far)
     * 
     * example:  
     * guessToClue = {OMENS=**e**, HELMS=*el**, GRASP=G****, LYART=l****}
     * returns the following list of possible words: [glide, glued, guile]
     * 
     */
    public static List<String> listOfPossibleWords(Map<String, String> guessToClue, String[] dictionary) 
    {   
        List<String> possibleWords = new ArrayList<String>();
        boolean allValid;
        for (int i = 0; i < dictionary.length; i++)
        {
            allValid = true;
            for (String guess : guessToClue.keySet())
            {
                if (!getHint(guess, dictionary[i]).equals(guessToClue.get(guess)))
                {
                    allValid = false;
                    break;
                }
            }
            if (allValid == true)
            {
                possibleWords.add(dictionary[i]);
            }
        }
        return possibleWords;
    }

/* Startup code */
    public static void main(String[] args) 
    {
        new Wordle().run();
    }
}