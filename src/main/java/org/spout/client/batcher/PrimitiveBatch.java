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
		renderer.enableColors();
		renderer.begin();
	}
	
	public void AddQuad(Vector3 a, Vector3 b, Vector3 c, Vector3 d){
		renderer.AddVertex(a);
		renderer.AddVertex(b);
		renderer.AddVertex(c);
		
		renderer.AddVertex(c);
		renderer.AddVertex(a);
		renderer.AddVertex(d);
		
	}
	public void AddQuad(PositionColor a, PositionColor b, PositionColor c, PositionColor d){
		renderer.AddColor(a.getColor().getRedF(), a.getColor().getGreenF(), a.getColor().getBlueF());
		renderer.AddVertex(a.getPosition());
		
		renderer.AddColor(b.getColor().getRedF(), b.getColor().getGreenF(), b.getColor().getBlueF());
		renderer.AddVertex(b.getPosition());
		
		renderer.AddColor(c.getColor().getRedF(), c.getColor().getGreenF(), c.getColor().getBlueF());
		renderer.AddVertex(c.getPosition());
		
		renderer.AddColor(c.getColor().getRedF(), c.getColor().getGreenF(), c.getColor().getBlueF());
		renderer.AddVertex(c.getPosition());
		
		renderer.AddColor(d.getColor().getRedF(), d.getColor().getGreenF(), d.getColor().getBlueF());
		renderer.AddVertex(d.getPosition());
		
		renderer.AddColor(a.getColor().getRedF(), a.getColor().getGreenF(), a.getColor().getBlueF());
		renderer.AddVertex(a.getPosition());
		
	}
	
	public void end(){
		renderer.end();
		
	}
	public void draw(){
		renderer.render();
	}
}
