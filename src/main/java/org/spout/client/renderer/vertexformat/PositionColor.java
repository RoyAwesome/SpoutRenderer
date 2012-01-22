package org.spout.client.renderer.vertexformat;

import org.spout.api.math.Vector3;
import org.spout.api.util.Color;

public class PositionColor extends VertexFormat {
	
	Vector3 position;
	Color color;
	
	public PositionColor(Vector3 position, Color c){
		this.position = position;
		this.color = c;
	}

	public Vector3 getPosition() {
		return position;
	}

	public Color getColor() {
		return color;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
