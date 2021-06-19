void type_casses(tree type)
{
  //printf("%s\n", TREE_TYPE(type));
  switch (TREE_CODE(type))
  {
    case INTEGER_CST:
    {
      printf("%ld", (unsigned int)(TREE_INT_CST_HIGH(type) << HOST_BITS_PER_WIDE_INT) + TREE_INT_CST_LOW(type));
      break;
    }
    case STRING_CST:
    {
      printf("`%s`", TREE_STRING_POINTER(type));
      break;
    }
    case IDENTIFIER_NODE:
    {
      printf("%s", IDENTIFIER_POINTER(type));
      break;
    }
    case ARRAY_REF:
    {
      tree arr = TREE_OPERAND(type, 0);
      tree ind = TREE_OPERAND(type, 1);
      type_casses(arr);
      printf("[");
      type_casses(ind);
      printf("]");
      break;
    }
    case MEM_REF:
    {
      tree ptr = TREE_OPERAND(type, 0);
      printf("*");
      type_casses(ptr);
      break;
    }
    case SSA_NAME:
    {
      if (SSA_NAME_IDENTIFIER(type)) type_casses(SSA_NAME_IDENTIFIER(type));
      printf("_%d", SSA_NAME_VERSION(type));
      break;
    }
    default: printf("/:( => %d/", TREE_CODE(type));
  }
}

void expr_casses(enum tree_code code)
{
  switch(code) 
  {
    case PLUS_EXPR: 
    {
    printf("+");
    break;
    }
    case MINUS_EXPR: 
    {
    printf("-");
    break;
    }
    case MULT_EXPR: 
    {
    printf("*");
    break;
    }
    case LT_EXPR: 
    {
    printf("<");
    break;
    }
    case LE_EXPR: 
    {
    printf("<=");
    break;
    }
    case GT_EXPR: 
    {
    printf(">");
    break;
    }
    case GE_EXPR: 
    {
    printf(">=");
    break;
    }
    case EQ_EXPR: 
    {
    printf("==");
    break;
    }
    case NE_EXPR: 
    {
    printf("<>");
    break;
    }
    default: printf("/:< => %d/", code);
  }
}

void lab_print(void) 
{
  basic_block bb;
  gimple_stmt_iterator gsi, psi, msi;
  gimple stmt;
  printf("function = %s;\n", function_name(cfun));
  FOR_EACH_BB_FN(bb, cfun) {
    edge e;
    edge_iterator ei;
    printf("{\n");
    printf("node number = %d;\n", bb->index);
    printf("Incoming edges from nodes: ");
    FOR_EACH_EDGE(e, ei, bb->preds)
    {
      printf(" %d", e->src->index);
    }

    printf(";\nOutgoing edges to nodes: ");
    FOR_EACH_EDGE(e, ei, bb->succs)
    {
      printf(" %d", e->dest->index);
    }
    printf(";\n");

    for (msi = gsi_start_phis(bb); !gsi_end_p(msi); gsi_next(&msi))
    {	    
      stmt = gsi_stmt(msi);
      if (gimple_code(stmt) == GIMPLE_PHI)
      {
        //phi
	printf("phi => (");
	int sz = gimple_phi_num_args(stmt);
	for (int j = 0; j < sz; j++)
	{
	  tree tp = gimple_phi_arg(stmt, j)->def;
	  type_casses(tp);
	  if (j < sz - 1) printf(", ");
	}
	printf(");\n");
      }
    }

    for (gsi = gsi_start_bb (bb); !gsi_end_p (gsi); gsi = psi)
    {
	  //printf("A\n");
	  stmt = gsi_stmt(gsi);

          if (gimple_code(stmt) == GIMPLE_COND)
	  {
	    //cond
	    printf("cond(");
	    enum tree_code code = gimple_cond_code(stmt);
	    tree lhs = gimple_cond_lhs(stmt);
	    type_casses(lhs);
	    expr_casses(code);
	    tree rhs = gimple_cond_rhs(stmt);
	    type_casses(rhs);
	    printf(")\n");
	  }/* else if (gimple_code(stmt) == GIMPLE_CALL) 
	  {
	    printf("call:\n");
	    //call
	  } */else if (gimple_code(stmt) == GIMPLE_ASSIGN) 
	  {
	    //assign
	    printf("assign(");
	    if (gimple_num_ops(stmt) == 2)
	    {
	      tree lhs = gimple_assign_lhs(stmt);
	      tree rhs = gimple_assign_rhs1(stmt);
	      type_casses(lhs);
	      printf("=");
	      type_casses(rhs);
	      printf(")\n");
	    }
	    else if (gimple_num_ops(stmt) == 3)
	    {
	      tree lhs = gimple_assign_lhs(stmt);
	      tree rhs1 = gimple_assign_rhs1(stmt);
	      tree rhs2 = gimple_assign_rhs2(stmt);
	      type_casses(lhs);
	      printf("=");
	      type_casses(rhs1);
              expr_casses(gimple_assign_rhs_code(stmt));
	      type_casses(rhs2);
	      printf(")\n");
	    }
	  } else 
	  {
	    printf("other => ");
	    print_gimple_stmt(stdout, stmt, 0, TDF_SLIM);
	    //else
	  }
	  psi = gsi;
	  gsi_next (&psi);
    }
    

    printf("}\n");
  }
}

/* Pass entry points.  */
static unsigned int
tree_ssa_dce (void)
{
  printf("----------------------------\n");
  lab_print();
  return perform_tree_ssa_dce (/*aggressive=*/false);
}