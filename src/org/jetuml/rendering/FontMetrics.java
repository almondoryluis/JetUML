/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020, 2021 by McGill University.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/
package org.jetuml.rendering;

import org.jetuml.geom.Dimension;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * A utility class to determine various font position metrics
 * for the particular font.
 * 
 * A visual diagram for why the bounds values are what they are
 * (with word "Thy"):   ____________________
 * getMinY() (ascent)  |*****  *           |
 *                     |  *    *           |
 *                     |  *    *****   *  *|
 *                     |  *    *   *   *  *|
 *                     |  *    *   *   ****|
 * (baseline)          |------------------*| x=getWidth()
 *                     |                  *|
 *                     |                  *| 
 * y = 0  (descent)    |                ***|
 *                     |                   |
 *                     |                   |
 * getMaxY() (leading) |-------------------|
 *
 * Hence, upon calling getHeight(), to get tight bounds, one should subtract
 * off the leading value (found by getting the max Y value of a one-lined text
 * box)
 */
public class FontMetrics 
{
	public static final int DEFAULT_FONT_SIZE = 12;
	public static final String DEFAULT_FONT_NAME = "System";
	private static final String BLANK = "";
	private Text aTextNode;

	/**
	 * Creates a new FontMetrics object.
	 * @param pFont The font to use.
	 */

	public FontMetrics(Font pFont)
	{
		assert pFont != null;
		
		aTextNode = new Text();
		aTextNode.setFont(pFont);
	}

	/**
	 * Returns the dimension of a given string.
	 * @param pString The string to which the bounds pertain.
	 * @return The dimension of the string
	 */
	public Dimension getDimension(String pString)
	{
		assert pString != null;
		
		aTextNode.setText(pString);
		Bounds bounds = aTextNode.getLayoutBounds();
		aTextNode.setText(BLANK);
		double leading = aTextNode.getLayoutBounds().getMaxY();
		return new Dimension((int) Math.round(bounds.getWidth()), (int) Math.round(bounds.getHeight()));
		
//		aTextNode.setText(pString);
//	    Bounds tb = aTextNode.getLayoutBounds();
//	    Rectangle stencil = new Rectangle(
//	            tb.getMinX(), tb.getMinY(), tb.getWidth(), tb.getHeight()
//	    );
//	    Shape intersection = Shape.intersect(aTextNode, stencil);
//
//	    Bounds ib = intersection.getLayoutBounds();
//	    return new Dimension((int) Math.round(ib.getWidth()), (int) Math.round(ib.getHeight()));
	}
	
	/**
	 * Returns the height of a string including the leading space.
	 * 
	 * @param pString The string. 
	 * @return The height of the string.
	 * @pre pString != null
	 */
	public int getHeight(String pString)
	{
		assert pString != null;
		
//		aTextNode.setText(pString);
//		return (int) Math.round(aTextNode.getLayoutBounds().getHeight());
		aTextNode.setText("Thy\nThy");
		double height1 = aTextNode.getLayoutBounds().getHeight();
		aTextNode.setText("Thy");
		double height2 = aTextNode.getLayoutBounds().getHeight();
		return (int) Math.round(height1 - height2);
	}
	
	public static void main(String[] args)
	{
		Text t1 = new Text("Thy");
		t1.setFont(new Font("Consolas", 12));
		Text t2 = new Text("Thy\nThy");
		t2.setFont(new Font("Consolas", 12));
		System.out.println(t1.getLayoutBounds());
		System.out.println(t2.getLayoutBounds());
	}
} 