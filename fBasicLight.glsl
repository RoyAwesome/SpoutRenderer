//glsl lighting shader



varying vec4 normal;
varying vec4 color;

uniform vec4 ambientColor;

uniform vec4 lightColor;
uniform vec4 lightDirection;

void main()
{
	gl_FragColor = normal;
	//gl_FragColor = lerp(ambientColor, color, dot(lightDirection, normal) * lightColor);

}