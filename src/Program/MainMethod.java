package Program;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import Values.BestSolution;
import Values.Data;
import Values.MoveData;
import Values.Parameter;
import Values.Tabu;

public class MainMethod {
	
	private Data data = Data.getInstance();
	private Parameter parameter = Parameter.getInstance();
	
	private boolean isInteger = false;
	
	// 演算法主程式
	public BestSolution mainProgram(boolean isInteger) {
		BestSolution bestSolution = new BestSolution();
		ArrayList<Tabu> tabuList = new ArrayList<>();
		int termination = 0;
		this.isInteger = isInteger;
		// 設定初始狀態
		bestSolution = initialSolution();
		
		for (int t=0; t<parameter.getGeneration(); t++) {
			// 移步
			Move move = new Move();
			MoveData moveData = move.move(bestSolution.solution, tabuList);
			if (moveData.positionOne != -1 && moveData.positionTwo != -1) {
				// 是否超過最大名單數
				if (tabuList.size() > parameter.getTabuListSize()) {
					tabuList.remove(0);
				}
				// 新增禁忌名單
				tabuList.add(getTabuList(moveData, t)); 
				// 更新
				bestSolution = updateBestSolution(moveData);
			} else {
				termination++;
			}
			if (termination >= 10) {
				break;
			}
		}
		Optimization optimization = new Optimization();
		bestSolution.solution = optimization.twoOptimization(bestSolution.solution);
		bestSolution.distance = caculateDistance(bestSolution.solution);
		bestSolution.fitness = 1/bestSolution.distance;
		clearAll();
		return bestSolution;
	}
	
	// 初始解
	private BestSolution initialSolution() {
		ArrayList<Integer> point = getDataSetList();
		BestSolution initialSolution = new BestSolution();
		initialSolution.solution = createSolution(point);
		point.clear();
		initialSolution.distance = caculateDistance(initialSolution.solution);
		initialSolution.fitness = 1/initialSolution.distance;
		return initialSolution;
	}
	// 資料集List
	private ArrayList<Integer> getDataSetList() {
		ArrayList<Integer> point = new ArrayList<>();
		for (int i=0; i<data.total; i++) {
			point.add(i);
		}
		return point;
	}
	// 產生解
	private ArrayList<Integer> createSolution(ArrayList<Integer> point) {
		Random random = new Random();
		boolean run = true;
		ArrayList<Integer> newSolution = new ArrayList<Integer>();
		ArrayList<Integer> allPoint = new ArrayList<Integer>();
		allPoint.addAll(point);
		while (run) {
			int tmp = random.nextInt(allPoint.size());
			newSolution.add(allPoint.get(tmp));
			allPoint.remove(tmp);
			if (allPoint.size() <= 0)
				run = false;
		}
		return newSolution;
	}
	
	// 取得禁忌名單
	private Tabu getTabuList(MoveData moveData, int t) {
		Tabu tabu = new Tabu();
		tabu.generation = t;
		tabu.positionOne = moveData.positionOne;
		tabu.positionTwo = moveData.positionTwo;
		return tabu;
	}
	
	// 更新
	private BestSolution updateBestSolution(MoveData moveData) {
		BestSolution bestSolution = new BestSolution();
		bestSolution.solution = moveData.solution;
		double dis = 0;
		if (this.isInteger) {
			dis = moveData.distanceInt;
		} else {
			dis = moveData.distanceDouble;
		}
		bestSolution.distance = dis;
		bestSolution.fitness = 1/dis;
		return bestSolution;
	}
	
	// 計算解距離
	private double caculateDistance(ArrayList<Integer> solution) {
		int size = solution.size();
		double tmpDistance = 0;
		
		for (int i=0; i<size; i++){
			if (i == size-1) {
				if (this.isInteger)
					tmpDistance += pointDistanceInt(solution.get(i), solution.get(0));
				else {
					tmpDistance += pointDistanceDouble(solution.get(i), solution.get(0));
				}
			}
			else {
				if (this.isInteger)
					tmpDistance += pointDistanceInt(solution.get(i), solution.get(i+1));
				else
					tmpDistance += pointDistanceDouble(solution.get(i), solution.get(i+1));
			}
		}
		return tmpDistance;
	}
	// 距離公式，型態為Integer
	private double pointDistanceInt(int pointA, int pointB) {
		return (double)Math.round(Point.distance(
				this.data.x[pointA], this.data.y[pointA], 
				this.data.x[pointB], this.data.y[pointB]));
	}
	// 距離公式，型態為Double
	private double pointDistanceDouble(int pointA, int pointB) {
		return Point.distance(
				this.data.x[pointA], this.data.y[pointA], 
				this.data.x[pointB], this.data.y[pointB]);
	}
	
	// 釋放記憶體
	private void clearAll() {
		this.data = null;
		this.parameter = null;
	}
}
