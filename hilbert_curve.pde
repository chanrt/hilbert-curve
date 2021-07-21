int order = 6;
int num_points;
float res;

int counter, speed;
float hue_offset;

void setup() {
  fullScreen();
  colorMode(HSB);
  textAlign(CENTER);
  textSize(32);
  initParams();
}

PVector getCoord(int index) {
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

    shift = int(pow(2, iteration));
    index = index >> 2;
    secondary_index = index & 3;

    switch(secondary_index) {
    case 0:
      temp = int(coord.x);
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
      temp = int(shift-1-coord.x);
      coord.x = shift-1-coord.y;
      coord.y = temp;
      coord.x += shift;
      break;
    }
  }
  return coord.mult(res);
}

void draw() {
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

void initParams() {
  num_points = int(pow(4, order));
  res = min(width, height) / pow(2, order);
  counter = 1;
  if(order < 10) {
    speed = int(num_points / pow(2, order-1));
  }  
  else {
    speed = int(num_points / pow(2, order-4));
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

void keyPressed() {
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
