import java.net.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TCPClient {

    public static void main(String args[]) {
        // arguments supply message and hostname
        Socket s = null; // �N���C�A���g�p�\�P�b�g
        
        // ���͗�\��
        System.out.println("\r\n���͗�\r\n���Z�q(+, -, *, /)����͂��ĉ������B�I����(q)����͂��Ă��������B");
        System.out.println(" > +");
        System.out.println("��ڂ̐�������͂��ĉ������B");
        System.out.println(" > 45");
        System.out.println("��ڂ̐�������͂��ĉ������B");
        System.out.println(" > 90");
        System.out.println("���ʁF135.0\r\n\r\n\r\n");

        try {
            
            int serverPort = 7896;
            s = new Socket(args[0], serverPort); // �N���C�A���g�\�P�b�g����
                
            DataInputStream in = new DataInputStream(s.getInputStream()); // �������̓f�[�^
            DataOutputStream out = new DataOutputStream(s.getOutputStream()); // �����o�̓f�[�^
            
            while (true) {

                String operator = "", operandLeft = "", operandRight = "";
                try {
                    do {
                        System.out.println("���Z�q(+, -, *, /)����͂��ĉ������B�I����(q)����͂��Ă��������B");
                        operator = inputData();
                    } while (!operator.equals("+") && !operator.equals("-") && !operator.equals("*") && !operator.equals("/"));

                    do {
                        System.out.println("��ڂ̐�������͂��ĉ������B");
                        operandLeft = inputData();
                    } while (!isNumber(operandLeft));

                    do {
                        System.out.println("��ڂ̐�������͂��ĉ������B");
                        operandRight = inputData();
                    } while (!isNumber(operandRight));
                } catch (InputMismatchException e) { // ���͒l�̕����w��ʂ�łȂ�
                    System.out.println("�^���Ⴂ�܂��F" + e);
                }
                // 3�̓��͂�[,]�ŋ�؂�ЂƂ܂Ƃ߂ɂ���
                String sendMsg = operator + "," + operandLeft + "," + operandRight;
                out.writeUTF(sendMsg);// UTF is a string encoding see Sn. 4.4 �@args[0]���o�̓X�g���[���ɏ�������
                String data = in.readUTF(); //  read a line of data from the stream�@�X�g���[��in����ǂݍ���
                System.out.println("���ʁF" + data + "\r\n"); // ���ʕ\��
            }
        } catch (UnknownHostException e) { // �z�X�g��IP�A�h���X������ł��Ȃ������ꍇ
            System.out.println("Socket:" + e.getMessage());
        } catch (EOFException e) { // ���͂̓r���ŁA�\�z�O�̃t�@�C���̏I���A�܂��͗\�z�O�̃X�g���[���̏I����������
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) { // �Ȃ�炩�̓��o�͗�O�̔���
            System.out.println("readline:" + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close(); // �N���C�A���g�̃\�P�b�g�I��
                } catch (IOException e) { // �Ȃ�炩�̓��o�͗�O�̔���
                    System.out.println("close:" + e.getMessage());
                }
            }
        }
    }
    
    // �L�[�{�[�h����
    public static String inputData() {
        System.out.print(" > ");
        Scanner scan = new Scanner(System.in); // �L�[�{�[�h����
        String result = scan.next();
        if(result.equals("q")) System.exit(0); // [q]�̏ꍇ�V�X�e���I��
        return result; // scan��string��
    }

    // ���͂��ꂽ�l������������
    public static boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) { // ���l�ł͂Ȃ�
            return false;
        }
    }
}
