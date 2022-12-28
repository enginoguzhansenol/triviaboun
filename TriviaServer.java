import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;


public class TriviaServer {

    // create a mutex object
    private static Semaphore mutex = new Semaphore(1);

    // shared data
    private static int responded = 0;


    // list of sample trivia questions
    static String[] QUESTIONS = {
            "Who is the most handsome professor of CMPE Department?",
            "What is most entertaining CC Class in CMPE Department?",
            "Which Erasmus student has a mustache?",
            "Which one is the novel that Ayse Kulin wrote?",
            "Which pronounciation is correct?",
            "Who is Alper Sen's favorite student?",
            "Who has a motorbike?",
            "Which Bogazici University Sports Team that Oguzhan play in?",
            "Which department is the only one that has its own building in North Campus?",
    };

    // list of answers to the sample trivia questions
    static String[] ANSWERS = {
            "Alper Sen",
            "CMPE436",
            "Joseph",
            "Kardelenler",
            "Safak",
            "Baki",
            "Erdinc",
            "Handball",
            "CMPE",
    };
    static String[] OPTIONS1 = {
            "Tuna Tugcu",
            "CMPE475",
            "Anton",
            "Alperler",
            "Sefik",
            "Berfin",
            "Alperen",
            "Basketball",
            "EE",
    };
    static String[] OPTIONS2 = {
            "Cem Ersoy",
            "CMPE480",
            "Niklas",
            "Berfinler",
            "Sufuk",
            "Emre",
            "Ilgaz",
            "Volleyball",
            "IE",

    };
    static String[] OPTIONS3 = {
            "Can Ozturan",
            "CMPE483",
            "Hamza",
            "Oguzhanlar",
            "Sofok",
            "Safak",
            "Safak",
            "Tennis",
            "CE",
    };
    static int correct1=0;
    static int correct2=0;
    public static void main(String[] args) throws IOException {
        // create a server socket to listen for client connections
        final String[] clientSentence = new String[1];
        ServerSocket welcomeSocket = new ServerSocket(8080);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        System.out.println("while dışında");
        Vector<String> usernames = new Vector<String>();

        while(true) {

            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Connection");
            executor.submit(()->{
                try{

                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(),true);

                    Vector<String> clientMsg = new Vector<String>();

                    while((clientSentence[0] = inFromClient.readLine()) != null) {

                        clientMsg.add(clientSentence[0]);

                        if(clientSentence[0].equals("END")) {
                            break;
                        }
                    }
                    for(String s: clientMsg) {
                        System.out.println(s);
                    }


                    if(clientMsg.get(0).equals("start")){ //starta basarsa
                        outToClient.println("Welcome " + clientMsg.get(1) + ". after each player press to the next button.");
                        usernames.add(clientMsg.get(1));


                    } else if(clientMsg.get(0).equals("9")){ //sorular biterse
                        if(correct1==3 && correct2==3){
                            outToClient.println("TIE!!");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                        }else if(correct1==3){
                            outToClient.println(usernames.get(0) + " won the game.");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                        }else if(correct2==3){
                            outToClient.println(usernames.get(1) + " won the game.");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                        }else{
                            outToClient.println("GAME OVER! " + usernames.get(0)+ " has " + correct1 +" correct answers, " + usernames.get(1) + " has " + correct2 + " correct answers.");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                            outToClient.println("GAME OVER");
                        }

                    }

                    //next e basarsa
                    else if(clientMsg.get(0).equals("0") || clientMsg.get(0).equals("1")||clientMsg.get(0).equals("2")||clientMsg.get(0).equals("3")||clientMsg.get(0).equals("4")|| clientMsg.get(0).equals("5")||clientMsg.get(0).equals("6")||clientMsg.get(0).equals("7")||clientMsg.get(0).equals("8")||clientMsg.get(0).equals("9")) {
                        try {
                            // acquire the mutex
                            mutex.acquire();

                            // access the shared data
                            responded++;

                            // release the mutex
                            mutex.release();
                        } catch (InterruptedException e) {
                            // handle exception
                        }
                        while (responded % 2 == 1){
                            Thread.sleep(1000);
                        }
                        if(usernames.size()==2){
                            if(correct1>=3 && correct2>=3){
                                outToClient.println("TIE!!");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                            }else if(correct1>=3){
                                outToClient.println(usernames.get(0) + " won the game.");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                            }else if(correct2>=3){
                                outToClient.println(usernames.get(1) + " won the game.");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                                outToClient.println("GAME OVER");
                            }

                            outToClient.println(    QUESTIONS[Integer.parseInt(clientMsg.get(0))]    );
                            outToClient.println(    ANSWERS[Integer.parseInt(clientMsg.get(0))]    );
                            outToClient.println(    OPTIONS1[Integer.parseInt(clientMsg.get(0))]    );
                            outToClient.println(    OPTIONS2[Integer.parseInt(clientMsg.get(0))]    );
                            outToClient.println(    OPTIONS3[Integer.parseInt(clientMsg.get(0))]    );
                        }else{
                            outToClient.println("Waiting for other users to join...");
                            outToClient.println("A");
                            outToClient.println("B");
                            outToClient.println("C");
                            outToClient.println("D");
                        }




                    }else{//şıkka basarsa

                        char chIndex = clientMsg.get(0).charAt(0) ;
                        int index = Integer.parseInt(String.valueOf(chIndex));
                        System.out.println("User in the " + index + "question");
                        String answer = clientMsg.get(0).substring(1);
                        if(answer.equals(ANSWERS[index-1])){
                            System.out.println("cevabı: " + clientMsg.get(0) + " answer: " + ANSWERS[index-1]);

                            if(clientMsg.get(1).equals(usernames.get(0))){
                                correct1++;
                                outToClient.println(correct1);
                                System.out.println(usernames.get(0) +"nin doğru cevabı arttı");
                            }
                            else if(clientMsg.get(1).equals(usernames.get(1))){
                                correct2++;
                                outToClient.println(correct2);
                                System.out.println(usernames.get(1) +"nin doğru cevabı arttı");
                            }


                        }else{
                            System.out.println("cevabı: " + clientMsg.get(0) + " answer: " + ANSWERS[index-1] + "coorect: " + correct1);
                            outToClient.println(correct1);
                        }
                    }
                    connectionSocket.close();
                }
                catch (IOException e) {
                    System.out.println("error");
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
        }
    }
}
