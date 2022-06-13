/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2022 by McGill University.
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
package org.jetuml.layouttests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.jetuml.diagram.PropertyName;
import org.jetuml.diagram.edges.AggregationEdge;
import org.jetuml.diagram.edges.GeneralizationEdge;
import org.jetuml.diagram.edges.NoteEdge;
import org.jetuml.geom.Rectangle;
import org.jetuml.viewers.RenderingFacade;
import org.jetuml.viewers.edges.EdgeViewerRegistry;
import org.jetuml.viewers.nodes.TypeNodeViewer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/*
 * This class tests that the layout of a manually-created diagram file corresponds to expectations.
 */
public class TestLayoutClassDiagram1 extends AbstractTestClassDiagramLayout
{
	private static final Path PATH = Path.of("testdata", "testPersistenceService.class.jet");

	public TestLayoutClassDiagram1() throws IOException
	{
		super(PATH);
	}
	 
	/*
	 * Tests that nodes are in the position that corresponds to their position value 
	 * in the file. We don't test Node7 because its position is calculated from its children
	 */
	@ParameterizedTest
	@CsvSource({"Node1, 200, 10",
				"Node2, 30, 130",
				"Node3, 200, 130",
				"Node4, 370, 130",
				"Node5, 200, 280",
				"Node6, 440, 290"})
	void testNamedNodePosition(String pNodeName, int pExpectedX, int pExpectedY)
	{
		verifyPosition(nodeByName(pNodeName), pExpectedX, pExpectedY);
	}
	
	/*
	 * Tests that all class nodes that are supposed to have the default dimension
	 * actually do. 
	 */
	@ParameterizedTest
	@ValueSource(strings = {"Node1", "Node2", "Node3", "Node4"})
	void testClassNodesDefaultDimension(String pNodeName)
	{
		verifyClassNodeDefaultDimensions(nodeByName(pNodeName));
	}
	
	/*
	 * Tests that the note node has the default dimensions. 
	 */
	@Test
	void testNoteNodeDefaultDimension()
	{
		verifyNoteNodeDefaultDimensions(nodeByName("Node6"));
	}
	
	/*
	 * Tests that Node5 is longer that the default height 
	 */
	@Test
	void testNode5IsExpanded()
	{
		final int DEFAULT_HEIGHT = getStaticIntFieldValue(TypeNodeViewer.class, "DEFAULT_HEIGHT");
		Rectangle bounds = RenderingFacade.getBounds(nodeByName("Node5"));
		assertTrue(bounds.getHeight() > DEFAULT_HEIGHT);
	}
	
	/*
	 * Tests that the bounds of the package node are outside of the bounds
	 * of its child Node2
	 */
	@Test
	void testPackageNodeContainment()
	{
		verifyPackageNodeContainmentOfSingleNode("Node7", "Node2");
	}
	
	/*
	 * Tests that the dependency edge connects to its node boundaries. 
	 */
	@Test
	void testDependencyEdge()
	{
		Rectangle boundsNode2 = RenderingFacade.getBounds(nodeByName("Node2"));
		Rectangle boundsNode3 = RenderingFacade.getBounds(nodeByName("Node3"));
		Rectangle edgeBounds = EdgeViewerRegistry.getBounds(edgeByMiddleLabel("e1"));
		assertWithDefaultTolerance(boundsNode2.getMaxX(), edgeBounds.getX());
		assertWithDefaultTolerance(boundsNode3.getX(), edgeBounds.getMaxX());
	}
	
