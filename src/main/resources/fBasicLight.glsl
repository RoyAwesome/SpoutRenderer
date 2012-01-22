//glsl lighting shader



varying vec4 normal;
varying vec4 color;
varying vec3 point;

uniform vec4 ambientColor;

uniform mat4 View;

uniform vec4 lightColor;
uniform vec4 lightDirection;

void main()
{
	//gl_FragColor = normal;
	
	vec3 light = normalize( lightDirection.xyz - point);
	vec3 H = normalize(light + -point);
	
	vec3 Normal = normalize(View * vec4(normal) ).xyz;
	
	float diffuseCoef = max( dot(light, Normal), 0.0);
	vec4 diffuse = diffuseCoef * lightColor;
	
	
	gl_FragColor = lerp(color, diffuse + ambientColor, diffuseCoef);

}