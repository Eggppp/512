package com.eggp.test.xiancheng;

public class SequenceA implements Sequence {
    private static int number =0;

    public int getNumber() {
        number=number+1;
        return number;
    }

    public static void main(String[] args) {
        Sequence sequence=new SequenceA();
        ClientThread c1=new ClientThread(sequence);
        ClientThread c2=new ClientThread(sequence);
        ClientThread c3=new ClientThread(sequence);

        c1.start();
        c2.start();
        c3.start();
    }
}
