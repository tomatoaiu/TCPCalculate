import java.net.*;
import java.io.*;

public class TCPServer {
    
    public static void main(String args[]) {
        
        int cnt = 1; // クライアントの番号
        
        try {
            
            int serverPort = 7896; // the server port
            // サーバー用ソケット
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) { // サーバーを永遠に稼働
                 // ソケットに対する接続要求を待機し、それを受け取り
                Socket clientSocket = listenSocket.accept();
                 // Conectionクラスのスレッド生成
                Connection c = new Connection(clientSocket, cnt++);
            }
        } catch (IOException e) { // なんらかの入出力例外の発生
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}

class Connection extends Thread {

    DataInputStream in; // データ入力ストリーム
    DataOutputStream out; // データ出力ストリーム
    Socket clientSocket; // ソケットクライアント
    
    private int cnt; // クライアントの番号

    // 引数でクライアントのソケットを受け取る
    public Connection(Socket aClientSocket, int cnt) {
        
        this.cnt = cnt; // mainからクライアントの番号を受け取る
        
        try {
            // Connectionクラスのソケットにクライアントのソケットを入れる
            clientSocket = aClientSocket;
            // クライアントから受け取った入力ストリームをデータ入力ストリームに
            in = new DataInputStream(clientSocket.getInputStream());
            // クライアントから受け取った出力ストリームをデータ出力ストリームに
            out = new DataOutputStream(clientSocket.getOutputStream());
            // このスレッドの実行を開始し、このスレッドのrunメソッドを呼び出し。
            this.start();
        } catch (IOException e) { // なんらかの入出力例外の発生
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            // 受信表示
            System.out.println("Handling client at " +
                clientSocket.getInetAddress().getHostAddress() + " on port " +
                clientSocket.getPort());
            while(true) {

// read a line of data from the stream クライアントから送られてきたデータを
// utf-8でエンコードしString型に変換しdataに入れる
                String data = in.readUTF();
                // 受け取ったデータを[,]で分割
                String[] recvMsg = data.split(",", 0); 
                double sum = 0; // 合計

                switch (recvMsg[0]) { // 演算子の違いによる各計算
                    case "+": // 足し算
                        sum = Integer.parseInt(recvMsg[1])
                                + Integer.parseInt(recvMsg[2]);
                        break;
                    case "-": // 引き算
                        sum = Integer.parseInt(recvMsg[1])
                                - Integer.parseInt(recvMsg[2]);
                        break;
                    case "*": // 掛け算
                        sum = Integer.parseInt(recvMsg[1])
                                * Integer.parseInt(recvMsg[2]);
                        break;
                    case "/": // 割り算
                        sum = Integer.parseInt(recvMsg[1])
                                / (double) (Integer.parseInt(recvMsg[2]));
                        break;
                }

                // 結果の表示
                System.out.println("client : " + cnt + "   result:" + sum);
                // sting型の変数を修正utf-8でエンコードして出力ストリームに書き換え
                out.writeUTF(String.valueOf(sum));

            }
      // 入力の途中で、予想外のファイルの終了、または予想外のストリームの終了があった
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) { // なんらかの入出力例外の発生
            System.out.println("readline:" + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // 処理が終わったらソケットを削除
            } catch (IOException e) {/*close failed*/
            }
        }
    }
}
