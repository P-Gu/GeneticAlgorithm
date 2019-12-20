import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Robot {
    private enum Direction {NORTH, EAST, SOUTH, WEST};
 
    private int xPos;
    private int yPos;
    private Direction headDirect;
    int maxMoves;
    int movesCounter;
    private int sensorVal;
    private final int sensorActions[];
    private Maze maze;
    private ArrayList<int[]> route;
    

    public Robot(int[] sensorActions, Maze maze, int maxMoves){
        this.sensorActions = this.calcSensorActions(sensorActions);
        this.maze = maze;
        int startPos[] = this.maze.getStartPosition();
        this.xPos = startPos[0];
        this.yPos = startPos[1];
        this.sensorVal = -1;
        this.headDirect = Direction.EAST;
        this.maxMoves = maxMoves;
        this.movesCounter = 0;
        this.route = new ArrayList<int[]>() {{
        	add(startPos);
        }};
    }
    
    public void run(){
        // we keep the robot to run until runs too much or stops
        // not need to stop
        Boolean stop = false;
        while(!stop){       
            this.movesCounter++;
            if (this.getNextAction() == 0 || this.maze.getPositionValue(this.xPos, this.yPos) == 4 || this.movesCounter > this.maxMoves) {
                stop = true;
            }
            this.NextAction();
        }
    }
    

    /* Given the 128 bit binary string, we return an array of 
    integers, each element representing an action for the 
    specified sensor 
    */
    // Our sensorActions returns 64 bit string 
    private int[] calcSensorActions(int[] sensorAnsStr){
        // we are converting each two digits of bits to an integer
        int numActions = (int) sensorAnsStr.length / 2;
        int sensorActions[] = new int[numActions];
        
        // Loop through actions
        for (int sensorValue = 0; sensorValue < numActions; sensorValue++){
            // Get sensor action
            // our possible sensorAction is 0, 1, 2, 3
            /* because 
                00 -> 0
                01 -> 1
                10 -> 2
                11 -> 3
            */
            int sensorAction = 0;
            if (sensorAnsStr[sensorValue*2] == 1){
                sensorAction += 2;
            }
            if (sensorAnsStr[(sensorValue*2)+1] == 1){
                sensorAction += 1;
            }
            
            // Add to sensor-action map
            sensorActions[sensorValue] = sensorAction;
        }
      
        return sensorActions;
    }
    
    public void NextAction(){
        // Move forward
        if (this.getNextAction() == 1) {
            int currentX = this.xPos;
            int currentY = this.yPos;
            
            // Move depending on current direction
            if (Direction.NORTH == this.headDirect) {
                this.yPos--;
                if (this.yPos < 0) {
                    this.yPos = 0;
                }
            }
            else if (Direction.EAST == this.headDirect) {
                this.xPos++;
                if (this.xPos > this.maze.getMaxX()) {
                    this.xPos = this.maze.getMaxX();
                }
            }
            else if (Direction.SOUTH == this.headDirect) {
                this.yPos++;
                if (this.yPos > this.maze.getMaxY()) {
                    this.yPos = this.maze.getMaxY();
                }
            }
            else if (Direction.WEST == this.headDirect) {
                this.xPos--;
                if (this.xPos < 0) {
                    this.xPos = 0;
                }
            }
            
            // We can't move here
            if (this.maze.isWall(this.xPos, this.yPos) == true) {
                this.xPos = currentX;
                this.yPos = currentY;
            } 
            else {
                if(currentX != this.xPos || currentY != this.yPos) {
                    this.route.add(this.getPosition());
                }
            }
        }

        // Turn left
        else if(this.getNextAction() == 2) {
            if (Direction.NORTH == this.headDirect) {
                this.headDirect = Direction.EAST;
            }
            else if (Direction.EAST == this.headDirect) {
                this.headDirect = Direction.SOUTH;
            }
            else if (Direction.SOUTH == this.headDirect) {
                this.headDirect = Direction.WEST;
            }
            else if (Direction.WEST == this.headDirect) {
                this.headDirect = Direction.NORTH;
            }
        }
        // Turn right
        else if(this.getNextAction() == 3) {
            if (Direction.NORTH == this.headDirect) {
                this.headDirect = Direction.WEST;
            }
            else if (Direction.EAST == this.headDirect) {
                this.headDirect = Direction.NORTH;
            }
            else if (Direction.SOUTH == this.headDirect) {
                this.headDirect = Direction.EAST;
            }
            else if (Direction.WEST == this.headDirect) {
                this.headDirect = Direction.SOUTH;
            }
        }
        
        // Reset sensor value
        this.sensorVal = -1;
    }

    public int getNextAction() {
        return this.sensorActions[this.getSensorValue()];
    }
    
 
    // For each step that robot takes, we look around the robot to see if there is any wall adjacent to the respective sensor
    // Then we calculate the sensorVal that is represented in 6 digit binary bits 
    public int getSensorValue(){
        // If sensor value has already been calculated
        if (this.sensorVal > -1) {
            return this.sensorVal;
        }
                
		boolean frontSensor, frontLeftSensor, frontRightSensor, leftSensor, rightSensor, backSensor;
		frontSensor = frontLeftSensor = frontRightSensor = leftSensor = rightSensor = backSensor = false;
        
		int fx, fy, flx, fly, frx, fry, lx, ly, rx, ry, bx, by;
		if (this.getHeading() == Direction.NORTH) {
			fx = this.xPos;
			fy = this.yPos-1;
			flx = this.xPos-1;
			fly = this.yPos-1;
			frx = this.xPos+1;
			fry = this.yPos-1;
			lx = this.xPos-1;
			ly = this.yPos;
			rx = this.xPos+1;
			ry = this.yPos;
			bx = this.xPos;
			by = this.yPos+1;
		}
		else if (this.getHeading() == Direction.EAST) {
			fx = this.xPos+1;
			fy = this.yPos;
			flx = this.xPos+1;
			fly = this.yPos-1;
			frx = this.xPos+1;
			fry = this.yPos+1;
			lx = this.xPos;
			ly = this.yPos-1;
			rx = this.xPos;
			ry = this.yPos+1;
			bx = this.xPos-1;
			by = this.yPos;
		}
		else if (this.getHeading() == Direction.SOUTH) {
			fx = this.xPos;
			fy = this.yPos+1;
			flx = this.xPos+1;
			fly = this.yPos+1;
			frx = this.xPos-1;
			fry = this.yPos+1;
			lx = this.xPos+1;
			ly = this.yPos;
			rx = this.xPos-1;
			ry = this.yPos;
			bx = this.xPos;
			by = this.yPos-1;
		}
		else {
			fx = this.xPos-1;
			fy = this.yPos;
			flx = this.xPos-1;
			fly = this.yPos+1;
			frx = this.xPos-1;
			fry = this.yPos-1;
			lx = this.xPos;
			ly = this.yPos+1;
			rx = this.xPos;
			ry = this.yPos-1;
			bx = this.xPos+1;
			by = this.yPos;
		}
		frontSensor = this.maze.isWall(fx, fy);
        frontLeftSensor = this.maze.isWall(flx, fly);
        frontRightSensor = this.maze.isWall(frx, fry);
        leftSensor = this.maze.isWall(lx, ly);
        rightSensor = this.maze.isWall(rx, ry);
        backSensor = this.maze.isWall(bx, by);
                
        // Calculate sensor value
        int sensorVal = 0;
        
        if (frontSensor) { //increment the 1st digit in binary
            sensorVal += 1;
        }
        if (frontLeftSensor) { //increment the 2nd digit in binary
            sensorVal += 2;
        }
        if (frontRightSensor) { //increment the 3rd digit in binary
            sensorVal += 4;
        }
        if (leftSensor) { //increment the 4th digit in binary
            sensorVal += 8;
        }
        if (rightSensor) { //increment the 5th digit in binary
            sensorVal += 16;
        }
        if (backSensor) { //increment the 6th digit in binary
            sensorVal += 32;
        }

        this.sensorVal = sensorVal;

        // the sensorVal here represents the six digit binary bits
        return sensorVal;
    }
    

    public int[] getPosition(){
        return new int[]{this.xPos, this.yPos};
    }
    
    
    private Direction getHeading(){
        return this.headDirect;
    }
    
    public ArrayList<int[]> getRoute(){       
        return this.route;
    }
    

}
