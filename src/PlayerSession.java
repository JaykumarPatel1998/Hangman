import java.util.Arrays;

/*
PlayerSession class has been created to keep account of the player session entities like characters
guessed by the player and keeping record of the number of misses
 */
public class PlayerSession {

    /*
    CharacterPosition class has been created to keep track of correct guesses along with their position
     */
    public static class CharacterPosition {
        char c;
        int position;

        public char getC() {
            return c;
        }

        public void setC(char c) {
            this.c = c;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public CharacterPosition(char c, int position) {
            this.c = c;
            this.position = position;
        }

        @Override
        public String toString() {
            return "CharacterPosition{" +
                    "c=" + c +
                    ", position=" + position +
                    '}';
        }
    }
    private String currentWordToGuess;
    private CharacterPosition [] correctlyGuessedChars;
    private char [] incorrectlyGuessedChars = new char[100];
    private int Score;
    private String guessSequence;

    public CharacterPosition[] getCorrectlyGuessedChars() {
        return correctlyGuessedChars;
    }

    public void setCorrectlyGuessedChars(CharacterPosition[] correctlyGuessedChars) {
        this.correctlyGuessedChars = correctlyGuessedChars;
        this.setGuessSequence();
    }

    public char[] getIncorrectlyGuessedChars() {
        return incorrectlyGuessedChars;
    }

    public void setIncorrectlyGuessedChars(char incorrectlyGuessedChar) {
        for (int i = 0; i < this.incorrectlyGuessedChars.length; i++) {
            if (this.incorrectlyGuessedChars[i] == '\0') {
                this.incorrectlyGuessedChars[i] = incorrectlyGuessedChar;
                break;
            }
        }
    }

    public int getScore() {
        return Score;
    }

    public void setScore() {
        this.Score = ++this.Score;
    }

    public String getCurrentWordToGuess() {
        return currentWordToGuess;
    }

    public void setCurrentWordToGuess(String currentWordToGuess) {
        this.currentWordToGuess = currentWordToGuess;
    }

    public String getGuessSequence() {
        return guessSequence;
    }

    public void setGuessSequence() {
        char[] guessSeq = this.guessSequence.toCharArray();
        for (int i = 0; i < this.correctlyGuessedChars.length; i++) {
            if (this.correctlyGuessedChars[i] != null) {
                guessSeq[this.correctlyGuessedChars[i].position] =  this.correctlyGuessedChars[i].c;
            }
        }
        this.guessSequence = String.valueOf(guessSeq);
    }

    public PlayerSession(String currentWordToGuess) {
        this.currentWordToGuess = currentWordToGuess;
        //setting guess sequence or initializing guess sequence
        this.guessSequence = "*";
        for (int i = 1; i < currentWordToGuess.length(); i++) {
            this.guessSequence += '*';
        }
        correctlyGuessedChars = new CharacterPosition[currentWordToGuess.length()];
        this.Score = 0;
        System.out.println(this);
    }

    public void resetSession(String newWord) {
        this.setCurrentWordToGuess(newWord);
        this.guessSequence = "*";
        for (int i = 1; i < currentWordToGuess.length(); i++) {
            this.guessSequence += '*';
        }
        this.correctlyGuessedChars = new CharacterPosition[currentWordToGuess.length()];
        this.incorrectlyGuessedChars = new char[100];
    }

    @Override
    public String toString() {
        return "PlayerSession{" +
                "currentWordToGuess='" + currentWordToGuess + '\'' +
                ", correctlyGuessedChars=" + Arrays.toString(correctlyGuessedChars) +
                ", incorrectlyGuessedChars=" + Arrays.toString(incorrectlyGuessedChars) +
                ", Score=" + Score +
                ", guessSequence='" + guessSequence + '\'' +
                '}';
    }
}
