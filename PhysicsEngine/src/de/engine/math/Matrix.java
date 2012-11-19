package de.engine.math;

import java.text.MessageFormat;

public class Matrix 
{
    public enum SubstitutionDirection { FORWARD, BACKWARD }
    
    public static final Integer ZEILENSUMMENNORM = 0; 
    public static final Integer FROBENIUSNORM    = 1; 
    
    private int rows = 2;
    private int cols = 2;
    
	Double values[][] = new Double[2][2];
	
	public Matrix() {
	}

	public Matrix(double d11, double d12, double d21, double d22) {
	    values[0][0] = d11;
	    values[0][1] = d12;
	    values[1][0] = d21;
	    values[1][1] = d22;
	}
	
	public Vector multVector(Vector vec) {
		return new Vector(values[0][0] * vec.getX() + values[0][1] * vec.getY(),
		        values[1][0] * vec.getX() + values[1][1] * vec.getY());
	}
	
	
	// import from numeric project is major essential for jakobiMatrix calculation
    public int getRows()
    {
        return 2;
    }
    
    
    public int getCols()
    {
        return 2;
    }
    
    
    public Double get(int row, int col)
    {
        return values[row][col];
    }
    
    
    public void set(int row, int col, Double value)
    {
        values[row][col] = value;
    }
    
    
    public Matrix jakobiMatrix(Matrix functions)
    {
        for(int row=0; row<functions.getRows(); row++) 
        {
            for(int col=0; col<functions.getCols(); col++) 
            {
                values[row][col] = functions.get(row, col);
            }
        }

        return this;
    }
    
    
    public Double norm( int n ) throws RuntimeException
    {
        if( n==0 ) return zeilensummenNorm();
        if( n==1 ) return frobeniusNorm();
              
        throw new RuntimeException( MessageFormat.format( "MathLib.getNorm() liefert den Wert {0}, für welche es keine Normimplementierung für Matrizen gibt.", n ) );
    }
  
  
    private Double zeilensummenNorm()
    {
        Double sum = 0d;
        Double max = 0d;
          
        for(int t=0; t<2; t++) 
        {
            for(int i=0; i<2; i++) 
            {
                sum = sum + Math.abs( values[t][i] );
            }
            if ( max.compareTo( sum ) == -1 ) max = sum;
                  
            sum = 0d;
        } 
        return max;
    }
  
  
  private Double frobeniusNorm()
  {
      Double sum = 0d;
      
      for(int row=0; row < 2; row++)
      {
          for(int col=0; col < 2; col++)
          {
              sum = sum + values[row][col]*values[row][col];
          }
      }
      return Math.sqrt( sum );
  }
  
  
  public Vector solveX(Vector b) throws ArithmeticException 
  {
      Vector clone_b = b.clone();
      
      Matrix L = getL( b.clone());
//      System.out.println( "L: "+L.get(0, 0)+"|"+L.get(0,1)+"|"+L.get(1, 0)+"|"+L.get(1,1)+" || Clone_b: "+clone_b.get(0)+"|"+clone_b.get(1) );

      Matrix U = getU( clone_b );
//      System.out.println( "U: "+ U.get(0, 0)+"|"+U.get(0,1)+"|"+U.get(1, 0)+"|"+U.get(1,1)+" || Clone_b: "+clone_b.get(0)+"|"+clone_b.get(1) );
      
      Vector y = substitution( L, clone_b, SubstitutionDirection.FORWARD  ); 
      Vector x = substitution( U, y,       SubstitutionDirection.BACKWARD );
      
//      System.out.println( "x: "+ x.get(0)+"|"+x.get(1) );
      
      return x;
  }
  
  public Matrix getL(Vector b)
  {
      return doLUDecomposition(0, b).item2;
  }
  
  public Matrix getU(Vector b)
  {
      return doLUDecomposition(1, b).item2;
  }
  
  /**
   * Gibt eine Tuple zurück, die entweder eine Matrix (für L oder U) hält oder ein Vector (für die Permutationen)
   * @param which_matrix Gibt an, ob L (=0), U (=1), oder lperm(=2) zurückgegeben wird
   * @param b Ist ein Vektor, der die Ergebnisse von den Gleichungssystemen darstellt
   */
  private Tuple<Vector, Matrix> doLUDecomposition(int which_matrix, Vector b) throws ArithmeticException 
  {      
      Double temp = 0d;
      Matrix    U = clone();
      Matrix    L = identity();
      
      //Reihenfolge der Vertauschung bei der Pivotstrategie(Permutationen)
      Vector    lperm = new Vector();
      
      // Ergebnis ist [1, 2, 3, 4, ...] als Vektor
      for (int cellOfVector = 0; cellOfVector < rows; cellOfVector++)
      {
          lperm.set(cellOfVector, cellOfVector + 1d );
      }
      
      for(int row=0; row < L.rows; row++) 
      { 
          // Pivotisierung + Gaussschritte, reduzierte Zeilenstufenform
//          if (MathLib.isPivotStrategy())
//          {
//              lperm = lperm.swapRows(row, pivotColumnStrategy(U, b, row )); 
//          }
          
          for(int t=row; t<U.rows-1; t++) 
          {
              temp = U.values[t+1][row] / U.values[row][row];
              
              for(int i=row; i<U.rows; i++)
              { 
                  U.values[t+1][i] = U.values[t+1][i] - temp * U.values[row][i];
              }  
              U.values[t+1][row] = temp;
          }
      }
      
      for(int row=0; row<L.rows; row++) 
      { // Trenne Matizen U und L voneinander
          for(int col=0; col<L.cols; col++) 
          {
              if (row>col) {
                  L.values[row][col] = U.values[row][col];
                  U.values[row][col] = 0d;
              }
          }   
      }
      
      if(which_matrix == 0) 
      {
          return new Tuple<Vector, Matrix>(null, L);
      }
      if(which_matrix == 1)
      {
          return new Tuple<Vector, Matrix>(null, U);
      }
      else
      {
          return new Tuple<Vector, Matrix>(lperm, null);
      }
  }
  
