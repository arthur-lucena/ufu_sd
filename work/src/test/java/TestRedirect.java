public class TestRedirect {
	// https://matjaz.it/shortest-path-problem-in-ring-buffer-or-circular-double-linked-list/
	public static void main(String[] args) {
		int circ = 5;
		
		test(3, 1, circ, false);
		test(4, 1, circ, true);
		test(5, 1, circ, true);
		test(4, 3, circ, false);
		test(4, 2, circ, false);
		test(5, 4, circ, false);
		test(3, 5, circ, true);
		test(2, 4, circ, true);
		test(1, 5, circ, false);
		test(1, 3, circ, true);
		test(3, 2, circ, false);
		test(4, 5, circ, true);
	}
	
	public static void test(int startNode, int destineNode, int ringCirc, boolean direction) {
		System.out.print("Path from " + startNode + " to " + destineNode + " in ring of " + ringCirc + " must be " + direction + "\n");
		
		boolean calcDirection = minDistanceOnRing(startNode, destineNode, ringCirc);
		System.out.print("goto calc " + calcDirection + ", expected " + direction + ", works? " + (calcDirection == direction) + "\n");
		
		System.out.println("-------------------");
	}
	
	public static boolean minDistanceOnRing(int startNode, int destineNode, int ringCirc) {
		int distance = startNode - destineNode;  
		boolean direction = distance >= 0; 
		
		distance = Math.abs(distance);
		
		if (distance <= (ringCirc - distance)) { 
			return !direction;
		} else {
			return direction;
		}
	}
}