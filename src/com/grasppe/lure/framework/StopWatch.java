/*
 * @(#)StopWatch.java   11/11/10
 * Copyright (c) 2011 Copyright © 2001, Sun Microsystems,Inc.. All rights reserved.
 * http://java.sun.com/docs/books/performance/1st_edition/html/JPMeasurement.fm.html
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.lure.framework;

/**
 * A class to help benchmark code
 * It simulates a real stop watch
 * Listing 3-3 shows how you can use this stopwatch class to measure a piece of code. Using this class is simpler than adding the timing code to each operation you want to measure and ensures that errors aren't introduced by the timing mechanism.
 * class TimeTest2 {
 *    public static void main(String[] args) {
 *       Stopwatch timer = new Stopwatch().start();
 *       long total = 0;
 *       for (int i = 0; i < 10000000; i++) {
 *          total += i;
 *       }
 *       timer.stop();
 *       System.out.println(timer.getElapsedTime());
 *    }
 * }
 */
public class StopWatch {

    private long	startTime = -1;
    private long	stopTime  = -1;
    private boolean	running   = false;

    /**
     * @return
     */
    public StopWatch reset() {
        startTime = -1;
        stopTime  = -1;
        running   = false;

        return this;
    }

    /**
     * @return
     */
    public StopWatch start() {
        startTime = System.currentTimeMillis();
        running   = true;

        return this;
    }

    /**
     * @return
     */
    public StopWatch stop() {
        stopTime = System.currentTimeMillis();
        running  = false;

        return this;
    }

    /**
     * returns elapsed time in milliseconds
     *  if the watch has never been started then
     *  return zero
     * @return
     */
    public long getElapsedTime() {
        if (startTime == -1) {
            return 0;
        }

        if (running) {
            return System.currentTimeMillis() - startTime;
        } else {
            return stopTime - startTime;
        }
    }
}
