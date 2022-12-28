package utils;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.util.ArrayList;

import entities.creatures.Zombie;

public class Node {
	int vertex;
	int x, y;
	ArrayList<Node> nextNodes;
	Ellipse2D.Float radius;
	
	public Node(int vertex, int x, int y) {
		this.x = x;
		this.y = y;
		this.vertex = vertex;
		nextNodes = new ArrayList<Node>();
		radius = new Ellipse2D.Float(x - 5, y - 5, 10, 10);
		//radius = new Ellipse2D.Float(x - 50, y - 50, 100, 100);
	}
	
	
	public boolean checkWithinNode(Zombie z) {
		//Ellipse2D.Float point = new Ellipse2D.Float( (z.getX() + z.getWidth()/2), 
		//										(z.getY() + z.getHeight()/2), 1, 1);
		if(radius.intersects(z.getCollisionBounds(0, 0))) {
			//z.setGoToNode(nextNode);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setNextNodes(Node nextNode) {
		
		this.nextNodes.add(nextNode);
	}
	
	public Node getNextNode(int node) {
		return nextNodes.get(node);
	}

	public int getVertex() {
		return vertex;
	}

	public void setVertex(int vertex) {
		this.vertex = vertex;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
