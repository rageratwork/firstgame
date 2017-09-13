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
import java.security.SecureRandom;
import java.util.Random;

import com.fourthwoods.firstgame.shapes.Circle;
import com.fourthwoods.firstgame.shapes.Decagon;
import com.fourthwoods.firstgame.shapes.Heart;
import com.fourthwoods.firstgame.shapes.Heptagon;
import com.fourthwoods.firstgame.shapes.Hexagon;
import com.fourthwoods.firstgame.shapes.Letter;
import com.fourthwoods.firstgame.shapes.Nonagon;
import com.fourthwoods.firstgame.shapes.Octagon;
import com.fourthwoods.firstgame.shapes.Oval;
import com.fourthwoods.firstgame.shapes.Parallelogram;
import com.fourthwoods.firstgame.shapes.Pentagon;
import com.fourthwoods.firstgame.shapes.Point;
import com.fourthwoods.firstgame.shapes.Rectangle;
import com.fourthwoods.firstgame.shapes.Shape;
import com.fourthwoods.firstgame.shapes.Square;
import com.fourthwoods.firstgame.shapes.Star;
import com.fourthwoods.firstgame.shapes.Trapezoid;
import com.fourthwoods.firstgame.shapes.Triangle;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;

public abstract class GameView extends SurfaceView {
	protected static final Random random = new SecureRandom();

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public abstract void advanceFrame();
	
	public abstract void render(Canvas canvas);

	protected Shape getNumber(Point p, Shape.Animation animation)
	{
		if(Settings.numbers_basic == false && Settings.numbers_intermediate == false)
			return null;
		
		Shape s = new com.fourthwoods.firstgame.shapes.Number(animation);
		s.setCenter(p);
		return s;
	}
	
	protected Shape getLetter(Point p, Shape.Animation animation)
	{
		if(Settings.letters_basic == false && Settings.letters_intermediate == false && Settings.letters_advanced == false)
			return null;
		
		Shape s = new Letter(animation);
		s.setCenter(p);
		return s;
	}
	
	protected Shape getShape(Point p, Shape.Animation animation)
	{
		int rand;
		Shape s;

		if(Settings.shapes_basic == false && Settings.shapes_intermediate == false && Settings.shapes_advanced == false)
			return null;
		
		do
		{
			rand = random.nextInt(15);
			if(Settings.shapes_basic == true && rand <= 4)
				break;
			else if(Settings.shapes_intermediate == true && rand > 4 && rand <= 9)
				break;
			else if(Settings.shapes_advanced == true && rand > 9)
				break;
		} while(true);
		
		switch(rand)
		{
		case 0:
			s = new Circle(animation);
			break;
		case 1:
			s = new Square(animation);
			break;
		case 2:
			s = new Triangle(animation);
			break;
		case 3:
			s = new Rectangle(animation);
			break;
		case 4:
			s = new Star(animation);
			break;
		case 5:
			s = new Heart(animation);
			break;
		case 6:
			s = new Oval(animation);
			break;
		case 7:
			s = new Parallelogram(animation);
			break;
		case 8:
			s = new Trapezoid(animation);
			break;
		case 9:
			s = new Pentagon(animation);
			break;
		case 10:
			s = new Hexagon(animation);
			break;
		case 11:
			s = new Heptagon(animation);
			break;
		case 12:
			s = new Octagon(animation);
			break;
		case 13:
			s = new Nonagon(animation);
			break;
		case 14:
		default:
			s = new Decagon(animation);
			break;
		}
		
		s.setCenter(p);
		return s;
	}
}
