package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static java.lang.reflect.Array.*;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;

    //incremental word length variable
    private static int wordLength = DEFAULT_WORD_LENGTH;

    //Random class to generate Random numbers
    private Random random = new Random();

    //data structures to hold information
    HashSet<String> wordSet = new HashSet<String>();
    ArrayList<String> wordList = new ArrayList<String>();
    HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<>();


    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        ArrayList<String> wordMapList;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            //fill sizeToWords HashMap
            if(sizeToWord.containsKey(word.length())){
                wordMapList = sizeToWord.get(word.length());
                wordMapList.add(word);
                sizeToWord.put(word.length(),wordMapList);
            } else{
                ArrayList<String> newWordList =  new ArrayList<>();
                newWordList.add(word);
                sizeToWord.put(word.length(),newWordList);
            }

            ArrayList<String> sortedList = new ArrayList<String>();
            String sortedWord = sortLetters(word);

          //fill lettersToWord HashMap
            if(!(lettersToWord.containsKey((sortedWord)))){
                sortedList.add(word);
                lettersToWord.put(sortedWord,sortedList);
            } else {
                sortedList = lettersToWord.get(sortedWord);
                sortedList.add(word);
                lettersToWord.put(sortedWord,sortedList);
            }
        }
    }

    //check for validity of the word
    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !(base.contains(word))){
            return  true;
        } else{
            return false;
        }
    }

    //fetching anagrams of the entered word
    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> temp;
        String anagram = sortLetters(targetWord);
        if(lettersToWord.containsKey(anagram)) {
            temp = lettersToWord.get(anagram);

            for(int i=0;i<temp.size();i++) {
                if(!(temp.get(i).contains(targetWord))) {
                    result.add(temp.get(i));
                }
            }
        }
        return result;
    }

    //fetching anagrams of the input word with one more letter
    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> temp;
        ArrayList<String> result = new ArrayList<String>();

        for(char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String anagram = word + alphabet;
            String sortedAnagram = sortLetters(anagram);

            if(lettersToWord.containsKey(sortedAnagram)) {
                temp = lettersToWord.get(sortedAnagram);

                for(int i=0;i<temp.size();i++) {
                    if(!(temp.get(i).contains(word))) {
                        result.add(temp.get(i));
                    }
                }
            }
        }
        return result;
    }

    //fetching anagrams of the input word with two more letters
    public ArrayList<String> getAnagramsWithTwoMoreLetters(String word){
        ArrayList<String> temp;
        ArrayList<String> result = new ArrayList<String>();

        for(char alphabetOne = 'a'; alphabetOne <= 'z'; alphabetOne++) {
            for(char alphabetTwo = 'a'; alphabetTwo <= 'z'; alphabetTwo++) {
                String anagram = word + alphabetOne + alphabetTwo;
                String sortedAnagram = sortLetters(anagram);

                if(lettersToWord.containsKey(sortedAnagram)) {
                    temp = lettersToWord.get(sortedAnagram);

                    for(int i=0;i<temp.size();i++){
                        if(!(temp.get(i).contains(word))) {
                            result.add(temp.get(i));
                        }
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int randomNumber;
        String starterWord;

        do{
            randomNumber = random.nextInt(sizeToWord.get(wordLength).size());
            starterWord = sizeToWord.get(wordLength).get(randomNumber);
        }while (getAnagramsWithOneMoreLetter(starterWord).size() < MIN_NUM_ANAGRAMS);

        if(wordLength < MAX_WORD_LENGTH){
            wordLength++;
        }
        return starterWord;
    }

    public String sortLetters(String word){
        char[] characters = word.toCharArray();
        Arrays.sort(characters);
        String sortedWord = new String(characters);
        return sortedWord;
    }
}


