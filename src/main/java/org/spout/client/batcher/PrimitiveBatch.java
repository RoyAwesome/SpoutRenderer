package org.spout.client.batcher;

import org.spout.api.math.Vector3;
import org.lwjgl.opengl.GL11;
import org.spout.client.renderer.BatchVertexRenderer;

public class PrimitiveBatch {
	BatchVertexRenderer renderer;
	
	public PrimitiveBatch(){
		renderer = BatchVertexRenderer.constructNewBatch(GL11.GL_TRIANGLES);
	}
	
	public BatchVertexRenderer getRenderer(){
		return renderer;
	}
	
	public void begin(){
		renderer.begin();
	}
	
	public void AddQuad(Vector3 a, Vector3 b, Vector3 c, Vector3 d){
		renderer.AddVertex(a);
		renderer.AddVertex(b);
		renderer.AddVertex(c);
		
		renderer.AddVertex(c);
		renderer.AddVertex(d);
		renderer.AddVertex(a);
		
	}
	public void end(){
		renderer.end();
		
	}
	public void draw(){
		renderer.render();
	}
}
