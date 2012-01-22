//glsl lighting shader

attribute vec4 vPosition;
attribute vec4 vColor;
attribute vec4 vNormal;

varying vec4 normal;
varying vec4 color;
varying vec3 point;

uniform mat4 Projection;
uniform mat4 View;

uniform vec4 ambientColor;

uniform vec4 lightColor;
uniform vec4 lightDirection;

void main()
{
	gl_Position = Projection * View * vPosition;
	point = (View* vPosition).xyz;
	normal = vNormal;
	color = vColor;

}