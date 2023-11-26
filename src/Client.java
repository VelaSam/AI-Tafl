import modele.ai.AlphaBeta;
import modele.helpers.Helpers;
import modele.plan_de_jeu.Board;
import modele.plan_de_jeu.Position;
import modele.plan_de_jeu.Tile;
import modele.plan_de_jeu.TileState;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


class Client {
    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        Board board = null; // Board vide.

        AlphaBeta alphaBeta = new AlphaBeta();

        try {
            MyClient = new Socket("localhost", 8888);

            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                char cmd = 0;

                cmd = (char)input.read();
                System.out.println(cmd);
                // Debut de la partie en joueur rouge
                if(cmd == '1'){
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    board = new Board(boardValues, TileState.ROUGE);
//                    System.out.println(board);

                    System.out.println(board.getPossibleMoves());

                    System.out.println("Nouvelle partie! Vous jouer rouge, entrez votre premier coup : ");
                    String move = null;
//                    move = console.readLine();

                    Map<Tile, ArrayList<Position>> moves = alphaBeta.getNextMoveAB(board);
                    Map.Entry<Tile, ArrayList<Position>> firstEntry  = moves.entrySet().iterator().next();
//                    System.out.println(moves);// Get the first entry
                    Tile firstTile = firstEntry.getKey(); // Get the first tile
                    Position firstPosition = moves.get(firstTile).get(0); // Get the first position
                    move = Helpers.getLetterValue(firstTile.getX()) + (firstTile.getY()+1) + " - "
                            + Helpers.getLetterValue(firstPosition.getX()) + (firstPosition.getY()+1);

                    System.out.println(move);
                    board.playMoveOnBoard(move);

//                    System.out.println(board);

                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
                // Debut de la partie en joueur Noir
                if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des rouge");
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");

                    board = new Board(boardValues, TileState.NOIR);
//                    System.out.println(board);

                    output.flush();

                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
//                    System.out.println("size :" + size);
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);
                    System.out.println("Dernier coup :"+ s);

                    board.playMoveOnBoard(s);


                    System.out.println(board.getPossibleMoves());
                    System.out.println("Entrez votre coup : ");
                    String move = null;
//                    move = console.readLine();

                    Map<Tile, ArrayList<Position>> moves = alphaBeta.getNextMoveAB(board);
                    Map.Entry<Tile, ArrayList<Position>> firstEntry  = moves.entrySet().iterator().next();
//                    System.out.println(moves);// Get the first entry
                    Tile firstTile = firstEntry.getKey(); // Get the first tile
                    Position firstPosition = moves.get(firstTile).get(0); // Get the first position
                    move = Helpers.getLetterValue(firstTile.getX()) + (firstTile.getY()+1) + " - "
                            + Helpers.getLetterValue(firstPosition.getX()) + (firstPosition.getY()+1);

                    System.out.println(move);
                    board.playMoveOnBoard(move);

//                    System.out.println(board);

                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // La partie est terminée
                if(cmd == '5'){
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer);
                    System.out.println("Partie Terminé. Le dernier coup joué est: "+s);
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }
}
