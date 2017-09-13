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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.util.SparseIntArray;

import com.fourthwoods.firstgame.shapes.Circle;
import com.fourthwoods.firstgame.shapes.Decagon;
import com.fourthwoods.firstgame.shapes.Heart;
import com.fourthwoods.firstgame.shapes.Heptagon;
import com.fourthwoods.firstgame.shapes.Hexagon;
import com.fourthwoods.firstgame.shapes.Nonagon;
import com.fourthwoods.firstgame.shapes.Octagon;
import com.fourthwoods.firstgame.shapes.Oval;
import com.fourthwoods.firstgame.shapes.Parallelogram;
import com.fourthwoods.firstgame.shapes.Pentagon;
import com.fourthwoods.firstgame.shapes.Rectangle;
import com.fourthwoods.firstgame.shapes.Square;
import com.fourthwoods.firstgame.shapes.Star;
import com.fourthwoods.firstgame.shapes.Trapezoid;
import com.fourthwoods.firstgame.shapes.Triangle;

public class Sounds {
	public static SparseIntArray pop = new SparseIntArray();
	public static SparseIntArray giggles = new SparseIntArray();
	public static SparseIntArray cheers = new SparseIntArray();
	public static SparseIntArray yes = new SparseIntArray();
	public static SparseIntArray no = new SparseIntArray();
	public static SparseIntArray karaColors = new SparseIntArray();
	@SuppressWarnings("rawtypes")
	public static Map<Class, Integer> karaShapes = new HashMap<Class, Integer>();
	public static SparseIntArray karaNumbers = new SparseIntArray();
	public static SparseIntArray karaLetters = new SparseIntArray();
	public static SparseIntArray lexieColors = new SparseIntArray();
	@SuppressWarnings("rawtypes")
	public static Map<Class, Integer> lexieShapes = new HashMap<Class, Integer>();
	public static SparseIntArray lexieNumbers = new SparseIntArray();
	public static SparseIntArray lexieLetters = new SparseIntArray();
	
	public static List<MediaPlayer> old = new ArrayList<MediaPlayer>();
	public static List<MediaPlayer> remove = new ArrayList<MediaPlayer>();
	
