package com.fourthwoods.firstgame.shapes;

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
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class Oval extends FaceShape {
	private double width = 60;
	private double height = 30;
	
	public Oval(Animation animation)
	{
		super(animation);
		face.setDx(dx);
		face.setDy(dy);
		face.setRotateSpeed(rotateSpeed);
		
		face.setWidth((int)(height * .4));
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.save();
		canvas.rotate((float)angle, (float)center.x, (float)center.y);
		canvas.translate((float)center.x, (float)center.y);
		canvas.scale((float)scale, (float)scale);
		
		ShapeDrawable d = new ShapeDrawable(new OvalShape());
		d.setBounds((int)-width / 2, (int)-height / 2, (int)width / 2, (int)height / 2);
		
		Paint p = d.getPaint();
		p.setColor(fillColor);
		p.setAlpha(alpha);
		p.setAntiAlias(true);

		d.draw(canvas);
		
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(borderWidth);
		p.setColor(borderColor);
		p.setAlpha(alpha);

		d.draw(canvas);
		canvas.restore();
		
		super.draw(canvas);
	}
	
	@Override
	public boolean hitTest(Point point)
	{
		return (Math.pow(point.x - center.x, 2.0) / Math.pow(width, 2.0) + Math.pow(point.y - center.y, 2.0) / Math.pow(height, 2.0)) <= 1;
	}
	
	private static double SQR(double d)
	{
		return d * d;
	}
	
	@Override
	public boolean hitTest(Line line) {
		// if one or both points are inside the ellipse then
		// return true.
		if(hitTest(line.getBegin()) || hitTest(line.getEnd()))
			return true;
		
	    double angleRad = Math.toRadians(angle);

	    double c = Math.cos(angleRad);
	    double s = Math.sin(angleRad);

		// Find the half lengths of the semi-major and semi-minor axes
		double dx = width / 2;
		double dy = height / 2;

		double a, b;
		
		if (dx >= dy) {
			a = dx;
			b = dy;
		} else {
			a = dy;
			b = dx;
		}

		// Find k1, k2, k3 - define when a point x,y is on the ellipse
		double k1 = SQR(c / a) + SQR(s / b);
		double k2 = 2 * s * c * ((1 / SQR(a)) - (1 / SQR(b)));
		double k3 = SQR(s / a) + SQR(c / b);

	    // public int [More ...] intersect(LineSegment seg, Point2D begin, Point2D
		// end) {

		Point begin = line.getBegin();
		Point end = line.getEnd();
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 0);
		
		// Solution is found by parameterizing the line segment and
		// substituting those values into the ellipse equation.
		// Results in a quadratic equation.

		double x1 = center.x;
		double y1 = center.y;
		double u1 = begin.x;
		double v1 = begin.y;
		double u2 = end.x;
		double v2 = end.y;
		dx = u2 - u1;
		dy = v2 - v1;
		double q0 = k1 * SQR(u1 - x1) + k2 * (u1 - x1) * (v1 - y1) + k3 * SQR(v1 - y1) - 1;
		double q1 = (2 * k1 * dx * (u1 - x1)) + (k2 * dx * (v1 - y1)) + (k2 * dy * (u1 - x1)) + (2 * k3 * dy * (v1 - y1));
		double q2 = (k1 * SQR(dx)) + (k2 * dx * dy) + (k3 * SQR(dy));

		// Compare q1^2 to 4*q0*q2 to see how quadratic solves
		double d = SQR(q1) - (4 * q0 * q2);

		if (d < 0) {
			// Roots are complex valued. Line containing the segment does
			// not intersect the ellipse

			return false;
		}

		if (d == 0) {
			// One real-valued root - line is tangent to the ellipse
			double t = -q1 / (2 * q2);
			if (0 <= t && t <= 1) {
				// Intersection occurs along line segment
				p1.x = u1 + t * dx;
				p1.y = v1 + t * dy;
				return line.contains(p1);
			} else
				return false;
		} else {
			// Two distinct real-valued roots. Solve for the roots and see if
			// they fall along the line segment

			int n = 0;
			double q = Math.sqrt(d);
			double t = (-q1 - q) / (2 * q2);

			if (0 <= t && t <= 1) {
				// Intersection occurs along line segment
				p1.x = u1 + t * dx;
				p1.y = v1 + t * dy;

				n++;
			}
			// 2nd root
			t = (-q1 + q) / (2 * q2);
			if (0 <= t && t <= 1) {
				if (n == 0) {
					p1.x = u1 + t * dx;
					p1.y = v1 + t * dy;

					n++;
				} else {
					p2.x = u1 + t * dx;
					p2.y = v1 + t * dy;

					n++;
				}
			}
			
			if(n == 1)
				return line.contains(p1);
			else
				return line.contains(p1) && line.contains(p2);
		}
	}
}
