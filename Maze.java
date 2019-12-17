
import java.util.ArrayList;

public class Maze {
	private final int maze[][];
	private int startPosition[] = { -1, -1 };
	private int visited[][];

	public Maze(int maze[][]) {
		this.maze = maze;
	}

	public int[] getStartPosition() {
		if (startPosition[0]>=0) return startPosition;
		for (int i=0; i<this.maze.length; i++) {
			for (int j=0; j<this.maze[0].length; j++) {
				if (this.maze[i][j]==2) {
					startPosition[0] = i;
					startPosition[1] = j;
					return startPosition;
				}
			}
		}
		return startPosition;
	}

	public int getPositionValue(int x, int y) {
		if (!this.inMaze(x, y)) return 1;
		return this.maze[x][y];
	}

	public boolean inMaze(int x, int y) {
		return x>-1 && y>-1 && x<this.maze.length && y<this.maze[0].length;
	}


	public boolean isWall(int x, int y) {
        return (!this.inMaze(x, y)) || this.maze[x][y] == 1;
	}

	public int getMaxX() {
        return this.maze.length-1;
	}

	public int getMaxY() {
        return this.maze[0].length-1;
	}

	public int scoreRoute(ArrayList<int[]> route) {
		this.visited = new int[maze.length][maze[0].length];
		int score = 0;
		for (Object p:route) {
			int[] pos = (int [])p;
			int posX = pos[0];
			int posY = pos[1];
			if (this.maze[posX][posY]==3 && this.visited[posX][posY]!=1) {
				this.visited[posX][posY] = 1;
				score ++;
			}
		}
		return score;
	}
}
