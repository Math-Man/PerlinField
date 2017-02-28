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
  void update() 
  {
    this.vel.add(this.acc);
    this.vel.limit(this.maxSpeed);
    this.pos.add(this.vel);
    this.acc.mult(0);
    prevPos = this.pos;
  }
  
  void applyForce(PVector force) 
  {
    this.acc.add(force);
  }
  
  void show() 
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
  
  void updatePrev() 
  {
    this.prevPos.y = this.pos.y;
    this.prevPos.x = this.pos.x;
  }
  
  void edgeCheck() 
  {
    if(this.pos.x > width) {this.pos.x = 0; this.updatePrev();}
    if(this.pos.x < 0) {this.pos.x = width; this.updatePrev();}
    if(this.pos.y > height) {this.pos.y = 0; this.updatePrev();}
    if(this.pos.y < 0) {this.pos.y = height; this.updatePrev();}
  }
  
  void follow(PVector[]  vectors) 
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