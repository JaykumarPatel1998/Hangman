/*
Authors: Group3
    -1 Jaykumar Patel
    -2 Aditya Negandhi
    -3 Rishi Patel
    -4 Yuvraj Singh
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class HangmanApplication {
    static File hangmanFile = new File("src/hangman.txt");
    static String [] hangmanWords = new String[100];

    static final String NOT_CHARACTER_ERROR = "Your guess is invalid, please enter only one alphabet at a time";
    public static void main(String[] args) throws IOException {
        PlayerSession session = initiateGame();

        //GUI development
        JFrame frame = new JFrame("Hangman");
        frame.setSize(600, 355);

        JLabel scoreNameLabel = new JLabel("Score : ");
        JLabel scoreLabel = new JLabel("0");
        JPanel scorePanel = new JPanel();
        scorePanel.add(scoreNameLabel);
        scorePanel.add(scoreLabel);

        JLabel guessLabel = new JLabel("type a guess : ");
        JTextField guessOptionLabel = new JTextField(3);
        JPanel guessPanel = new JPanel();
        guessPanel.add(guessLabel);
        guessPanel.add(guessOptionLabel);

        JButton guessButton = new JButton("Guess");
        JPanel operationsPanel = new JPanel();
        operationsPanel.add(guessButton);

        JLabel resultLabel = new JLabel();
        JPanel resultPanel = new JPanel();
        resultPanel.add(resultLabel);

        JLabel guessSequenceHeader = new JLabel("word is : ");
        JLabel guessSequenceLabel = new JLabel(session.getGuessSequence());
        JPanel guessSequencePanel = new JPanel();
        guessSequencePanel.add(guessSequenceHeader);
        guessSequencePanel.add(guessSequenceLabel);

        JPanel mainPanel = new JPanel();
        mainPanel.add(scorePanel);
        mainPanel.add(guessPanel);
        mainPanel.add(operationsPanel);
        mainPanel.add(guessSequencePanel);
        mainPanel.add(resultPanel);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        frame.add(mainPanel);

        frame.setDefaultCloseOperation(3); //EXIT_ON_CLOSE resolves to 3
        frame.setVisible(true);

        ActionListener guessActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSession(session, guessOptionLabel, resultLabel, scoreLabel, guessSequenceLabel, frame, mainPanel);
            }

        };
        guessButton.addActionListener(guessActionListener);
    }

    public static void handleSession(PlayerSession session, JTextField guessOptionLabel, JLabel resultLabel, JLabel scoreLabel, JLabel guessSequenceLabel, JFrame frame, JPanel mainPanel) {
        resultLabel.setText("");

        char guessedChar = '\0';
        if (!guessOptionLabel.getText().isBlank()) {
            guessedChar = guessOptionLabel.getText().toCharArray()[0];
        }
        if (guessOptionLabel.getText().isBlank() || !Utils.isAnAlphabet(guessOptionLabel.getText()) || guessOptionLabel.getText().length() >1) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText(NOT_CHARACTER_ERROR);
        }
        else {
            //current word to guess string has been converted to char [] for easier manipulation of data
            char [] currentWord = session.getCurrentWordToGuess().toCharArray();
            boolean isWrongGuessFlag = true;
            if (!checkAlreadyGuessed(session, guessedChar)) {
                //this loop will loop through the character array(created for the current word to guess) and check for matches
                for (int i = 0; i < currentWord.length; i++) {
                    if (currentWord[i] == guessedChar) {
                        isWrongGuessFlag = false;
                        PlayerSession.CharacterPosition[] correctGuesses = session.getCorrectlyGuessedChars();
                        for (int j = 0; j < correctGuesses.length; j++) {
                            if (correctGuesses[j] == null) {
                                correctGuesses[j] = new PlayerSession.CharacterPosition(guessedChar, i);
                                break;
                            }
                        }
                        session.setCorrectlyGuessedChars(correctGuesses);
                        System.out.println(correctGuesses);
                    }
                }

            }

            if(isWrongGuessFlag) {
                resultLabel.setForeground(Color.RED);
                session.setIncorrectlyGuessedChars(guessedChar);

                String incorrectGuesses = "";
                for (int i = 0; i < session.getIncorrectlyGuessedChars().length; i++) {
                    if (session.getIncorrectlyGuessedChars()[i] == '\0') {
                        break;
                    }
                    incorrectGuesses += session.getIncorrectlyGuessedChars()[i] + ", ";
                }
                resultLabel.setText("you guessed the following words incorrectly : " + incorrectGuesses);
            }

            else {
                resultLabel.setForeground(Color.GREEN);
                resultLabel.setText("Your guess is : " + guessOptionLabel.getText());
                guessSequenceLabel.setText(session.getGuessSequence());

                //we only want to run the matching algorithm only and only if our guess sequence contain an empty space to guess, otherwise we end game
                if (!session.getGuessSequence().contains("*")) {
                    resultLabel.setForeground(Color.GREEN);
                    resultLabel.setText("you guessed the word correctly : " + session.getGuessSequence());
                    System.out.println("you guessed the word correctly : " + session.getGuessSequence());

                    JLabel userLabel = new JLabel("Please add a string to our existing game dictionary");
                    JTextField userInputLabel = new JTextField(10);
                    JPanel userPanel = new JPanel();
                    userPanel.add(userLabel);
                    userPanel.add(userInputLabel);

                    JButton okButton = new JButton("Add");
                    JPanel okPanel = new JPanel();
                    okPanel.add(okButton);

                    mainPanel.add(userPanel);
                    mainPanel.add(okPanel);

                    ActionListener userActionListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writeToFile(userInputLabel.getText());
                                mainPanel.remove(userPanel);
                                mainPanel.remove(okPanel);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    };
                    okButton.addActionListener(userActionListener);

                    resetGUIAndSession(session, guessOptionLabel, resultLabel, scoreLabel, guessSequenceLabel);
                }
            }
        }
    }

    public static boolean checkAlreadyGuessed(PlayerSession session, char guessedChar) {
        for (int i = 0; i < session.getCorrectlyGuessedChars().length; i++) {
            if((session.getCorrectlyGuessedChars()[i] != null) && (session.getCorrectlyGuessedChars()[i].c == guessedChar)) {
                return true;
            }
        }
        return false;
    }
    public static void resetGUIAndSession(PlayerSession session, JTextField guessOptionLabel, JLabel resultLabel, JLabel scoreLabel, JLabel guessSequenceLabel) {

        session.resetSession(pickARandomWord());
        session.setScore();
        scoreLabel.setText(Integer.toString(session.getScore()));
        guessSequenceLabel.setText(session.getGuessSequence());
        guessOptionLabel.setText("");
    }

    public static PlayerSession initiateGame() throws IOException {
        readFile();
        return new PlayerSession(pickARandomWord());
    }

    public static void readFile() throws IOException {
        String word;
        BufferedReader reader = new BufferedReader(new FileReader(hangmanFile));

        while ((word = reader.readLine()) != null) {
            Utils.addElementToAnArray(hangmanWords, word);
        }
    }

    public static void writeToFile(String str) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(hangmanFile, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.write("\n"+str);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String pickARandomWord() {
        Random random = new Random();
        int randomNumber = random.nextInt(10); // Generates a random integer between 0 (inclusive) and 10 (exclusive)
        System.out.println(Arrays.toString(hangmanWords));
        return hangmanWords[randomNumber];
    }
}