  /**
   * Ermöglicht die Vorwärts und Rückwärtssubstitution von einer Matrix mit einem Vector b
   * 
   * @param matrix Matrix, die man Substituieren will
   * @param b Vektor, den man Substituieren will
   * @param str gibt an, wie man Substituieren will "forward" oder "backward"
   */
  public Vector substitution( Matrix matrix, Vector b, SubstitutionDirection direction ) throws ArithmeticException
  {
      Double term0 = 0d;
      Double term1 = 0d;
      Double term2 = 0d;
      Vector     y = new Vector();
      
      if ( direction == SubstitutionDirection.FORWARD)
      {
          y.set( 0, b.get(0) );
          
          for(int row=1; row<2; row++)
          {
              term0 = term0 + matrix.values[row][0] * y.get(0);

              term1 = b.get(row) - term0;
              term2 = term1 / matrix.values[row][row];
              
              y.set( row, term2);
          }
      }
      
      if ( direction == SubstitutionDirection.BACKWARD )
      {
          int dim = 1;
          
          y.set(dim, b.get(dim) / matrix.values[dim][dim] );
          
          for(int row=dim; row>=0; row--)
          {
              term0 = 0d;
              for(int i=0; i<dim-row; i++)
              {
                  term0 = term0 + matrix.values[row][dim-i] * y.get(dim-i) ;
              }
              term1 = b.get(row) - term0 ;
              term2 = term1 / matrix.values[row][row];
              y.set( row, term2);
          }
      }
      
      return y;
  }
  
  @Override
  public Matrix clone()
  {
      Matrix copy = new Matrix();
      
      for(int row=0; row<rows; row++) {
          for(int col=0; col<cols; col++) {
              copy.values[row][col] = values[row][col];
          }
      }
      return copy;
  }
  
  public Matrix identity()
  {
      Matrix identity = new Matrix();
      for (int i = 0; i < rows; i++)
      {
          identity.set(i, i, 1d);
      }
      
      return identity;
  }
  
  /**
   * Anwenden der Zeilenpivotstrategie, bei welcher Zeilen vertauscht werden nach dem Kriterium,
   * dass ein absoluter Wert aus einer Zeile der aktuellen Spalte ermittelt wird und die Zeile 
   * mit dem höchsten Wert wird mit der aktuellen Reihe int row getauscht
   * Zum Schluss wird die Zeile als int ausgegeben, die den maximalen Wert einer Zeile von der aktuellen
   * Spalte besitzt.
   * @param matrix Matrix, für die die Pivotstrategie angewendet werden soll
   * @param vector Vektor, für die die Pivotstrategie angewendet werden soll
   * @param row Anfangsreihe, bei welcher angefangen wird, die Zeilen zu vertauschen
   */
  public int pivotColumnStrategy( Matrix matrix, Vector b, int row ) throws ArithmeticException
  {
      if (matrix == null)
      {
          throw new ArithmeticException("Die Matrix als Input für die pivotColumnStrategy-Methode ist Null!");
      }
      
      if (b != null)
      {
          if (this.getRows() != b.getLength())
          {
              throw new ArithmeticException("Die Inputmatrix und der Inputvektor sind nicht gleichlang für die Methode pivotColumnStrategy!");
          }
      }

      if (row < 0 || row > (matrix.getRows() - 1))
      {
          throw new ArithmeticException("Der row-Input bei der Methode pivotColumnStrategy liegt ausserhalb des gültigen Bereiches!");
      }
      
      Double maximum = 0d;
      Double    temp = 0d;
      int    rowposition = matrix.rows == (row + 1) ? row : 0;
      boolean    rowswap = false;
      
      for(int t=0; t<matrix.getRows()-row; t++)
      {                              
          for(int i=row+t; i<matrix.getCols(); i++)
          {           
              matrix.values[i][row] = Math.abs(matrix.values[i][row]);
              
              if (matrix.values[i][row].compareTo( maximum ) == 1 )
              {  
                  rowposition = i;                                             // Markiere Zeile mit Maximum
                  maximum     = matrix.values[i][row];
                  rowswap     = true;
              }
          }
          
          if (rowswap && rowposition!=row )
          {
              for(int col=0; col<matrix.getCols(); col++)
              { // Zeilenvertauschung der Matrix
                  temp                            = matrix.values[row][col];
                  matrix.values[row][col]         = matrix.values[rowposition][col];
                  matrix.values[rowposition][col] = temp;
              }
              
              if(b!=null) 
              {
                  temp = b.get(row);                                               // Zeilenvertauschung des Vektors
                  b.set(row, b.get(rowposition));
                  b.set(rowposition, temp);
              }
              
              rowswap = false;
          }
      }
      return rowposition;
  }
}
