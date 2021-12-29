#version 150

uniform sampler2D DiffuseDepthSampler;

uniform vec4 ColorModulate;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    fragColor = texture(DiffuseDepthSampler, texCoord) * ColorModulate;
}
