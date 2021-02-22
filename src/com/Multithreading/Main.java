package com.Multithreading;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {

    // сложение чисел от мин до мах

    static long numbOfOperator = 1_000_000L; // число до которого будем считать

    static int numbOfThraeds = Runtime.getRuntime().availableProcessors(); // кол-во ядер процесора

    public static void main(String[] args) {
        System.out.println("Кол-во ядер: "+numbOfThraeds);
        System.out.println(new Date()); // вывод времени
        ForkJoinPool forkJoinPool = new ForkJoinPool(numbOfThraeds); // формируем Pool потоков, равный кол-ву ядер
        System.out.println(forkJoinPool.invoke(new MyWork(0, numbOfOperator))); //запускаем потоки
        System.out.println(new Date());
    }

    static class MyWork extends RecursiveTask<Long> {
        long from, to;

        public MyWork(long from, long to){
            this.from =from;
            this.to=to;
        }

        @Override
        protected Long compute() {
            if ((to-from)<=numbOfOperator/numbOfThraeds){
                long j=0;
                for(long i = from; i<=to; i++){
                    j+=i;
                }
                return j;
            }else {
                long middle = (to+from)/2;
                MyWork mw1 = new MyWork(from, middle); //создаение 1 потока от мин до х
                mw1.fork(); // вызывает метод разбиения
                MyWork mw2 = new MyWork(middle+1, to); //создаение 2 потока от х до мах
                long sum = mw1.compute()+mw2.compute(); // делает рекурсию для классов
                mw1.join(); // запускает поток, до смерти
                return sum;
            }
        }
    }
}
