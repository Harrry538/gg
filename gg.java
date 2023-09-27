 while (scanner.hasNext()) {
                int vi = scanner.nextInt() - 1;
                int wi = scanner.nextInt() - 1;
                BoolExpr[] edgeConstraints = new BoolExpr[M];
                for (int c = 0; c < M; c++) {
                    edgeConstraints[c] = ctx.mkNot(ctx.mkEq(pv[vi][c], pv[wi][c]));
                }
                solver.add(ctx.mkOr(edgeConstraints));
            }
