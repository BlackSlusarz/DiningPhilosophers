//Problem 5 filozofow i wystepujace w nim zakleszczenie


import java.util.*;
public class Philosopher extends Thread {
    public static final int numberOfThreads = 5;
    public static Object[] listOfLocks = new Object[numberOfThreads];
    public static char[] dinerTable = new char[4*numberOfThreads];
    public static char[] lockedDiner = new char[4*numberOfThreads];
    public static Random randomGenerator = new Random();
    public static int unitOfTime = 500;
    private int threadIndex;
    public static void main(String[] a) {
        for (int i=0; i<numberOfThreads; i++) listOfLocks[i] =
                new Object();
        // '|' Widelec
        // '-' Bez zadnego widelca, mysli
        // '=' Z jednym widelcem
        // 'o' z dwoma widelcami, je posilek
        for (int i=0; i<numberOfThreads; i++) {
            dinerTable[4*i] = '|';
            dinerTable[4*i+1] = ' ';
            dinerTable[4*i+2] = '-';
            dinerTable[4*i+3] = ' ';
            lockedDiner[4*i] = ' ';
            lockedDiner[4*i+1] = '|';
            lockedDiner[4*i+2] = '=';
            lockedDiner[4*i+3] = ' ';
        }
        for (int i=0; i<numberOfThreads; i++) {
            Thread t = new Philosopher(i);
            t.setDaemon(true);
            t.start();
        }
        String lockedString = new String(lockedDiner);
        System.out.println("Stolik dla filozofow:");
        long step = 0;
        while (true) {
            step++;
            System.out.println((new String(dinerTable))+"   "+step);
            if (lockedString.equals(new String(dinerTable)))
                break;
            try {
                Thread.sleep(unitOfTime);
            } catch (InterruptedException e) {
                System.out.println("Przerwano.");
            }
        }
        System.out.println("Nastapilo zakleszczenie. Kazdy z filozofow ma w rece jeden widelec.");
    }
    public Philosopher(int i) {
        threadIndex = i;
    }
    public void run() {
        while (!isInterrupted()) {
            try {
                sleep(unitOfTime*randomGenerator.nextInt(6));
            } catch (InterruptedException e) {
                break;
            }
            // Bierze widelec po lewej
            Object leftLock = listOfLocks[threadIndex];
            synchronized (leftLock) {
                int i = 4*threadIndex;
                dinerTable[i] = ' ';
                dinerTable[i+1] = '|';
                dinerTable[i+2] = '=';
                try {
                    sleep(unitOfTime*1);
                } catch (InterruptedException e) {
                    break;
                }
                // Bierze widelec po prawej
                Object rightLock =
                        listOfLocks[(threadIndex+1)%numberOfThreads];
                synchronized (rightLock) {
                    dinerTable[i+2] = 'o';
                    dinerTable[i+3] = '|';
                    dinerTable[(i+4)%(4*numberOfThreads)] = ' ';
                    try {
                        sleep(unitOfTime*1);
                    } catch (InterruptedException e) {
                        break;
                    }
                    dinerTable[i] = '|';
                    dinerTable[i+1] = ' ';
                    dinerTable[i+2] = '-';
                    dinerTable[i+3] = ' ';
                    dinerTable[(i+4)%(4*numberOfThreads)] = '|';
                }
            }
        }
    }
}