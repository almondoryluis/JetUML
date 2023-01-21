/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020 by McGill University.
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
package org.jetuml.diagram.edges;

import org.jetuml.diagram.AbstractDiagramElement;
import org.jetuml.diagram.Diagram;
import org.jetuml.diagram.Edge;
import org.jetuml.diagram.Node;

/**
 * Groups the functionality common to all edges.
 */
public abstract class AbstractEdge extends AbstractDiagramElement implements Edge
{
	private Node aStart;
	private Node aEnd;
	private Diagram aDiagram;
	
	@Override
	public final void connect(Node pStart, Node pEnd, Diagram pDiagram)
	{
		assert pStart != null && pEnd != null;
		aStart = pStart;
		aEnd = pEnd;
		aDiagram = pDiagram;
	}

	@Override
	public Node getStart()
	{
		return aStart;
	}

	@Override
	public Node getEnd()
	{
		return aEnd;
	}

	@Override
	public Diagram getDiagram()
	{
		return aDiagram;
	}

	@Override
	public AbstractEdge clone()
	{
		AbstractEdge clone = (AbstractEdge) super.clone();
		return clone;
	}
	
	@Override
	public String toString()
	{
		String result = getClass().getSimpleName();
		if( getStart() != null && getEnd() != null )
		{
			result += " " + getStart() + " -> " + getEnd();
		}
		return result;
	}
}