	public static void reload(Context context)
	{
		pop.put(0, R.raw.pop_1);
		pop.put(1, R.raw.pop_2);
		pop.put(2, R.raw.pop_3);

		giggles.put(0, R.raw.ethan_giggle_1);
		giggles.put(1, R.raw.ethan_giggle_2);
		giggles.put(2, R.raw.kara_giggle_1);
		giggles.put(3, R.raw.kara_giggle_2);
		giggles.put(4, R.raw.kara_giggle_3);
		giggles.put(5, R.raw.kara_giggle_4);
		giggles.put(6, R.raw.lexie_giggle_1);
		giggles.put(7, R.raw.lexie_giggle_2);
		giggles.put(8, R.raw.lexie_giggle_3);

		cheers.put(0, R.raw.kara_hooray);
		cheers.put(1, R.raw.kara_moo);
		cheers.put(2, R.raw.kara_wee);
		cheers.put(3, R.raw.kara_woohoo);
		cheers.put(4, R.raw.kara_yahoo);
		cheers.put(5, R.raw.kara_yeehaw);
		cheers.put(6, R.raw.kara_yippee);
		cheers.put(7, R.raw.lexie_hooray);
		cheers.put(8, R.raw.lexie_wee);
		cheers.put(9, R.raw.lexie_woohoo);
		cheers.put(10, R.raw.lexie_yahoo);
		cheers.put(11, R.raw.lexie_yippee);
		
		yes.put(0, R.raw.kara_good_job);
		yes.put(1, R.raw.kara_thats_it);
		yes.put(2, R.raw.kara_you_found_it);
		yes.put(3, R.raw.kara_you_got_that_one);
		yes.put(4, R.raw.lexie_good_job);
		yes.put(5, R.raw.lexie_thats_it);
		yes.put(6, R.raw.lexie_you_found_it);
		yes.put(7, R.raw.lexie_you_got_that_one);
		
		no.put(0, R.raw.kara_nope);
		no.put(1, R.raw.kara_not_that_one);
		no.put(2, R.raw.kara_sorry);
		no.put(3, R.raw.kara_try_again);
		no.put(4, R.raw.kara_wrong_one);
		no.put(5, R.raw.lexie_nope);
		no.put(6, R.raw.lexie_not_that_one);
		no.put(7, R.raw.lexie_oops);
		no.put(8, R.raw.lexie_sorry);
		no.put(8, R.raw.lexie_try_again);
		no.put(9, R.raw.lexie_wrong_one);
		
		karaColors.put(Color.WHITE, R.raw.kara_white);
		karaColors.put(0xff9513df, R.raw.kara_purple);
		karaColors.put(0xffffad54, R.raw.kara_orange);
		karaColors.put(Color.BLUE, R.raw.kara_blue);
		karaColors.put(Color.RED, R.raw.kara_red);
		karaColors.put(Color.YELLOW, R.raw.kara_yellow);
		karaColors.put(Color.GREEN, R.raw.kara_green);
		karaColors.put(Color.GRAY, R.raw.kara_grey);
		
		karaShapes.put(Circle.class, R.raw.kara_circle);
		karaShapes.put(Square.class, R.raw.kara_square);
		karaShapes.put(Triangle.class, R.raw.kara_triangle);
		karaShapes.put(Rectangle.class, R.raw.kara_rectangle);
		karaShapes.put(Star.class, R.raw.kara_star);
		karaShapes.put(Heart.class, R.raw.kara_heart);
		karaShapes.put(Oval.class, R.raw.kara_oval);
		karaShapes.put(Parallelogram.class, R.raw.kara_parallelogram);
		karaShapes.put(Trapezoid.class, R.raw.kara_trapezoid);
		karaShapes.put(Pentagon.class, R.raw.kara_pentagon);
		karaShapes.put(Hexagon.class, R.raw.kara_hexagon);
		karaShapes.put(Heptagon.class, R.raw.kara_heptagon);
		karaShapes.put(Octagon.class, R.raw.kara_octagon);
		karaShapes.put(Nonagon.class, R.raw.kara_nonagon);
		karaShapes.put(Decagon.class, R.raw.kara_decagon);

		karaNumbers.put(0, R.raw.kara_zero);
		karaNumbers.put(1, R.raw.kara_one);
		karaNumbers.put(2, R.raw.kara_two);
		karaNumbers.put(3, R.raw.kara_three);
		karaNumbers.put(4, R.raw.kara_four);
		karaNumbers.put(5, R.raw.kara_five);
		karaNumbers.put(6, R.raw.kara_six);
		karaNumbers.put(7, R.raw.kara_seven);
		karaNumbers.put(8, R.raw.kara_eight);
		karaNumbers.put(9, R.raw.kara_nine);
		karaNumbers.put(10, R.raw.kara_ten);
		karaNumbers.put(11, R.raw.kara_eleven);
		karaNumbers.put(12, R.raw.kara_twelve);
		karaNumbers.put(13, R.raw.kara_thirteen);
		karaNumbers.put(14, R.raw.kara_fourteen);
		karaNumbers.put(15, R.raw.kara_fifteen);
		karaNumbers.put(16, R.raw.kara_sixteen);
		karaNumbers.put(17, R.raw.kara_seventeen);
		karaNumbers.put(18, R.raw.kara_eighteen);
		karaNumbers.put(19, R.raw.kara_nineteen);
		karaNumbers.put(20, R.raw.kara_twenty);

		karaLetters.put('A', R.raw.kara_a);
		karaLetters.put('B', R.raw.kara_b);
		karaLetters.put('C', R.raw.kara_c);
		karaLetters.put('D', R.raw.kara_d);
		karaLetters.put('E', R.raw.kara_e);
		karaLetters.put('F', R.raw.kara_f);
		karaLetters.put('G', R.raw.kara_g);
		karaLetters.put('H', R.raw.kara_h);
		karaLetters.put('I', R.raw.kara_i);
		karaLetters.put('J', R.raw.kara_j);
		karaLetters.put('K', R.raw.kara_k);
		karaLetters.put('L', R.raw.kara_l);
		karaLetters.put('M', R.raw.kara_m);
		karaLetters.put('N', R.raw.kara_n);
		karaLetters.put('O', R.raw.kara_o);
		karaLetters.put('P', R.raw.kara_p);
		karaLetters.put('Q', R.raw.kara_q);
		karaLetters.put('R', R.raw.kara_r);
		karaLetters.put('S', R.raw.kara_s);
		karaLetters.put('T', R.raw.kara_t);
		karaLetters.put('U', R.raw.kara_u);
		karaLetters.put('V', R.raw.kara_v);
		karaLetters.put('W', R.raw.kara_w);
		karaLetters.put('X', R.raw.kara_x);
		karaLetters.put('Y', R.raw.kara_y);
		karaLetters.put('Z', R.raw.kara_z);

		lexieColors.put(Color.WHITE, R.raw.lexie_white);
		lexieColors.put(0xff9513df, R.raw.lexie_purple);
		lexieColors.put(0xffffad54, R.raw.lexie_orange);
		lexieColors.put(Color.BLUE, R.raw.lexie_blue);
		lexieColors.put(Color.RED, R.raw.lexie_red);
		lexieColors.put(Color.YELLOW, R.raw.lexie_yellow);
		lexieColors.put(Color.GREEN, R.raw.lexie_green);
		lexieColors.put(Color.GRAY, R.raw.lexie_grey);
		
		lexieShapes.put(Circle.class, R.raw.lexie_circle);
		lexieShapes.put(Square.class, R.raw.lexie_square);
		lexieShapes.put(Triangle.class, R.raw.lexie_triangle);
		lexieShapes.put(Rectangle.class, R.raw.lexie_rectangle);
		lexieShapes.put(Star.class, R.raw.lexie_star);
		lexieShapes.put(Heart.class, R.raw.lexie_heart);
		lexieShapes.put(Oval.class, R.raw.lexie_oval);
		lexieShapes.put(Parallelogram.class, R.raw.lexie_parallelogram);
		lexieShapes.put(Trapezoid.class, R.raw.lexie_trapezoid);
		lexieShapes.put(Pentagon.class, R.raw.lexie_pentagon);
		lexieShapes.put(Hexagon.class, R.raw.lexie_hexagon);
		lexieShapes.put(Heptagon.class, R.raw.lexie_heptagon);
		lexieShapes.put(Octagon.class, R.raw.lexie_octagon);
		lexieShapes.put(Nonagon.class, R.raw.lexie_nonagon);
		lexieShapes.put(Decagon.class, R.raw.lexie_decagon);

		lexieNumbers.put(0, R.raw.lexie_zero);
		lexieNumbers.put(1, R.raw.lexie_one);
		lexieNumbers.put(2, R.raw.lexie_two);
		lexieNumbers.put(3, R.raw.lexie_three);
		lexieNumbers.put(4, R.raw.lexie_four);
		lexieNumbers.put(5, R.raw.lexie_five);
		lexieNumbers.put(6, R.raw.lexie_six);
		lexieNumbers.put(7, R.raw.lexie_seven);
		lexieNumbers.put(8, R.raw.lexie_eight);
		lexieNumbers.put(9, R.raw.lexie_nine);
		lexieNumbers.put(10, R.raw.lexie_ten);
		lexieNumbers.put(11, R.raw.lexie_eleven);
		lexieNumbers.put(12, R.raw.lexie_twelve);
		lexieNumbers.put(13, R.raw.lexie_thirteen);
		lexieNumbers.put(14, R.raw.lexie_fourteen);
		lexieNumbers.put(15, R.raw.lexie_fifteen);
		lexieNumbers.put(16, R.raw.lexie_sixteen);
		lexieNumbers.put(17, R.raw.lexie_seventeen);
		lexieNumbers.put(18, R.raw.lexie_eighteen);
		lexieNumbers.put(19, R.raw.lexie_nineteen);
		lexieNumbers.put(20, R.raw.lexie_twenty);

		lexieLetters.put('A', R.raw.lexie_a);
		lexieLetters.put('B', R.raw.lexie_b);
		lexieLetters.put('C', R.raw.lexie_c);
		lexieLetters.put('D', R.raw.lexie_d);
		lexieLetters.put('E', R.raw.lexie_e);
		lexieLetters.put('F', R.raw.lexie_f);
		lexieLetters.put('G', R.raw.lexie_g);
		lexieLetters.put('H', R.raw.lexie_h);
		lexieLetters.put('I', R.raw.lexie_i);
		lexieLetters.put('J', R.raw.lexie_j);
		lexieLetters.put('K', R.raw.lexie_k);
		lexieLetters.put('L', R.raw.lexie_l);
		lexieLetters.put('M', R.raw.lexie_m);
		lexieLetters.put('N', R.raw.lexie_n);
		lexieLetters.put('O', R.raw.lexie_o);
		lexieLetters.put('P', R.raw.lexie_p);
		lexieLetters.put('Q', R.raw.lexie_q);
		lexieLetters.put('R', R.raw.lexie_r);
		lexieLetters.put('S', R.raw.lexie_s);
		lexieLetters.put('T', R.raw.lexie_t);
		lexieLetters.put('U', R.raw.lexie_u);
		lexieLetters.put('V', R.raw.lexie_v);
		lexieLetters.put('W', R.raw.lexie_w);
		lexieLetters.put('X', R.raw.lexie_x);
		lexieLetters.put('Y', R.raw.lexie_y);
		lexieLetters.put('Z', R.raw.lexie_z);

	}
	
	public static MediaPlayer create(Context context, final int resid)
	{
		MediaPlayer mp = MediaPlayer.create(context, resid);
		mp.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer arg0) {
				// Overridden to get rid of error log messages in newer Android though I still get the
				// "mOnPreparedListener is null. Failed to send MEDIA_PREPARED message" message.
			}
		});
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// Overridden to get rid of error log messages in newer Android
			}
		});
		mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
			@Override
			public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
				// Overridden to get rid of error log messages in newer Android though I still get the
				// "mOnVideoSizeChangedListener is null. Failed to send MEDIA_SET_VDEO_SIZE message" message.
			}
		});
		
		for(MediaPlayer m : remove)
		{
			try
			{
				m.reset();
			}
			catch(Exception ex){}
			
			m.release();
			old.remove(m);
		}
		
		remove.clear();
		
		for(MediaPlayer m : old)
		{
			try
			{
				if(!m.isPlaying())
					remove.add(m);
			}
			catch(Exception ex)
			{
				remove.add(m);
			}
		}
		
		old.add(mp);
		return mp;
	}
	
	public static void killAllSounds()
	{
		for(MediaPlayer m : old)
		{
			try
			{
				m.stop();
			}
			catch(Exception ex)
			{

			}
		}
	}
}
