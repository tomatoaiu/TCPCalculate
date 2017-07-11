import java.net.*;
import java.io.*;

public class TCPServer {
    
    public static void main(String args[]) {
        
        int cnt = 1; // �N���C�A���g�̔ԍ�
        
        try {
            
            int serverPort = 7896; // the server port
            // �T�[�o�[�p�\�P�b�g
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) { // �T�[�o�[���i���ɉғ�
                 // �\�P�b�g�ɑ΂���ڑ��v����ҋ@���A������󂯎��
                Socket clientSocket = listenSocket.accept();
                 // Conection�N���X�̃X���b�h����
                Connection c = new Connection(clientSocket, cnt++);
            }
        } catch (IOException e) { // �Ȃ�炩�̓��o�͗�O�̔���
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}

class Connection extends Thread {

    DataInputStream in; // �f�[�^���̓X�g���[��
    DataOutputStream out; // �f�[�^�o�̓X�g���[��
    Socket clientSocket; // �\�P�b�g�N���C�A���g
    
    private int cnt; // �N���C�A���g�̔ԍ�

    // �����ŃN���C�A���g�̃\�P�b�g���󂯎��
    public Connection(Socket aClientSocket, int cnt) {
        
        this.cnt = cnt; // main����N���C�A���g�̔ԍ����󂯎��
        
        try {
            // Connection�N���X�̃\�P�b�g�ɃN���C�A���g�̃\�P�b�g������
            clientSocket = aClientSocket;
            // �N���C�A���g����󂯎�������̓X�g���[�����f�[�^���̓X�g���[����
            in = new DataInputStream(clientSocket.getInputStream());
            // �N���C�A���g����󂯎�����o�̓X�g���[�����f�[�^�o�̓X�g���[����
            out = new DataOutputStream(clientSocket.getOutputStream());
            // ���̃X���b�h�̎��s���J�n���A���̃X���b�h��run���\�b�h���Ăяo���B
            this.start();
        } catch (IOException e) { // �Ȃ�炩�̓��o�͗�O�̔���
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            // ��M�\��
            System.out.println("Handling client at " +
                clientSocket.getInetAddress().getHostAddress() + " on port " +
                clientSocket.getPort());
            while(true) {

// read a line of data from the stream �N���C�A���g���瑗���Ă����f�[�^��
// utf-8�ŃG���R�[�h��String�^�ɕϊ���data�ɓ����
                String data = in.readUTF();
                // �󂯎�����f�[�^��[,]�ŕ���
                String[] recvMsg = data.split(",", 0); 
                double sum = 0; // ���v

                switch (recvMsg[0]) { // ���Z�q�̈Ⴂ�ɂ��e�v�Z
                    case "+": // �����Z
                        sum = Integer.parseInt(recvMsg[1])
                                + Integer.parseInt(recvMsg[2]);
                        break;
                    case "-": // �����Z
                        sum = Integer.parseInt(recvMsg[1])
                                - Integer.parseInt(recvMsg[2]);
                        break;
                    case "*": // �|���Z
                        sum = Integer.parseInt(recvMsg[1])
                                * Integer.parseInt(recvMsg[2]);
                        break;
                    case "/": // ����Z
                        sum = Integer.parseInt(recvMsg[1])
                                / (double) (Integer.parseInt(recvMsg[2]));
                        break;
                }

                // ���ʂ̕\��
                System.out.println("client : " + cnt + "   result:" + sum);
                // sting�^�̕ϐ����C��utf-8�ŃG���R�[�h���ďo�̓X�g���[���ɏ�������
                out.writeUTF(String.valueOf(sum));

            }
      // ���͂̓r���ŁA�\�z�O�̃t�@�C���̏I���A�܂��͗\�z�O�̃X�g���[���̏I����������
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) { // �Ȃ�炩�̓��o�͗�O�̔���
            System.out.println("readline:" + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // �������I�������\�P�b�g���폜
            } catch (IOException e) {/*close failed*/
            }
        }
    }
}
