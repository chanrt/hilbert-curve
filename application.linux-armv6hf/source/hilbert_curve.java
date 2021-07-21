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

public class hilbert_curve extends PApplet {

int order = 6;
int num_points;
float res;

int counter, speed;
float hue_offset;

public void setup() {
  
  colorMode(HSB);
  textAlign(CENTER);
  textSize(32);
  initParams();
}

public PVector getCoord(int index) {
  PVector coord = new PVector();
  int primary_index = index & 3;

  switch(primary_index) {
  case 0:
    coord.set(0, 0);
    break;
  case 1:
    coord.set(0, 1);
    break;
  case 2:
    coord.set(1, 1);
    break;
  case 3:
    coord.set(1, 0);
    break;
  }

  int shift, secondary_index, temp;
  for (int iteration = 1; iteration < order; iteration++) {

    shift = PApplet.parseInt(pow(2, iteration));
    index = index >> 2;
    secondary_index = index & 3;

    switch(secondary_index) {
    case 0:
      temp = PApplet.parseInt(coord.x);
      coord.x = coord.y;
      coord.y = temp;
      break;
    case 1:
      coord.y += shift;
      break;
    case 2:
      coord.x += shift;
      coord.y += shift;
      break;
    case 3:
      temp = PApplet.parseInt(shift-1-coord.x);
      coord.x = shift-1-coord.y;
      coord.y = temp;
      coord.x += shift;
      break;
    }
  }
  return coord.mult(res);
}

public void draw() {
  noFill();
  translate(res / 2, res / 2);
  text("Order: " + Integer.toString(order), 3 * width / 4, height / 2);
  text("Vertices: " + Integer.toString(num_points), 3 * width / 4, height / 2 + 40);

  PVector point1, point2;
  float hue;
  for(int i = 0; i < speed && counter < num_points; i++, counter++) {
    point1 = getCoord(counter-1);
    point2 = getCoord(counter);
    hue = counter * 255 / num_points + hue_offset;
    if(hue > 255) {
      hue_offset -= 255;
    }
    stroke(hue, 255, 255);
    line(point1.x, point1.y, point2.x, point2.y);
  }
}

public void initParams() {
  num_points = PApplet.parseInt(pow(4, order));
  res = min(width, height) / pow(2, order);
  counter = 1;
  if(order < 10) {
    speed = PApplet.parseInt(num_points / pow(2, order-1));
  }  
  else {
    speed = PApplet.parseInt(num_points / pow(2, order-4));
  }
  
  background(0);
  hue_offset = random(255);
  stroke(255);
  if(min(width, height) == height) {
    
    line(height, 0, height, height);
  }
  else {
    line(0, width, width, width);
  }
}

public void keyPressed() {
  if (keyCode == LEFT || key == 'a' || key == 'A') {
    order--;

    if (order <= 0) {
      order = 1;
    }
    initParams();
  }
  if (keyCode == RIGHT || key == 'd' || key == 'D') {
    order++;
    initParams();
  }
  if (key == 'r' || key == 'R') {
    initParams();
  }
}

  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "hilbert_curve" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
