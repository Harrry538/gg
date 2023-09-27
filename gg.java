import com.microsoft.z3.*;

import java.io.*;
import java.util.*;

public class GraphColoringSolver {
    public static void main(String[] args) {
        // Initialize Z3 context
        try (Context ctx = new Context()) {
            // Read input from input.txt
            Scanner scanner = new Scanner(new File("input.txt"));
            int N = scanner.nextInt(); // Number of vertices
            int M = scanner.nextInt(); // Number of colors

            // Create Z3 solver
            Solver solver = ctx.mkSolver();

            // Create variables pv,c for color(v) = c
            BoolExpr[][] pv = new BoolExpr[N][M];
            for (int v = 0; v < N; v++) {
                for (int c = 0; c < M; c++) {
                    pv[v][c] = ctx.mkBoolConst("p" + (v + 1) + "_" + (c + 1));
                }
            }

            // Describe the formula asserting every vertex is colored
            for (int v = 0; v < N; v++) {
                BoolExpr[] colorConstraints = new BoolExpr[M];
                for (int c = 0; c < M; c++) {
                    colorConstraints[c] = pv[v][c];
                }
                solver.add(ctx.mkOr(colorConstraints));
            }

            // Describe the formula asserting every vertex has at most one color
            for (int v = 0; v < N; v++) {
                BoolExpr[] oneColorConstraints = new BoolExpr[M];
                for (int c = 0; c < M; c++) {
                    oneColorConstraints[c] = pv[v][c];
                }
                solver.add(ctx.mkAtMost(oneColorConstraints, 1));
            }

            // Create an ordering constraint for color assignment
            for (int v = 0; v < N; v++) {
                for (int c = 0; c < M - 1; c++) {
                    solver.add(ctx.mkImplies(pv[v][c], ctx.mkNot(pv[v][c + 1]));
                }
            }

            // Read the edges and describe the formula asserting no two connected vertices have the same color
            while (scanner.hasNext()) {
                int vi = scanner.nextInt() - 1;
                int wi = scanner.nextInt() - 1;
                for (int c = 0; c < M; c++) {
                    solver.add(ctx.mkImplies(pv[vi][c], ctx.mkNot(pv[wi][c]));
                }
            }

            // Check if there is a satisfying solution
            if (solver.check() == Status.SATISFIABLE) {
                // Get the model
                Model model = solver.getModel();

                // Write the solution to output.txt
                try (PrintWriter writer = new PrintWriter("output.txt")) {
                    for (int v = 0; v < N; v++) {
                        for (int c = 0; c < M; c++) {
                            if (model.eval(pv[v][c], true).isTrue()) {
                                writer.println((v + 1) + " " + (c + 1));
                            }
                        }
                    }
                }
            } else {
                // No solution
                try (PrintWriter writer = new PrintWriter("output.txt")) {
                    writer.println("No Solution");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Z3Exception e) {
            e.printStackTrace();
        }
    }
}
