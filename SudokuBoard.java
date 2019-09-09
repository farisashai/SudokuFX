package FX.FXSudoku;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SudokuBoard {
    private static Random gen;
    private static List<String> easyBoards, mediumBoards, hardBoards;
    static {
        String[] boardFiles = {"0.txt","1.txt","2.txt","3.txt","5.txt"};
        gen = new Random();
        easyBoards = new ArrayList<>();
        mediumBoards= new ArrayList<>();
        hardBoards = new ArrayList<>();
        for (int i = 0; i < boardFiles.length; i++) {
            try (BufferedReader in = new BufferedReader(new FileReader("/Users/farisashai/Documents/everything/intellij/src/FX/FXSudoku/sudokuBoards/" + boardFiles[i]))) {
                String s;
                while ((s = in.readLine()) != null) {
                    if (i < 2)
                        easyBoards.add(s);
                    else if (i < 4)
                        mediumBoards.add(s);
                    else
                        hardBoards.add(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[][] getBoard (Difficulty difficulty) {
        return getBoard(difficulty == Difficulty.EASY ? easyBoards.get(gen.nextInt(easyBoards.size())) : difficulty == Difficulty.MEDIUM ? mediumBoards.get(gen.nextInt(mediumBoards.size())) : hardBoards.get(gen.nextInt(hardBoards.size())));
    }
    private static byte[][] getBoard(String board) {
        byte[][] arr = new byte[9][9];
        int index = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                arr[i][j] = Byte.parseByte(board.substring(index,1+index++));
        return arr;
    }
}
