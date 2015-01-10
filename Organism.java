import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
public abstract class Organism extends MapObject{
    protected double speed,angle;
    protected int health;
    protected DNA dna;
    protected int species;
    private long start;
    public Organism(Map map,double x,double y,int species){
	super(map,x,y);
	this.species = species;
	try{
	    if (species == 0)
		img = ImageIO.read(getClass().getResourceAsStream("buggy.png"));
	    else if (species == 1)
		img = ImageIO.read(getClass().getResourceAsStream("gooey.png"));
	    else if (species == 2)
		img = ImageIO.read(getClass().getResourceAsStream("aqua.png"));
	    else if (species == 3)
		img = ImageIO.read(getClass().getResourceAsStream("biter.png"));
	    else if (species == 4)
		img = ImageIO.read(getClass().getResourceAsStream("diatom.png"));
	    else if (species == 8)
		img = ImageIO.read(getClass().getResourceAsStream("triangle.png"));
	} catch(Exception e){
	    e.printStackTrace();
	}
	width = img.getWidth();
	height = img.getHeight();
	boxwidth = width/2;
	boxheight = height/2;
	speed = Math.random()*3+2;
	angle = Math.random()*360;
	
	start = System.currentTimeMillis();
    }
    public void update(){
	long elapsed = System.currentTimeMillis();
	if(elapsed-start>1000){
	    start = System.currentTimeMillis();
	    health -= dna.getHunger();
	}
    }
    public void draw(Graphics g){
	mapx = map.getX();
	mapy = map.getY();
	g.setColor(Color.black);
	g.drawRect((int)(x-mapx-width/2),(int)(y-mapy-height/2),width,height);
	g.setColor(Color.red);
	g.drawRect((int)(x-mapx-boxwidth/2),(int)(y-mapy-boxheight/2),boxwidth,boxheight);
	AffineTransform tx = AffineTransform.getRotateInstance(angle, width/2, height/2);
	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	g.drawImage(op.filter(img,null),(int)(x-mapx-width/2),(int)(y-mapy-height/2),null);
    }
    public void hit(int dmg){
	health = Math.max(health-dmg,0);
    }
    public int getSpecies(){
	return species;
    }
    public boolean isDead(){
	return health<=0;
    }
    public DNA getDNA(){
	return dna;
    }
    public void consume(DNA dna2){
	dna.add(dna2);
	this.health += dna2.getHealth()/2;
	if(this.health >= dna.getHealth()) this.health = dna.getHealth();
	this.speed = this.dna.getSpeed();
    }
    public void consume(Berry berry){
	health += berry.recoverHealth();
	if(this.health >= dna.getHealth()) this.health = dna.getHealth();
    }
}
