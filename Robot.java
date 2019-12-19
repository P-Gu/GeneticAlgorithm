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
        this.route = new ArrayList<int[]>();
        this.route.add(startPos);
    }
    
    /**
     * Runs the robot's actions based on sensor inputs
     */
    public void run(){
        // we keep the robot to run until runs too much or stops
        // not need to stop
        Boolean stop = false;
        while(stop.equals(false)){    
            // keep track of scores for fitness test       
            this.movesCounter++;
            
            // Break if the robot stops moving
            if (this.getNextAction() == 0) {
                stop = true;
            }

            // Break if we reach the goal
            if (this.maze.getPositionValue(this.xPos, this.yPos) == 4) {
                stop = true;
            }
            
            // Break if we reach a maximum number of moves
            if (this.movesCounter > this.maxMoves) {
                stop = true;
            }

            // Run action
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
    
    /**
     * Runs the next action
     */
    public void NextAction(){
        // We use four actions binary to integer representation
        // Since the integer representation of 0 is 00 and the action is ``DO nothing",
        // we do not change our heading

        // Move forward
        if (this.getNextAction() == 1) {
            int currentX = this.xPos;
            int currentY = this.yPos;
            
            // Move depending on current direction
            if (Direction.NORTH == this.headDirect) {
                this.yPos += -1;
                if (this.yPos < 0) {
                    this.yPos = 0;
                }
            }
            else if (Direction.EAST == this.headDirect) {
                this.xPos += 1;
                if (this.xPos > this.maze.getMaxX()) {
                    this.xPos = this.maze.getMaxX();
                }
            }
            else if (Direction.SOUTH == this.headDirect) {
                this.yPos += 1;
                if (this.yPos > this.maze.getMaxY()) {
                    this.yPos = this.maze.getMaxY();
                }
            }
            else if (Direction.WEST == this.headDirect) {
                this.xPos += -1;
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

        // Turn left (clockwise)
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
        // Turn right (anti-clockwise)
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

        // Find which sensors have been activated
        if (this.getHeading() == Direction.NORTH) {
            frontSensor = this.maze.isWall(this.xPos, this.yPos-1);
            frontLeftSensor = this.maze.isWall(this.xPos-1, this.yPos-1);
            frontRightSensor = this.maze.isWall(this.xPos+1, this.yPos-1);
            leftSensor = this.maze.isWall(this.xPos-1, this.yPos);
            rightSensor = this.maze.isWall(this.xPos+1, this.yPos);
            backSensor = this.maze.isWall(this.xPos, this.yPos+1);
        }
        else if (this.getHeading() == Direction.EAST) {
            frontSensor = this.maze.isWall(this.xPos+1, this.yPos);
            frontLeftSensor = this.maze.isWall(this.xPos+1, this.yPos-1);
            frontRightSensor = this.maze.isWall(this.xPos+1, this.yPos+1);
            leftSensor = this.maze.isWall(this.xPos, this.yPos-1);
            rightSensor = this.maze.isWall(this.xPos, this.yPos+1);
            backSensor = this.maze.isWall(this.xPos-1, this.yPos);
        }
        else if (this.getHeading() == Direction.SOUTH) {
            frontSensor = this.maze.isWall(this.xPos, this.yPos+1);
            frontLeftSensor = this.maze.isWall(this.xPos+1, this.yPos+1);
            frontRightSensor = this.maze.isWall(this.xPos-1, this.yPos+1);
            leftSensor = this.maze.isWall(this.xPos+1, this.yPos);
            rightSensor = this.maze.isWall(this.xPos-1, this.yPos);
            backSensor = this.maze.isWall(this.xPos, this.yPos-1);
        }
        else {
            frontSensor = this.maze.isWall(this.xPos-1, this.yPos);
            frontLeftSensor = this.maze.isWall(this.xPos-1, this.yPos+1);
            frontRightSensor = this.maze.isWall(this.xPos-1, this.yPos-1);
            leftSensor = this.maze.isWall(this.xPos, this.yPos+1);
            rightSensor = this.maze.isWall(this.xPos, this.yPos-1);
            backSensor = this.maze.isWall(this.xPos+1, this.yPos);
        }
                
        // Calculate sensor value
        int sensorVal = 0;
        
        if (frontSensor == true) {
            sensorVal += 1;
        }
        if (frontLeftSensor == true) {
            sensorVal += 2;
        }
        if (frontRightSensor == true) {
            sensorVal += 4;
        }
        if (leftSensor == true) {
            sensorVal += 8;
        }
        if (rightSensor == true) {
            sensorVal += 16;
        }
        if (backSensor == true) {
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
    
    /**
     * Returns robot's complete route around the maze
     * 
     * @return ArrayList<int> Robot's route
     */
    public ArrayList<int[]> getRoute(){       
        return this.route;
    }
    










    /**
     * Returns route in printable format
     * 
     * @return String Robot's route
     */
    public String printRoute(){
        String route = "";
        
        for (Object routeStep : this.route) {
            int step[] = (int[]) routeStep;
            route += "{" + step[0] + "," + step[1] + "}";
        }
        return route;
    }
}
