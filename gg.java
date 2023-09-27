import com.microsoft.z3.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GraphColoring {

    public static void main(String[] args) {
        try {
            // Create a Z3 solver context
            Context ctx = new Context();

            // Read input from input.txt
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String[] firstLine = reader.readLine().split(" ");
            int N = Integer.parseInt(firstLine[0]);
            int M = Integer.parseInt(firstLine[1]);

            // Create variables for color assignments
            Map<Integer, IntExpr> vertexToColor = new HashMap<>();
            for (int i = 1; i <= N; i++) {
                vertexToColor.put(i, ctx.mkIntConst("color_" + i));
            }

            // Create Z3 solver
            Solver solver = ctx.mkSolver();

            // Add constraints to the solver
            for (int i = 1; i <= N; i++) {
                IntExpr color = vertexToColor.get(i);
                solver.add(ctx.mkAnd(ctx.mkGe(color, ctx.mkInt(1)), ctx.mkLe(color, ctx.mkInt(M)));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] edge = line.split(" ");
                int v1 = Integer.parseInt(edge[0]);
                int v2 = Integer.parseInt(edge[1]);

                // Add constraint: vertices connected by an edge cannot have the same color
                solver.add(ctx.mkNot(ctx.mkEq(vertexToColor.get(v1), vertexToColor.get(v2)));
            }
            reader.close();

            // Check for a solution
            if (solver.check() == Status.SATISFIABLE) {
                Model model = solver.getModel();
                BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

                for (int i = 1; i <= N; i++) {
                    int color = model.evaluate(vertexToColor.get(i), false).getInt();
                    writer.write(i + " " + color + "\n");
                }

                writer.close();
            } else {
                // No solution
                BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
                writer.write("No Solution");
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
