package com.aaron.rvitemdecorationdemo;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/5/27.
 */

public class ObservableSample {

    class NewspaterObservable extends Observable {

        public void notifyAllMan(String info) {
            setChanged();
            notifyObservers(info);
        }

    }

    class ManObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            System.out.println(arg);
        }
    }

    public void test() {
        NewspaterObservable observable = new NewspaterObservable();
        observable.addObserver(new ManObserver());
        observable.addObserver(new ManObserver());
        observable.notifyAllMan("have a newspater");
    }

    public static void main(String[] args) {
        new ObservableSample().test();
    }

}
