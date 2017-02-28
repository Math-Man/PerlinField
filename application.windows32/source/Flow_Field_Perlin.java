import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Flow_Field_Perlin extends PApplet {





float inc = .1f;
int scl = 10;
int cols, rows;
float zoff = 0;
int particleAmount = 1000;    //Amount of particles on the screen
float VectorStrength = 1;   // Vector's magnitude
float noise = 2;            //makes vectoral direction change more jittery , 1 is normal
float changeSpeed = 0.0006f; //How erratic the vectors are 
int particalAlpha = 5;     //Alpha of the particles
boolean showVectors = false;

float angleCopy;
boolean clicked = false;
Particle[] particles = new Particle[particleAmount];
PVector[] flowField;

public void setup() 
{
  
  cols = floor(width/scl);
  rows = floor(height/scl);
  
  flowField = new PVector[cols * rows];
  
  
  for(int i = 0 ; i < particleAmount ; i++) 
  {
    particles[i] = new Particle();
  }
  background(255);
}

public void draw()
{

  if(clicked)  {if(mousePressed) {clicked = false; background(255); }}

  if(mousePressed) {clicked = true;}
  
 
  if(clicked) {
    
  if(showVectors) {background(255);}
  float yoff = 0;

  for(int y = 0 ; y < rows; y++) 
  {
    float xoff= 0;
    for(int x = 0; x < cols; x++)
    {
      int index = (x + y * cols);
      float angle = noise(xoff, yoff, zoff) * 2 * PI * noise;
      PVector v = PVector.fromAngle(angle);
      v.setMag(VectorStrength);
      flowField[index] = v;
      xoff += inc;
      stroke(0, 80);
      angleCopy = angle;
      
      
      if(showVectors) {
      pushMatrix();
      translate(x* scl, y * scl);
      rotate(v.heading());
      line(0, 0, scl , 0);
      popMatrix();
      }
      
      
      //fill(0);
      //rect(x * scl , y * scl, scl, scl);
    }
    yoff += inc;
    
    zoff += changeSpeed;
  }
  
  for(int i = 0 ; i < particles.length ; i ++) 
  {  

      particles[i].follow(flowField);
      particles[i].update();
      particles[i].show();
      particles[i].edgeCheck();
      
  }

  
}
}
class Particle
{
  PVector pos, vel, acc;
  float maxSpeed = 2;
  PVector prevPos;
  
  
  Particle(PVector pos, PVector vel, PVector acc) 
  {
    this.pos = pos;
    this.vel = vel;
    this.acc = acc;
    prevPos = this.pos;
  }
  
  Particle() 
  {
    this.pos = new PVector(random(width),random(height));
    this.vel = new PVector(0,0);
    this.acc = new PVector(0,0);
    prevPos = this.pos;
  }
  public void update() 
  {
    this.vel.add(this.acc);
    this.vel.limit(this.maxSpeed);
    this.pos.add(this.vel);
    this.acc.mult(0);
    prevPos = this.pos;
  }
  
  public void applyForce(PVector force) 
  {
    this.acc.add(force);
  }
  
  public void show() 
  {
    float r = this.pos.x, g = this.pos.y, b = this.pos.x * this.pos.y ;
    
    //stylized
    /* 
     r = floor(map(r, 0, width, 0, 255));
     g = floor(map(g, 0, height, 0, 255));
     b = floor(map(b, 0, width * height, 0 , 255));
    */
    //gray scale ancients
    r = width * r % 255;
    g = height * g % 255;
    b = width * height * b % 255;
    
    //Trippy
    r = (angleCopy*this.pos.x) % 255;
    g = (angleCopy*this.pos.y) % 255;
    b = (angleCopy * random(0 , 1000)) % 255;
    
    
    
    fill(r,g,b, particalAlpha);
    stroke(r,g,b, particalAlpha);
    strokeWeight(1);
    //point(this.pos.x, this.pos.y);
    ellipse(this.pos.x, this.pos.y, 2 , 2); 
    line(this.pos.x, this.pos.y, this.prevPos.x, this.prevPos.y);
  }
  
  public void updatePrev() 
  {
    this.prevPos.y = this.pos.y;
    this.prevPos.x = this.pos.x;
  }
  
  public void edgeCheck() 
  {
    if(this.pos.x > width) {this.pos.x = 0; this.updatePrev();}
    if(this.pos.x < 0) {this.pos.x = width; this.updatePrev();}
    if(this.pos.y > height) {this.pos.y = 0; this.updatePrev();}
    if(this.pos.y < 0) {this.pos.y = height; this.updatePrev();}
  }
  
  public void follow(PVector[]  vectors) 
  {
    //println(this);
    float x = floor(this.pos.x / scl);
    float y = floor(this.pos.y / scl);
    int index = floor((x + y * cols));
    //println(index);
    if(index >= width * height /100) { index =width*height/1000;}  //hacky way to prevent array overflow
    PVector force = vectors[index];
    this.applyForce(force);
  }
  
}
  public void settings() {  size(1200, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Flow_Field_Perlin" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
