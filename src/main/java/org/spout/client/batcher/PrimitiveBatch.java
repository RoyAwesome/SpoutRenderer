package org.spout.client.batcher;

import org.spout.api.math.Vector3;
import org.lwjgl.opengl.GL11;
import org.spout.client.renderer.BatchVertexRenderer;
import org.spout.client.renderer.vertexformat.PositionColor;

public class PrimitiveBatch {
	BatchVertexRenderer renderer;
	
	public PrimitiveBatch(){
		renderer = BatchVertexRenderer.constructNewBatch(GL11.GL_TRIANGLES);
	}
	
	public BatchVertexRenderer getRenderer(){
		return renderer;
	}
	
	public void begin(){
		renderer.enableNormals();
		renderer.enableColors();
		renderer.begin();
	}
	
	public void addQuad(Vector3 a, Vector3 b, Vector3 c, Vector3 d){
		renderer.addVertex(a);
		renderer.addVertex(b);
		renderer.addVertex(c);
		
		renderer.addVertex(c);
		renderer.addVertex(a);
		renderer.addVertex(d);
		
	}
	public void addQuad(PositionColor a, PositionColor b, PositionColor c, PositionColor d){
		Vector3 normal = (a.getPosition().subtract(b.getPosition())).cross(b.getPosition().subtract(c.getPosition()));
		renderer.addColor(a.getColor().getRedF(), a.getColor().getGreenF(), a.getColor().getBlueF());
		renderer.addNormal(normal);
		renderer.addVertex(a.getPosition());
		
		renderer.addColor(b.getColor().getRedF(), b.getColor().getGreenF(), b.getColor().getBlueF());
		renderer.addNormal(normal);
		renderer.addVertex(b.getPosition());
		
		renderer.addColor(c.getColor().getRedF(), c.getColor().getGreenF(), c.getColor().getBlueF());
		renderer.addNormal(normal);		
		renderer.addVertex(c.getPosition());
		
		renderer.addColor(c.getColor().getRedF(), c.getColor().getGreenF(), c.getColor().getBlueF());
		renderer.addNormal(normal);		
		renderer.addVertex(c.getPosition());
		
		renderer.addColor(d.getColor().getRedF(), d.getColor().getGreenF(), d.getColor().getBlueF());
		renderer.addNormal(normal);		
		renderer.addVertex(d.getPosition());
		
		renderer.addColor(a.getColor().getRedF(), a.getColor().getGreenF(), a.getColor().getBlueF());
		renderer.addNormal(normal);		
		renderer.addVertex(a.getPosition());
		
	}
	
	public void end(){
		renderer.end();
		
	}
	public void draw(){
		renderer.render();
	}
}
