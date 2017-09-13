package com.fourthwoods.firstgame;

/**
 * Copyright (c) 2013 David J. Rager
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
import android.graphics.Canvas;

public class GameThread extends Thread {
	private final static int MAX_FPS = 50;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;

	GameView view;

	GameThread(GameView view) {
		this.view = view;
	}

	// flag to hold game state
	private boolean running;

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		Canvas canvas;
		long beginTime; // the time when the cycle begun
		long timeDiff; // the time it took for the cycle to execute
		int sleepTime; // ms to sleep (<0 if we're behind)
		int framesSkipped; // number of frames being skipped

		sleepTime = 0;

		while (running) {
			if(view.isShown() == false)
			{
				try {
					Thread.sleep(FRAME_PERIOD);
				} catch (InterruptedException e) {
				}

				continue;
			}
			
			beginTime = System.currentTimeMillis();
			canvas = null;
			framesSkipped = 0;
			
			view.advanceFrame();
			
			try {
				canvas = view.getHolder().lockCanvas();
				view.render(canvas);
			} finally {
				if (canvas != null)
					view.getHolder().unlockCanvasAndPost(canvas);
			}

			timeDiff = System.currentTimeMillis() - beginTime;
			sleepTime = (int) (FRAME_PERIOD - timeDiff);

			while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
				beginTime = System.currentTimeMillis();
				view.advanceFrame();

				framesSkipped++;
				
				timeDiff = FRAME_PERIOD - (System.currentTimeMillis() - beginTime);
				sleepTime += timeDiff;
			}

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
