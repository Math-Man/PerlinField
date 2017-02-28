



float inc = .1;
int scl = 10;
int cols, rows;
float zoff = 0;
int particleAmount = 1000;    //Amount of particles on the screen
float VectorStrength = 1;   // Vector's magnitude
float noise = 2;            //makes vectoral direction change more jittery , 1 is normal
float changeSpeed = 0.0006; //How erratic the vectors are 
int particalAlpha = 5;     //Alpha of the particles
boolean showVectors = false;

float angleCopy;
boolean clicked = false;
Particle[] particles = new Particle[particleAmount];
PVector[] flowField;

void setup() 
{
  size(1200, 900);
  cols = floor(width/scl);
  rows = floor(height/scl);
  
  flowField = new PVector[cols * rows];
  
  
  for(int i = 0 ; i < particleAmount ; i++) 
  {
    particles[i] = new Particle();
  }
  background(255);
}

void draw()
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