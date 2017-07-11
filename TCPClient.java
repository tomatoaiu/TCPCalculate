import java.net.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TCPClient {

    public static void main(String args[]) {
        // arguments supply message and hostname
        Socket s = null; // クライアント用ソケット
        
        // 入力例表示
        System.out.println("\r\n入力例\r\n演算子(+, -, *, /)を入力して下さい。終了は(q)を入力してください。");
        System.out.println(" > +");
        System.out.println("一つ目の整数を入力して下さい。");
        System.out.println(" > 45");
        System.out.println("二つ目の整数を入力して下さい。");
        System.out.println(" > 90");
        System.out.println("結果：135.0\r\n\r\n\r\n");

        try {
            
            int serverPort = 7896;
            s = new Socket(args[0], serverPort); // クライアントソケット生成
                
            DataInputStream in = new DataInputStream(s.getInputStream()); // 流れる入力データ
            DataOutputStream out = new DataOutputStream(s.getOutputStream()); // 流れる出力データ
            
            while (true) {

                String operator = "", operandLeft = "", operandRight = "";
                try {
                    do {
                        System.out.println("演算子(+, -, *, /)を入力して下さい。終了は(q)を入力してください。");
                        operator = inputData();
                    } while (!operator.equals("+") && !operator.equals("-") && !operator.equals("*") && !operator.equals("/"));

                    do {
                        System.out.println("一つ目の整数を入力して下さい。");
                        operandLeft = inputData();
                    } while (!isNumber(operandLeft));

                    do {
                        System.out.println("二つ目の整数を入力して下さい。");
                        operandRight = inputData();
                    } while (!isNumber(operandRight));
                } catch (InputMismatchException e) { // 入力値の方が指定通りでない
                    System.out.println("型が違います：" + e);
                }
                // 3つの入力を[,]で区切りひとまとめにする
                String sendMsg = operator + "," + operandLeft + "," + operandRight;
                out.writeUTF(sendMsg);// UTF is a string encoding see Sn. 4.4 　args[0]を出力ストリームに書き込み
                String data = in.readUTF(); //  read a line of data from the stream　ストリームinから読み込み
                System.out.println("結果：" + data + "\r\n"); // 結果表示
            }
        } catch (UnknownHostException e) { // ホストのIPアドレスが判定できなかった場合
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) { // 入力の途中で、予想外のファイルの終了、または予想外のストリームの終了があった
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) { // なんらかの入出力例外の発生
            System.out.println("readline:" + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close(); // クライアントのソケット終了
                } catch (IOException e) { // なんらかの入出力例外の発生
                    System.out.println("close:" + e.getMessage());
                }
            }
        }
    }
    
    // キーボード入力
    public static String inputData() {
        System.out.print(" > ");
        Scanner scan = new Scanner(System.in); // キーボード入力
        String result = scan.next();
        if(result.equals("q")) System.exit(0); // [q]の場合システム終了
        return result; // scanをstringに
    }

    // 入力された値が整数か判定
    public static boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) { // 数値ではない
            return false;
        }
    }
}