	/*
	 * Tests that the implementation edge connects to its node boundaries. 
	 */
	@Test
	void testImplementationEdge()
	{
		Rectangle boundsNode1 = RenderingFacade.getBounds(nodeByName("Node1"));
		Rectangle boundsNode3 = RenderingFacade.getBounds(nodeByName("Node3"));
		GeneralizationEdge edge = (GeneralizationEdge) edgesByType(GeneralizationEdge.class).stream()
				.filter(e -> e.properties().get(PropertyName.GENERALIZATION_TYPE).get() == GeneralizationEdge.Type.Implementation)
				.findFirst()
				.get();
		Rectangle edgeBounds = EdgeViewerRegistry.getBounds(edge);
		assertWithDefaultTolerance(boundsNode1.getMaxY(), edgeBounds.getY());
		assertWithDefaultTolerance(boundsNode3.getY(), edgeBounds.getMaxY());
	}
	
	/*
	 * Tests that the inheritance edge connects to its node boundaries. 
	 */
	@Test
	void testInheritanceEdge()
	{
		Rectangle boundsNode3 = RenderingFacade.getBounds(nodeByName("Node3"));
		Rectangle boundsNode5 = RenderingFacade.getBounds(nodeByName("Node5"));
		GeneralizationEdge edge = (GeneralizationEdge) edgesByType(GeneralizationEdge.class).stream()
				.filter(e -> e.properties().get(PropertyName.GENERALIZATION_TYPE).get() == GeneralizationEdge.Type.Inheritance)
				.findFirst()
				.get();
		Rectangle edgeBounds = EdgeViewerRegistry.getBounds(edge);
		assertWithDefaultTolerance(boundsNode3.getMaxY(), edgeBounds.getY());
		assertWithDefaultTolerance(boundsNode5.getY(), edgeBounds.getMaxY());
	}
	
	/*
	 * Tests that the aggregation edge connects to its node boundaries. 
	 */
	@Test
	void testAggregationEdge()
	{
		Rectangle boundsNode3 = RenderingFacade.getBounds(nodeByName("Node3"));
		Rectangle boundsNode4 = RenderingFacade.getBounds(nodeByName("Node4"));
		AggregationEdge edge = (AggregationEdge) edgesByType(AggregationEdge.class).stream()
				.filter(e -> e.properties().get(PropertyName.AGGREGATION_TYPE).get() == AggregationEdge.Type.Aggregation)
				.findFirst()
				.get();
		Rectangle edgeBounds = EdgeViewerRegistry.getBounds(edge);
		assertWithDefaultTolerance(boundsNode3.getMaxX(), edgeBounds.getX());
		assertWithDefaultTolerance(boundsNode4.getX(), edgeBounds.getMaxX());
	}
	
	/*
	 * Tests that the composition edge connects to its node boundaries. 
	 */
	@Test
	void testCompositionEdge()
	{
		Rectangle boundsNode5 = RenderingFacade.getBounds(nodeByName("Node5"));
		Rectangle boundsNode4 = RenderingFacade.getBounds(nodeByName("Node4"));
		AggregationEdge edge = (AggregationEdge) edgesByType(AggregationEdge.class).stream()
				.filter(e -> e.properties().get(PropertyName.AGGREGATION_TYPE).get() == AggregationEdge.Type.Composition)
				.findFirst()
				.get();
		Rectangle edgeBounds = EdgeViewerRegistry.getBounds(edge);
		assertWithDefaultTolerance(boundsNode5.getMaxX(), edgeBounds.getX());
		assertWithDefaultTolerance(boundsNode4.getX(), edgeBounds.getMaxX());
	}
	
	/*
	 * Tests that the note edge connects to the note node boundary and falls within
	 *  the target node.
	 */
	@Test
	void testNoteEdge()
	{
		Rectangle boundsNode6 = RenderingFacade.getBounds(nodeByName("Node6"));
		Rectangle boundsNode4 = RenderingFacade.getBounds(nodeByName("Node4"));
		NoteEdge edge = (NoteEdge) edgesByType(NoteEdge.class).stream()
				.findFirst()
				.get();
		Rectangle edgeBounds = EdgeViewerRegistry.getBounds(edge);
		assertWithDefaultTolerance(boundsNode6.getY(), edgeBounds.getMaxY());
		assertTrue(boundsNode4.contains(edge.getEnd().position()));
	}
}
