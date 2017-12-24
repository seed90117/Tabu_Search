package Program;

import java.awt.Point;
import java.util.ArrayList;

import Values.Data;
import Values.MoveData;
import Values.Tabu;

public class Move {

	private Data data = Data.getInstance();
	
	public MoveData move(ArrayList<Integer> solution, ArrayList<Tabu> tabuList) {
		MoveData moveData = new MoveData();
		int size = solution.size();
		for (int i=0; i<size; i++) {
			for (int j=i+1; j<size; j++) {
				ArrayList<Integer> newSolution = solution;
				int p1 = newSolution.get(i);
				int p2 = newSolution.get(j);
				newSolution.set(i, p2);
				newSolution.set(j, p1);
				double dis = caculateDistance(newSolution, false);
				if (moveData.positionOne == -1 || moveData.positionTwo == -1 || dis < moveData.distanceDouble) {
					if (isExistenceList(tabuList, p1, p2)) {
						moveData.positionOne = p1;
						moveData.positionTwo = p2;
						moveData.solution = newSolution;
						moveData.distanceDouble = dis;
					}
				}
			}
		}
		if (moveData.positionOne != -1 && moveData.positionTwo != -1) {
			moveData.distanceInt = caculateDistance(moveData.solution, true);
		}
		return moveData;
	}
	
	private boolean isExistenceList(ArrayList<Tabu> tabuList, int pointA, int pointB) {
		boolean isExistence = false;
		for (Tabu tabu : tabuList) {
			if ((tabu.positionOne == pointA && tabu.positionTwo == pointB) || (tabu.positionOne == pointB && tabu.positionTwo == pointA)) {
				isExistence = true;
				break;
			}
		}
		return isExistence;
	}
	
	// 計算解距離
	private double caculateDistance(ArrayList<Integer> solution, boolean isInteger) {
		int size = solution.size();
		double tmpDistance = 0;
		
		for (int i=0; i<size; i++){
			if (i == size-1) {
				if (isInteger)
					tmpDistance += pointDistanceInt(solution.get(i), solution.get(0));
				else {
					tmpDistance += pointDistanceDouble(solution.get(i), solution.get(0));
				}
			}
			else {
				if (isInteger)
					tmpDistance += pointDistanceInt(solution.get(i), solution.get(i+1));
				else
					tmpDistance += pointDistanceDouble(solution.get(i), solution.get(i+1));
			}
		}
		return tmpDistance;
	}
	
	private double pointDistanceInt(int pointA, int pointB) {
		return (double)Math.round(Point.distance(
				this.data.x[pointA], this.data.y[pointA], 
				this.data.x[pointB], this.data.y[pointB]));
	}
	
	private double pointDistanceDouble(int pointA, int pointB) {
		return Point.distance(
				this.data.x[pointA], this.data.y[pointA], 
				this.data.x[pointB], this.data.y[pointB]);
	}
}
